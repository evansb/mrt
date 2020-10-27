package com.github.evansb.mrt.station;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/** Business logic for {@link Station} */
public interface StationService {
  /** Get all stations */
  Collection<Station> getStations();

  /**
   * Get all connecting stations.
   *
   * <p>Two stations are connecting if they have the same name but different line.
   */
  default Map<String, List<Station>> getConnectingStations() {
    return getStations().stream()
        .collect(Collectors.groupingBy(Station::getName))
        .entrySet()
        .stream()
        .filter(it -> it.getValue().size() >= 2)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  /**
   * Get station by code, e.g DT12, NE12
   *
   * @param code The code of the station (line code + number)
   */
  default Optional<Station> findByCode(String code) {
    return getStations().stream()
        .filter(it -> (it.getLineCode() + it.getLineNumber()).equalsIgnoreCase(code))
        .findFirst();
  }
}
