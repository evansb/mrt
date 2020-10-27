package com.github.evansb.mrt.station;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** CSV representation of a {@link Station} */
@Data
@JsonPropertyOrder({"code", "name", "openingDate"})
@AllArgsConstructor
@NoArgsConstructor
public class StationCsv {
  String code = "";
  String name = "";
  String openingDate = "";

  private static Pattern codePattern = Pattern.compile("([A-Z]{2})([0-9]{1,2})");
  private static ZoneOffset SGT = ZoneOffset.ofHours(8);

  /**
   * Convert to {@link Station}
   *
   * @throws IllegalArgumentException if the station code cannot be parsed
   */
  public Station toStation() {
    Matcher matcher = codePattern.matcher(this.code);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Unable to parse station code " + this.code);
    }
    Instant openAt =
        LocalDate.parse(this.openingDate, DateTimeFormatter.ofPattern("d MMMM uuuu"))
            .atStartOfDay(ZoneId.of("Asia/Singapore"))
            .toInstant();

    String lineCode = matcher.group(1);
    short lineNumber = Short.parseShort(matcher.group(2), 10);

    return Station.builder()
        .name(this.name)
        .lineCode(lineCode)
        .lineNumber(lineNumber)
        .openAt(openAt)
        .build();
  }
}
