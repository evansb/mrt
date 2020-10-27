package com.github.evansb.mrt.trip;

import com.github.evansb.mrt.station.Station;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class DefaultStepCalculator implements StepCalculator {
  @Override
  public Optional<Duration> calculateStepDuration(
      Instant startTime, Station source, Station destination) {
    if (source.isConnectingTo(destination)) {
      return Optional.of(Duration.ofMinutes(10));
    }

    if ("DT".equals(source.getLineCode()) || "TE".equals(source.getLineCode())) {
      return Optional.of(Duration.ofMinutes(8));
    }

    return Optional.of(Duration.ofMinutes(10));
  }
}
