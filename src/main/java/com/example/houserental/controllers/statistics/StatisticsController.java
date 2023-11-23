package com.example.houserental.controllers.statistics;


import com.example.houserental.controllers.models.GeneralGrow;
import com.example.houserental.services.statistics.StatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
@Slf4j
public class StatisticsController {
    private final StatisticService statisticService;
    @GetMapping("/general-grow")
    public ResponseEntity<Object> generalGrowStatisticFromLastMonth(){
        GeneralGrow generalGrow = statisticService.generalGrowStatisticFromLastMonth();
        if(generalGrow != null){
            return ResponseEntity.ok(generalGrow);
        }
        return ResponseEntity.internalServerError().build();
    }
}

