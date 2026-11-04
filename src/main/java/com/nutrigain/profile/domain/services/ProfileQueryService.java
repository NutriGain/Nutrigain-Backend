package com.nutrigain.profile.domain.services;

import com.nutrigain.profile.domain.model.queries.GetHealthSummaryQuery;
import com.nutrigain.profile.domain.model.valueobjects.HealthSummary;

public interface ProfileQueryService {
    HealthSummary handle(GetHealthSummaryQuery query);
}
