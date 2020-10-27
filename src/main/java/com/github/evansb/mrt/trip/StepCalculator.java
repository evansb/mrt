package com.github.evansb.mrt.trip;

import com.github.evansb.mrt.station.Station;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

/**
 * Calculates one-hop of trip between station. Stations must be adjacent with each other, either on
 * the same line or are connecting stations.
 */
public interface StepCalculator {
  /**
   * Returns the duration required to go from source station to destination when the trip is started
   * at specified time.
   *
   * @param startTime trip starting time
   * @param source source station
   * @param destination destination station
   * @return Empty if the trip is not possible, Optional with the duration otherwise
   */
  Optional<Duration> calculateStepDuration(Instant startTime, Station source, Station destination);

  /**
   * Returns the {@link Step} required to go from source station to destination when the trip is
   * started at specified time.
   *
   * @param startTime trip starting time
   * @param source source station
   * @param destination destination station
   * @return Empty if the trip is not possible, Optional with the duration otherwise
   */
  default Optional<Step> calculateStep(Instant startTime, Station source, Station destination) {
    return calculateStepDuration(startTime, source, destination)
        .map(duration -> new Step(source, destination, duration));
  }
}
