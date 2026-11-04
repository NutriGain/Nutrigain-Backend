package com.nutrigain.fitness.domain.services;

import com.nutrigain.fitness.domain.model.aggregates.BodyWeightEntry;
import com.nutrigain.fitness.domain.model.aggregates.WeeklyProgress;
import com.nutrigain.fitness.domain.model.queries.GetBodyWeightHistoryQuery;
import com.nutrigain.fitness.domain.model.queries.GetCurrentWeekProgressQuery;
import com.nutrigain.fitness.domain.model.queries.GetWeeklyProgressHistoryQuery;

import java.util.List;

public interface TrainingProgressQueryService {
    List<WeeklyProgress> handle(GetWeeklyProgressHistoryQuery query);

    WeeklyProgress handle(GetCurrentWeekProgressQuery query);

    List<BodyWeightEntry> handle(GetBodyWeightHistoryQuery query);
}
