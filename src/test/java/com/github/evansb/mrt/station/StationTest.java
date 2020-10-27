package com.github.evansb.mrt.station;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class StationTest {
  private final Station DT1 =
      Station.builder().lineCode("DT").lineNumber(1).name("Bukit Panjang").build();
  private final Station NE12 =
      Station.builder().lineCode("NE").lineNumber(12).name("Serangoon").build();
  private final Station NE13 =
      Station.builder().lineCode("NE").lineNumber(13).name("Kovan").build();
  private final Station CC13 =
      Station.builder().lineCode("CC").lineNumber(13).name("Serangoon").build();

  @Test
  public void isConnectingTo() {
    assertTrue(NE12.isConnectingTo(CC13));
    assertTrue(CC13.isConnectingTo(NE12));
    assertFalse(NE12.isConnectingTo(NE13));
    assertFalse(DT1.isConnectingTo(NE13));
  }

  @Test
  public void compareTo() {
    assertTrue(NE12.compareTo(NE13) < 0);
    assertTrue(NE13.compareTo(NE12) > 0);
    assertTrue(DT1.compareTo(NE12) < 0);
  }
}
