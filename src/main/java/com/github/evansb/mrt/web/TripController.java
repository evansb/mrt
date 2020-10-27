package com.github.evansb.mrt.web;

import com.github.evansb.mrt.station.Station;
import com.github.evansb.mrt.station.StationService;
import com.github.evansb.mrt.trip.Trip;
import com.github.evansb.mrt.trip.TripCalculator;
import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/trip")
public class TripController {
  private final StationService stationService;
  private final TripCalculator tripCalculator;
  private final TripPresenter tripPresenter;

  @Autowired
  public TripController(
      StationService stationService, TripCalculator tripCalculator, TripPresenter tripPresenter) {
    this.stationService = stationService;
    this.tripCalculator = tripCalculator;
    this.tripPresenter = tripPresenter;
  }

  @PostMapping
  public String createTrip(
      @RequestParam String from,
      @RequestParam String to,
      @RequestParam(required = false) Instant start) {
    start = start == null ? Instant.now() : start;
    Station source =
        stationService.findByCode(from).orElseThrow(() -> new StationNotFoundException(from));
    Station destination =
        stationService.findByCode(to).orElseThrow(() -> new StationNotFoundException(from));

    Collection<Trip> trips = tripCalculator.calculateTrip(start, source, destination);
    return trips.stream().map(tripPresenter::present).collect(Collectors.joining("---\n\n"));
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(StationNotFoundException.class)
  public void handleStationNotFound() {}
}
