package com.github.evansb.mrt.trip;

import com.github.evansb.mrt.station.Station;
import java.time.Duration;
import lombok.Value;

@Value
public class Step {
  Station from;
  Station to;
  Duration duration;
}
