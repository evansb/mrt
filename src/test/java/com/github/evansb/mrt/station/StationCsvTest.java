package com.github.evansb.mrt.station;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import org.junit.jupiter.api.Test;

public class StationCsvTest {
  @Test
  void toStationSucess() {
    StationCsv csv = new StationCsv("DT1", "Downtown", "1 January 1994");

    Station result = csv.toStation();

    assertEquals("DT", result.getLineCode());
    assertEquals(1, result.getLineNumber());
    assertEquals("Downtown", result.getName());
    assertEquals(Instant.parse("1993-12-31T16:00:00Z"), result.getOpenAt());
  }

  @Test
  void toStationFail() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new StationCsv("DT", "Downtown", "1 January 1994").toStation();
        });

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new StationCsv("11", "Downtown", "1 January 1994").toStation();
        });
  }
}
