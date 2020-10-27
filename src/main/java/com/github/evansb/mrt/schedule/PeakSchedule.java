package com.github.evansb.mrt.schedule;

import java.time.DayOfWeek;
import java.time.Instant;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class PeakSchedule implements Schedule {
  private final Predicate<Instant> activeDays =
      Schedule.isBetweenDays(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
  private final Predicate<Instant> activeTimes =
      Schedule.isBetweenHours(6, 8).or(Schedule.isBetweenHours(18, 20));

  @Override
  public boolean isActive(Instant timestamp) {
    return activeDays.and(activeTimes).test(timestamp);
  }
}
