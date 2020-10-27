package com.github.evansb.mrt.schedule;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import org.junit.jupiter.api.Test;

public class PeakScheduleTest {
  PeakSchedule subject = new PeakSchedule();

  @Test
  void isActive() {
    // Monday 06:00
    assertTrue(subject.isActive(Instant.parse("2020-10-26T22:00:00Z")));
    // Monday 07:00
    assertTrue(subject.isActive(Instant.parse("2020-10-26T23:00:00Z")));
    // Monday 08:59
    assertTrue(subject.isActive(Instant.parse("2020-10-26T00:59:00Z")));
    // Tuesday 18:00
    assertTrue(subject.isActive(Instant.parse("2020-10-27T10:59:00Z")));

    // Monday 09:00
    assertFalse(subject.isActive(Instant.parse("2020-10-27T01:00:00Z")));
    // Monday 14:00
    assertFalse(subject.isActive(Instant.parse("2020-10-27T06:00:00Z")));
    // Saturday 06:00
    assertFalse(subject.isActive(Instant.parse("2020-10-30T22:00:00Z")));
  }
}
