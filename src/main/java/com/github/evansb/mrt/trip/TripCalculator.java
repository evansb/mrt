package com.github.evansb.mrt.trip;

import com.github.evansb.mrt.station.Station;
import java.time.Instant;
import java.util.Collection;

public interface TripCalculator {
  /**
   * Calculate some possible trips between two stations. Empty collection indicates that no possible
   * trip can be generated. The order of the collection can be used to sort trip by some metric.
   *
   * @param startTime starting time of the trip
   * @param from starting station
   * @param to destination station
   */
  Collection<Trip> calculateTrip(Instant startTime, Station from, Station to);
}
