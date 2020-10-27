package com.github.evansb.mrt.trip;

import com.github.evansb.mrt.schedule.Schedule;
import com.github.evansb.mrt.station.Station;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

/**
 * Step calculator that delegates the calculation to a set of {@link StepCalculator} based on which
 * {@link Schedule} is active during the trip start time.
 */
public class ScheduledStepCalculator implements StepCalculator {
  private final Map<Schedule, StepCalculator> schedules;
  private final StepCalculator defaultCalculator;

  public ScheduledStepCalculator(
      Map<Schedule, StepCalculator> schedules, StepCalculator defaultCalculator) {
    this.schedules = schedules;
    this.defaultCalculator = defaultCalculator;
  }

  @Override
  public Optional<Duration> calculateStepDuration(
      Instant startTime, Station source, Station destination) {
    return schedules.entrySet().stream()
        .filter(it -> it.getKey().isActive(startTime))
        .findFirst()
        .map(Map.Entry::getValue)
        .orElse(defaultCalculator)
        .calculateStepDuration(startTime, source, destination);
  }
}
