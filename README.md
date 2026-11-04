# NutriGain Backend

Monolito modular (Java 21 + Spring Boot 3.5 + MySQL) con **Domain-Driven Design** y **CQRS**, organizado por *bounded contexts*.

## Ejecutar

```bash
docker compose up -d          # MySQL 8.4 en localhost:3310 (db "nutrigain", root/root)
mvn spring-boot:run           # API en http://localhost:8081
mvn test                      # tests (usan H2 en modo MySQL, perfil "test")
```

- Swagger UI: `http://localhost:8081/swagger-ui.html`
- Variables: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET` (>= 32 bytes, **cambiarlo en produccion**), `JWT_EXPIRATION_MINUTES`, `CORS_ALLOWED_ORIGINS`, `PORT`.
- Al arrancar con la BD vacia se cargan (seeders idempotentes): 3 caminos fitness, 16 comidas, 20 retos (5 extremos) y 5 mensajes motivacionales.

## Bounded contexts

| Contexto | Responsabilidad | Agregados |
|---|---|---|
| `iam` | Registro, login (JWT), usuario | `User` |
| `fitness` | Caminos, rutina semanal, progreso semanal, peso corporal | `FitnessPath`, `RoutineDay`, `WeeklyProgress`, `BodyWeightEntry` |
| `nutrition` | Catalogo de comidas, plan automatico, plan manual | `Meal`, `NutritionPlan`, `ManualMealSelection` |
| `gamification` | Personaje (avatar), racha, retos aleatorios y reto diario extremo | `Avatar`, `Streak`, `Challenge`, `ChallengeAssignment`, `MotivationalMessage` |
| `profile` | Altura, sexo, actividad; IMC y calorias diarias (Mifflin-St Jeor) | `UserProfile` |
| `achievements` | Insignias por hitos (entrenos, retos, racha, nivel) | `AchievementTracker`, `UserBadge` |
| `notifications` | Bandeja in-app alimentada por eventos | `Notification` |
| `social` | Seguir usuarios, ranking global y de amigos por XP | `Competitor`, `Follow` |

Estructura de cada contexto:

```
<contexto>/
  domain/model/{aggregates,valueobjects,commands,queries,events}
  domain/services/            interfaces CommandService / QueryService (CQRS)
  application/internal/{commandservices,queryservices,eventhandlers,outboundservices/acl}
  infrastructure/persistence/jpa/repositories, seed
  interfaces/rest/{resources,transform}, interfaces/acl (facade publica del contexto)
```

`shared/` contiene el kernel compartido (base `AggregateRoot`, publicador de eventos, excepciones, manejo global de errores).

### CQRS
Cada operacion de escritura es un `Command` atendido por un `*CommandService` (transaccional, publica eventos de dominio); cada lectura es un `Query` atendido por un `*QueryService` (`readOnly`). Los controladores solo hablan con estas interfaces.

### Comunicacion entre contextos
- **Eventos de dominio** (sincronos, misma transaccion; ademas `ActivityRecordedEvent` y `BadgeAwardedEvent` alimentan achievements, notifications y social): `UserRegisteredEvent` (iam) hace que fitness genere la rutina, nutrition el plan y gamification el avatar y la racha. `WorkoutCompletedEvent` (fitness) y `ChallengeCompletedEvent` (gamification) otorgan experiencia y mantienen la racha.
- **ACL**: un contexto nunca usa agregados de otro. `FitnessContextFacade` expone solo primitivos; `nutrition` y `gamification` traducen el camino fitness a su propio lenguaje (`NutritionFocus`, `AvatarCharacter`).

### Reglas de negocio destacadas
- Camino -> rutina semanal (3 caminos con plantillas), plan nutricional segun foco (proteina / carbohidrato / ligero) y personaje (Titan / Rayo / Zen).
- Avatar: sube de nivel cada 100 XP. Entrenar da 20 XP; los retos dan su `xpReward`.
- Racha: dias consecutivos con actividad; si pasa mas de un dia sin actividad vuelve a 0.
- **Reto diario extremo**: uno por usuario y dia (idempotente). Escala: cada reto extremo completado avanza al siguiente del catalogo y suma +5 XP (maximo +50).
- Los retos aleatorios nunca son extremos.

## API (`/api/v1`, JWT `Authorization: Bearer <token>`)

Publicos: `POST /auth/sign-up`, `POST /auth/sign-in`, `GET /fitness-paths[/{id}]`. El resto requiere token y trabaja sobre el usuario autenticado (`/me/...`), sin `userId` en la URL.

| Metodo y ruta | Descripcion |
|---|---|
| `GET /users/me` | Usuario autenticado |
| `GET /me/routines` | Rutina semanal |
| `POST /me/routines/{routineDayId}/completions` | Marca el dia como hecho; devuelve el progreso semanal |
| `GET /me/progress`, `GET /me/progress/current` | Historial / semana actual |
| `PUT /me/progress/target-weight` | Peso objetivo |
| `GET`/`POST /me/body-weights` | Historial / registrar peso |
| `GET /meals?type=`, `GET /meals/{id}` | Catalogo de comidas (`BREAKFAST,LUNCH,DINNER,SNACK`) |
| `GET /me/nutrition-plan` | Plan automatico semanal |
| `GET /me/manual-plan`, `PUT`/`DELETE /me/manual-plan/meals/{mealId}` | Plan manual con totales de macros |
| `GET /me/avatar`, `GET /me/streak` | Personaje y racha |
| `GET /challenges?category=` | Catalogo de retos |
| `POST /me/challenges/draw?category=` | Reto aleatorio |
| `GET`/`POST /me/challenges/daily-extreme` | Reto extremo del dia (GET: 404 si aun no se pidio) |
| `POST /me/challenges/{assignmentId}/complete` | Completar reto |
| `GET /motivational-messages/random` | Frase motivacional |
| `GET`/`PUT /me/profile` | Perfil y metricas (IMC, calorias diarias) |
| `GET /me/badges` | Todas las insignias, con cuales estan desbloqueadas |
| `GET /me/notifications?unseenOnly=`, `GET .../unseen-count`, `POST .../{id}/seen`, `POST .../seen-all` | Bandeja de notificaciones |
| `GET /social/leaderboard?limit=`, `GET /me/social/leaderboard` | Ranking global / de amigos |
| `GET /social/users?name=`, `PUT`/`DELETE /me/following/{userId}` | Buscar, seguir y dejar de seguir |

Errores: `{timestamp,status,error,message,details}` (400 validacion, 401 credenciales/token, 404, 409 email duplicado, 422 regla de negocio).

## Mapeo desde la fake API del frontend

| Fake API (json-server) | Nuevo endpoint |
|---|---|
| `GET /users` + comparar password en cliente | `POST /auth/sign-in` (BCrypt + JWT) |
| `POST /users` | `POST /auth/sign-up` |
| `/rutinas?userId=` | `GET /me/routines` |
| `/progresos?userId=` | `GET /me/progress` |
| `/pesos?userId=` | `GET /me/body-weights` |
| `/planesNutricionales?userId=` | `GET /me/nutrition-plan` |
| `/NutricionalManual` + `localStorage` | `GET /meals` + `/me/manual-plan` |
| `/avatares?userId=` | `GET /me/avatar` |
| `/retos` (+ completado solo en memoria) | `GET /challenges`, `POST /me/challenges/draw`, `.../complete` |
| `/mensajesMotivacionales` | `GET /motivational-messages/random` |
| racha calculada con progresos | `GET /me/streak` |

Los nombres de campos pasan a ingles (`nombre`->`name`, `edad`->`age`, `caminoSeleccionado`->`fitnessPathId`, ...).

## Pendiente / siguientes pasos
- `billing`: plan premium (requiere pasarela de pago).
- Recordatorios programados y email/push en notifications.
- Migraciones Flyway en lugar de `ddl-auto=update` antes de produccion.
- Cambiar de camino fitness (regenerar rutina, plan y personaje).
- Probar contra un MySQL real (los tests usan H2 en modo MySQL).
