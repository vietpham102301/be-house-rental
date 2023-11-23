package com.example.houserental.services.statistics;

import com.example.houserental.controllers.models.GeneralGrow;

public interface StatisticService {
    GeneralGrow generalGrowStatisticFromLastMonth();
}
