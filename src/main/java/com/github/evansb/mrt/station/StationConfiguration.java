package com.github.evansb.mrt.station;

import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class StationConfiguration {
  @Bean
  Resource stationsCsv() {
    return new ClassPathResource("stations.csv");
  }

  @Bean
  StationService stationService() throws IOException {
    Resource csv = stationsCsv();
    return new InMemoryStationService(csv.getInputStream());
  }
}
