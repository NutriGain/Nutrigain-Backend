package com.nutrigain.profile.application.internal.queryservices;

import com.nutrigain.profile.application.internal.outboundservices.acl.ExternalUserDataService;
import com.nutrigain.profile.domain.model.aggregates.UserProfile;
import com.nutrigain.profile.domain.model.queries.GetHealthSummaryQuery;
import com.nutrigain.profile.domain.model.valueobjects.BmiCategory;
import com.nutrigain.profile.domain.model.valueobjects.HealthSummary;
import com.nutrigain.profile.domain.services.HealthMetrics;
import com.nutrigain.profile.domain.services.ProfileQueryService;
import com.nutrigain.profile.infrastructure.persistence.jpa.repositories.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class ProfileQueryServiceImpl implements ProfileQueryService {

    private final UserProfileRepository repository;
    private final ExternalUserDataService externalUserDataService;

    public ProfileQueryServiceImpl(UserProfileRepository repository, ExternalUserDataService externalUserDataService) {
        this.repository = repository;
        this.externalUserDataService = externalUserDataService;
    }

    @Override
    public HealthSummary handle(GetHealthSummaryQuery query) {
        UserProfile profile = repository.findByUserId(query.userId()).orElse(null);
        BigDecimal weight = externalUserDataService.fetchLatestWeightKg(query.userId()).orElse(null);
        if (profile == null) {
            return new HealthSummary(null, null, null, weight, null, null, null);
        }
        Double bmi = null;
        BmiCategory category = null;
        Integer calories = null;
        if (weight != null) {
            bmi = HealthMetrics.bmi(weight.doubleValue(), profile.getHeightCm());
            category = HealthMetrics.category(bmi);
            Integer age = externalUserDataService.fetchAge(query.userId()).orElse(null);
            if (age != null) {
                calories = HealthMetrics.dailyCalories(profile.getSex(), weight.doubleValue(),
                        profile.getHeightCm(), age, profile.getActivityLevel());
            }
        }
        return new HealthSummary(profile.getHeightCm(), profile.getSex(), profile.getActivityLevel(), weight,
                bmi, category, calories);
    }
}
