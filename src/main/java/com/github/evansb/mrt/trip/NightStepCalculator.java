package com.github.evansb.mrt.trip;

import com.github.evansb.mrt.station.Station;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class NightStepCalculator implements StepCalculator {
  private final Set<String> inactiveLines = new HashSet<>();

  {
    inactiveLines.add("DT");
    inactiveLines.add("CG");
    inactiveLines.add("CE");
  }

  @Override
  public Optional<Duration> calculateStepDuration(
      Instant startTime, Station source, Station destination) {
    if (inactiveLines.contains(source.getLineCode())
        || inactiveLines.contains(destination.getLineCode())) {
      return Optional.empty();
    }

    if (source.isConnectingTo(destination)) {
      return Optional.of(Duration.ofMinutes(10));
    }

    if ("TE".equals(source.getLineCode())) {
      return Optional.of(Duration.ofMinutes(8));
    }

    return Optional.of(Duration.ofMinutes(10));
  }
}
