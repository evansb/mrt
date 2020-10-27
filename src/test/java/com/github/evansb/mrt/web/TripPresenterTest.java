package com.github.evansb.mrt.web;

import static org.junit.Assert.assertEquals;

import com.github.evansb.mrt.trip.Trip;
import java.time.Instant;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class TripPresenterTest {
  @Test
  public void presentEmpty() {
    TripPresenter presenter = new TripPresenter();

    assertEquals("", presenter.present(new Trip(Instant.now(), new ArrayList<>())));
  }
}
