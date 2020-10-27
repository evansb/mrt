package com.github.evansb.mrt.web;

import com.github.evansb.mrt.trip.Step;
import com.github.evansb.mrt.trip.Trip;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class TripPresenter {
  public String present(Trip trip) {
    if (trip.getSteps().isEmpty()) {
      return "";
    }

    StringBuilder builder = new StringBuilder();
    String startLine = String.format("Start: %s\n", present(trip.getStartTime()));
    String endLine = String.format("End: %s\n", present(trip.getEndTime()));
    String totalDuration = String.format("Total: %s minutes\n", trip.getDuration().toMinutes());
    builder.append(totalDuration);
    builder.append(startLine);
    builder.append(endLine);
    for (Step step : trip.getSteps()) {
      builder.append(present(step));
      builder.append("\n");
    }
    return builder.toString();
  }

  private String present(Instant instant) {
    return LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Singapore"))
        .format(DateTimeFormatter.ofPattern("HH:mm"));
  }

  private String present(Step step) {
    String instruction;
    if (step.getFrom().isConnectingTo(step.getTo())) {
      instruction =
          String.format(
              "Change from %s line to %s line",
              step.getFrom().getLineCode(), step.getTo().getLineCode());
    } else {
      instruction =
          String.format(
              "Take %s line from %s to %s",
              step.getFrom().getLineCode(), step.getFrom().getName(), step.getTo().getName());
    }
    return instruction + String.format(" (%d m)", step.getDuration().toMinutes());
  }
}
