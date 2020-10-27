package com.github.evansb.mrt.schedule;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.util.function.Predicate;

/**
 * Objects that can changes to active/inactive status based on time. It is not very useful by
 * itself, but can be mapped to other components such as
 *
 * <pre>TripService</pre>
 *
 * to implement time-based activation
 */
public interface Schedule {
  /**
   * Whether the schedule is active at the specified timestamp
   *
   * @param timestamp The timestamp
   * @return true if schedule is active, false otherwise
   */
  boolean isActive(Instant timestamp);

  /**
   * Returns a predicate checking whether a timestamp falls in between two days of the week
   *
   * @param from From day, inclusive
   * @param to To day, inclusive
   */
  static Predicate<Instant> isBetweenDays(DayOfWeek from, DayOfWeek to) {
    return (time) -> {
      int dayOfWeek = time.atZone(ZoneId.of("Asia/Singapore")).getDayOfWeek().getValue();
      return dayOfWeek >= from.getValue() && dayOfWeek <= to.getValue();
    };
  }

  /**
   * Returns a predicate checking whether a timestamp falls in between two hours of the day
   *
   * @param from From hour, inclusive
   * @param to To hour, inclusive
   */
  static Predicate<Instant> isBetweenHours(int from, int to) {
    return (time) -> {
      int hour = time.atZone(ZoneId.of("Asia/Singapore")).getHour();
      return hour >= from && hour <= to;
    };
  }
}
