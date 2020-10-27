package com.github.evansb.mrt.station;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class StationServiceTest {
  StationService subject;

  @BeforeEach
  void setSubject() {
    subject = Mockito.mock(StationService.class);
    Mockito.when(subject.getConnectingStations()).thenCallRealMethod();
  }

  @Test
  void getConnectingStationsEmpty() {
    Mockito.when(subject.getStations()).thenReturn(Collections.emptyList());
    assertTrue(subject.getConnectingStations().isEmpty());
  }

  @Test
  void getConnectingStations() {
    Mockito.when(subject.getStations())
        .thenReturn(
            Arrays.asList(
                Station.builder().lineCode("DT").lineNumber(1).name("Downtown").build(),
                Station.builder().lineCode("EC").lineNumber(10).name("Downtown").build(),
                Station.builder().lineCode("RA").lineNumber(1).name("NotDowntown").build()));
    Map<String, List<Station>> result = subject.getConnectingStations();
    assertEquals(1, result.size());
    assertEquals(2, result.get("Downtown").size());
  }
}
