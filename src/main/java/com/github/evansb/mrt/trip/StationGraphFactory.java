package com.github.evansb.mrt.trip;

import com.github.evansb.mrt.station.Station;
import com.github.evansb.mrt.station.StationService;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;

/**
 * Factory class to create instance of {@link org.jgrapht.Graph} of {@link
 * com.github.evansb.mrt.station.Station}
 */
@Slf4j
public class StationGraphFactory {
  private final StationService stationService;
  private Graph<Station, DefaultEdge> graph = null;

  public StationGraphFactory(StationService stationService) {
    this.stationService = stationService;
  }

  /**
   * Construct undirected graph from current station data. Stations are connected if <code>
   * isAdjacentTo</code> returns <code>true</code>.
   */
  public Graph<Station, DefaultEdge> getGraph() {
    return graph;
  }

  private void createGraph() {
    Graph<Station, DefaultEdge> graph = new DirectedMultigraph<>(DefaultEdge.class);
    createVertices(graph);
    createLineEdges(graph);
    createConnectingEdges(graph);
    this.graph = graph;
  }

  private void createVertices(Graph<Station, ?> graph) {
    for (Station station : stationService.getStations()) {
      graph.addVertex(station);
    }
  }

  private void createLineEdges(Graph<Station, ?> graph) {
    Map<String, List<Station>> stationsByLine =
        stationService.getStations().stream().collect(Collectors.groupingBy(Station::getLineCode));

    for (Map.Entry<String, List<Station>> entry : stationsByLine.entrySet()) {
      Collections.sort(entry.getValue());

      Station prev = entry.getValue().get(0);
      for (Station station : entry.getValue().subList(1, entry.getValue().size())) {
        graph.addEdge(prev, station);
        graph.addEdge(station, prev);
        prev = station;
      }
    }
  }

  private void createConnectingEdges(Graph<Station, ?> graph) {
    for (Map.Entry<String, List<Station>> entry :
        stationService.getConnectingStations().entrySet()) {
      for (Station source : entry.getValue()) {
        for (Station dest : entry.getValue()) {
          if (source != dest) {
            graph.addEdge(source, dest);
          }
        }
      }
    }
  }

  @PostConstruct
  public void setup() {
    Instant start = Instant.now();
    createGraph();
    log.info(
        "Finished creating connectivity graph between stations in {} ms",
        Instant.now().toEpochMilli() - start.toEpochMilli());
  }
}
