package com.github.evansb.mrt.schedule;

import java.time.Instant;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class DefaultSchedule implements Schedule {
  @Override
  public boolean isActive(Instant timestamp) {
    return true;
  }
}
