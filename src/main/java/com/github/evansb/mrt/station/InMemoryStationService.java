package com.github.evansb.mrt.station;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InMemoryStationService implements StationService {
  private volatile Set<Station> stations;

  public InMemoryStationService(InputStream csv) throws IOException {
    this.load(csv);
  }

  @Override
  public Collection<Station> getStations() {
    return stations;
  }

  private void load(InputStream csv) throws IOException {
    CsvMapper mapper = CsvMapper.builder().build();
    MappingIterator<StationCsv> iter =
        mapper.readerWithTypedSchemaFor(StationCsv.class).readValues(csv);
    Set<Station> stations =
        iter.readAll().stream().map(StationCsv::toStation).collect(Collectors.toSet());

    if (log.isDebugEnabled()) {
      for (Station station : stations) {
        log.debug("Loaded {}", station);
      }
    }

    this.stations = stations;

    log.info("Initialised in-memory station storage with {} stations", stations.size());
  }
}
