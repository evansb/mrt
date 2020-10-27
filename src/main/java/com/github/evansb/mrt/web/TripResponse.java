package com.github.evansb.mrt.web;

import java.util.List;
import lombok.Value;

@Value
class TripResponse {
  List<String> instructions;
}
