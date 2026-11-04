# Despliegue del backend

El backend se publica como una imagen Docker (`Dockerfile` en la raíz) y necesita un MySQL. Esta guía usa **Railway**, que ofrece MySQL gestionado y construye directamente desde GitHub. Los nombres exactos de los menús pueden cambiar; los pasos y las variables son los importantes.

## Qué necesita la aplicación

| Variable | Obligatoria | Descripción |
|---|---|---|
| `DB_URL` | sí | URL JDBC de MySQL, por ejemplo `jdbc:mysql://HOST:3306/BASE?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC` |
| `DB_USERNAME` | sí | Usuario de MySQL |
| `DB_PASSWORD` | sí | Contraseña de MySQL |
| `JWT_SECRET` | sí | Secreto para firmar los tokens, mínimo 32 bytes. **Debe ser único y privado** |
| `CORS_ALLOWED_ORIGINS` | no | Orígenes permitidos separados por coma. Por defecto incluye `http://localhost:4200` y `https://nutrigain-frontend.vercel.app` |
| `PORT` | no | La plataforma suele definirlo sola. Por defecto 8081 |
| `JWT_EXPIRATION_MINUTES` | no | Duración del token. Por defecto 1440 (24 h) |
| `DDL_AUTO` | no | `update` por defecto: crea y ajusta las tablas al arrancar |

Generar un `JWT_SECRET` seguro:

```bash
node -e "console.log(require('crypto').randomBytes(48).toString('base64'))"
```

## Pasos en Railway

1. Crea una cuenta en railway.com e inicia un proyecto nuevo: **New Project → Deploy from GitHub repo** y elige `Nutrigain-Backend` (rama `main`). Railway detecta el `Dockerfile` y construye la imagen.
2. En el mismo proyecto agrega la base: **New → Database → MySQL**. Esto crea un servicio llamado `MySQL` con sus variables (`MYSQLHOST`, `MYSQLPORT`, `MYSQLUSER`, `MYSQLPASSWORD`, `MYSQLDATABASE`).
3. En el servicio del backend abre **Variables** y define (los `${{ ... }}` son referencias a las variables del servicio MySQL):

   ```
   DB_URL=jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
   DB_USERNAME=${{MySQL.MYSQLUSER}}
   DB_PASSWORD=${{MySQL.MYSQLPASSWORD}}
   JWT_SECRET=<el valor generado arriba>
   ```

4. En **Settings → Networking** genera un dominio público para el backend (algo como `https://nutrigain-backend-production.up.railway.app`).
5. En **Settings** configura el *healthcheck path* como `/actuator/health`.
6. Espera a que termine el despliegue (el primer arranque crea las tablas y carga los datos iniciales).

## Comprobar que funciona

```bash
curl https://TU-DOMINIO/actuator/health          # {"status":"UP"}
curl https://TU-DOMINIO/api/v1/fitness-paths     # lista de los 3 caminos
```

La documentación interactiva de la API queda en `https://TU-DOMINIO/swagger-ui.html`.

## Conectar el frontend

1. En el repo del frontend, edita `src/environments/environment.prod.ts` y pon la URL del backend terminada en `/api/v1`:

   ```ts
   apiUrl: 'https://TU-DOMINIO/api/v1'
   ```

2. Haz commit y push a `main`; Vercel vuelve a desplegar solo.
3. Si el frontend usa un dominio distinto de `nutrigain-frontend.vercel.app`, agrégalo en `CORS_ALLOWED_ORIGINS` del backend.

## Antes de usarlo con usuarios reales

- **Esquema de la base:** `DDL_AUTO=update` es cómodo, pero para producción conviene migraciones versionadas (Flyway), para que los cambios de tablas sean controlados y reversibles.
- **Copias de seguridad:** activa los backups del MySQL gestionado.
- **Swagger público:** `/swagger-ui.html` está abierto. Si no lo quieres expuesto, hay que restringirlo en `SecurityConfig`.
- **Secretos:** nunca los subas al repositorio; solo variables de entorno de la plataforma.
- **Costos:** Railway cobra por uso; revisa el plan antes de dejar el proyecto activo.
