package com.github.evansb.mrt.trip;

import com.github.evansb.mrt.station.Station;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.YenKShortestPath;
import org.jgrapht.graph.DefaultEdge;

/** Trip calculator that return the trip ordered by duration the trip. */
public class ShortestDurationFirstTripCalculator implements TripCalculator {
  // Show 4 trips at most
  private static final int RESULT_LIMIT = 4;
  private final StepCalculator stepCalculator;
  private final StationGraphFactory stationGraphFactory;

  public ShortestDurationFirstTripCalculator(
      StepCalculator stepCalculator, StationGraphFactory stationGraphFactory) {
    this.stepCalculator = stepCalculator;
    this.stationGraphFactory = stationGraphFactory;
  }

  @Override
  public Collection<Trip> calculateTrip(Instant startTime, Station from, Station to) {
    return new YenKShortestPath<>(stationGraphFactory.getGraph())
        .getPaths(from, to, RESULT_LIMIT).stream()
            .map(path -> createTrip(startTime, path))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .sorted(Comparator.comparing(Trip::getDuration))
            .collect(Collectors.toList());
  }

  private Optional<Trip> createTrip(Instant startTime, GraphPath<Station, DefaultEdge> path) {
    List<Step> steps = new ArrayList<>();

    List<Station> stations = path.getVertexList();
    if (stations.size() == 1) {
      return Optional.empty();
    }

    Instant time = startTime;
    Station previous = stations.get(0);
    for (Station current : stations.subList(1, stations.size())) {
      // Station not yet open, trip invalid
      if (!current.isOpenAt(time)) {
        return Optional.empty();
      }

      Optional<Step> step = stepCalculator.calculateStep(time, previous, current);
      // Single hop Trip invalid, entire trip invalid
      if (!step.isPresent()) {
        return Optional.empty();
      }

      steps.add(step.get());
      time = time.plus(step.get().getDuration());
      previous = current;
    }

    return Optional.of(new Trip(startTime, steps));
  }
}
