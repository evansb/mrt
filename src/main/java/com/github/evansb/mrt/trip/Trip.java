package com.github.evansb.mrt.trip;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import lombok.Value;

@Value
public class Trip {
  /** Trip starting time in epoch milliseconds, UTC */
  Instant startTime;

  /** List of steps in the trip */
  Collection<Step> steps;

  /** Get the total duration of the trip. */
  public Duration getDuration() {
    return steps.stream().map(Step::getDuration).reduce(Duration::plus).orElse(Duration.ZERO);
  }

  /** Get the end of the trip in epoch milliseconds, UTC */
  public Instant getEndTime() {
    return startTime.plus(getDuration());
  }
}
