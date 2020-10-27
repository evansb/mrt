package com.github.evansb.mrt.schedule;

import java.time.Instant;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class NightSchedule implements Schedule {
  private final Predicate<Instant> activeTimes =
      Schedule.isBetweenHours(22, 24).or(Schedule.isBetweenHours(0, 5));

  @Override
  public boolean isActive(Instant timestamp) {
    return activeTimes.test(timestamp);
  }
}
