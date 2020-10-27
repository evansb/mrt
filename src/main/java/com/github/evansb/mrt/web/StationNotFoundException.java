package com.github.evansb.mrt.web;

public class StationNotFoundException extends RuntimeException {
  public StationNotFoundException(String stationName) {
    super("Station not found: " + stationName);
  }
}
