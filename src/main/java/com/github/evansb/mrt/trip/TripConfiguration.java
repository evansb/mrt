package com.github.evansb.mrt.trip;

import com.github.evansb.mrt.schedule.NightSchedule;
import com.github.evansb.mrt.schedule.PeakSchedule;
import com.github.evansb.mrt.schedule.Schedule;
import com.github.evansb.mrt.station.StationService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TripConfiguration {
  @Bean
  StationGraphFactory stationGraphFactory(StationService stationService) {
    return new StationGraphFactory(stationService);
  }

  @Bean
  StepCalculator singleHopTripCalculator() {
    Map<Schedule, StepCalculator> schedules = new HashMap<>();
    schedules.put(new NightSchedule(), new NightStepCalculator());
    schedules.put(new PeakSchedule(), new PeakStepCalculator());

    return new ScheduledStepCalculator(schedules, new DefaultStepCalculator());
  }

  @Bean
  @Autowired
  TripCalculator tripCalculator(
      StepCalculator stepCalculator, StationGraphFactory stationGraphFactory) {
    return new ShortestDurationFirstTripCalculator(stepCalculator, stationGraphFactory);
  }
}
