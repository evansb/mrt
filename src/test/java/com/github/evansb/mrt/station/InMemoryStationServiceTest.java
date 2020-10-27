package com.github.evansb.mrt.station;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import org.junit.jupiter.api.Test;

public class InMemoryStationServiceTest {
  @Test
  void constructor() throws IOException {
    String csv = "NS1,Jurong East,10 March 1990\n" + "NS2,Bukit Batok,10 March 1990";
    InputStream is = new ByteArrayInputStream(csv.getBytes());
    Collection<Station> stations = new InMemoryStationService(is).getStations();
    assertEquals(2, stations.size());
  }

  @Test
  void constructorDuplicate() throws IOException {
    String csv = "NS1,Jurong East,10 March 1990\n";
    csv += csv;
    InputStream is = new ByteArrayInputStream(csv.getBytes());
    Collection<Station> stations = new InMemoryStationService(is).getStations();
    assertEquals(1, stations.size());
  }

  @Test
  void constructorEmpty() throws IOException {
    String csv = "";
    InputStream is = new ByteArrayInputStream(csv.getBytes());
    Collection<Station> stations = new InMemoryStationService(is).getStations();
    assertEquals(0, stations.size());
  }
}
