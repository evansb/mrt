package com.github.evansb.mrt.trip;

import static org.mockito.Mockito.*;

import com.github.evansb.mrt.schedule.Schedule;
import com.github.evansb.mrt.station.Station;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ScheduledStepCalculatorTest {
  Station source = Station.builder().build();
  Station destination = Station.builder().build();
  Instant startTime = Instant.now();
  Schedule inactive;
  Schedule active;
  StepCalculator day;
  StepCalculator fallback;

  @BeforeEach
  public void setup() {
    inactive = mock(Schedule.class);
    when(inactive.isActive(any())).thenReturn(false);

    active = mock(Schedule.class);
    when(active.isActive(any())).thenReturn(true);

    fallback = mock(StepCalculator.class);
    day = mock(StepCalculator.class);
  }

  @Test
  public void calculateStepDurationDefault() {
    Map<Schedule, StepCalculator> schedules = new HashMap<>();
    schedules.put(inactive, day);
    when(fallback.calculateStepDuration(any(), any(), any()))
        .thenReturn(Optional.of(Duration.ZERO));

    ScheduledStepCalculator calculator = new ScheduledStepCalculator(schedules, fallback);
    calculator.calculateStepDuration(startTime, source, destination);

    verifyNoInteractions(day);
    verify(fallback, only()).calculateStepDuration(startTime, source, destination);
  }

  @Test
  public void calculateStepDurationFound() {
    Map<Schedule, StepCalculator> schedules = new HashMap<>();
    schedules.put(active, day);

    ScheduledStepCalculator calculator = new ScheduledStepCalculator(schedules, fallback);
    calculator.calculateStepDuration(startTime, source, destination);

    verifyNoInteractions(fallback);
    verify(day, only()).calculateStepDuration(startTime, source, destination);
  }
}
