package com.github.evansb.mrt.station;

import java.time.Instant;
import lombok.Builder;
import lombok.Value;

/**
 * Logical representation of MRT Station. Uniqueness are determined by its code; connecting stations
 * are distinct from one another.
 */
@Value
@Builder
public class Station implements Comparable<Station> {
  /** English name of the station */
  String name;

  /** Two-letter code of the station line (e.g DT, NE) */
  String lineCode;

  /** Number of the station line */
  int lineNumber;

  /** When the station is first opened */
  Instant openAt;

  public boolean isOpenAt(Instant time) {
    return time.isAfter(openAt);
  }

  public boolean isConnectingTo(Station other) {
    return this.name.equals(other.name);
  }

  @Override
  public int compareTo(Station other) {
    if (this == other) {
      return 0;
    }
    if (this.lineCode.equals(other.lineCode)) {
      return Integer.compare(this.lineNumber, other.lineNumber);
    }
    return this.lineCode.compareTo(other.lineCode);
  }
}
