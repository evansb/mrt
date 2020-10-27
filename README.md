# MRT Route Exercise

## Ubuntu 16.04 Instructions

- Check if Java 8 or above is installed by running `java -version`. The code has been tested using OpenJDK-Hotspot VM-Adopt OpenJDK.
- If Java is not installed, run the following:
```
$ sudo apt update && sudo apt install openjdk-8-jre -y --no-install-recommends
```
- Run the server in port 9000 by running `java -jar mrt.jar`.
- Once the message `Started MrtApplication in ... seconds (JVM running for ...)` is shown, you can start using the API.

## Assumptions

- Stations within the same line are connected. With this assumption, `stations.csv` is sufficient to build the station graph.

- Station opens at 00:00AM on its opening date.

- Schedules are defined in-code. MRT station data are defined in-memory. Both requires server restart after modification.

## API Design

Here we use station code e.g `DT1` instead of station name so the backend does not have to disambiguate exchange stations.

```
Endpoint: POST /v1/trip
Parameter
from: string - station code of the source
to: string - station code of the source
start: string (optional) - timestamp of the travel start time, in UTC. If not provided, will use current timestamp
```

Example:
```
curl -X POST "http://localhost:9000/v1/trip?from=DT1&to=DT2&start=2020-10-27T14:00:00Z"
```

Return Value

```
200 with possible route(s) in plain text if any, ordered by duration ascendingly
200 with empty content if no possible routes
400 for bad requests (station not found, station name/timestamp cannot be parsed)
```

## Functional Design

### Models
- The route is encapsulated as a data class called `Trip` which comprises multiple `Step`s.
- Each MRT station are encapsulated as `Station`. Data access layer is abstracted as `StationService` interface.
- `Schedule` is an interface used to work with time ranges. 3 schedules are implemented (`NightSchedule`, `PeakSchedule`, `DefaultSchedule`).


### Business Logic

- `TripCalculator` is the primary interface that is able to return zero or more `Trip` given the input parameters.

- We provide `ShortestDurationFirstTripCalculator` which returns at most 4 trips, ordered by
duration. It collaborates with `StationGraphFactory` to get unweighted directed graph of stations. It uses Yen k-shortest path algorithm to compute k-shortest paths between stations. Each returned path is then validated. Only valid paths are returned to user (for instance, a path can be invalid because the station is closed).

- The weight of the path is computed after running the shortest path algorithm and the result is returned to the user. This allow us to accurately compute the time across peak/off-peak hours. To achieve this, `ShortestDurationFirstTripCalculator` collaborates with `ScheduledStepCalculator` that can calculate the duration of each step by mapping `Schedule` to `StepCalculator`s.


### SOLID Principles
We are able to demonstrate the use of SOLID throughout the codebase.

- (S) For instance, `ShortestDurationFirstTripCalculator` delegates graph creation to `StationGraphFactory` and single step calculation to `StepCalculator` to reduce its responsibility.

- (O) Most of the business logic components are interfaces `TripCalculator`, `StepCalculator`, `Schedule`, etc.

- (D) Collaborators are injected in constructors and wired up in Spring `Configuration` classes.

## Technologies Used

- Java 8
- Spring Boot 2 (Spring 5)
- Lombok (boilerplate)
- JGraphT (Graph Algorithm)

## Limitations

- Not able to suggest taking exchange when the original line closes at night.
e.g User travels from MacPherson DT26 to Chinatown DT19 at night. Downtown line is closed, but technically we can suggest
starting from MacPherson CC10

- Not able to show the reason why no route was suggested (e.g all stations are closed).

## Example Route Suggestions

All of the example below are tested in [src/test/java/integration/TripControllerTest], you can refer to this file for the exact query made.

### Saving Time using Exchange

When going from Bukit Panjang to Fort Canning, it is 5 minutes faster to use NE line

[Output](src/test/resources/shortcut)

### Dynamically Update Time Across Peak/Off-Peak Hour

User started journey just before peak hour (17:30), the timing in the suggested route reflect this.

[Output](src/test/resources/crossing)

### Special Case: Exchange-Only

Dhoby Ghaut (NS) to Dhoby Ghaut (NE). This also demonstrate ability to skip route suggestion if any intermediate
station is closed en-route.

[Evening](src/test/resources/crossing)
[Night Hour](src/test/resources/night)

### Special Case: Stations Not Yet Open

Stations that are not yet open will never appear in any of the route suggestions and will always return empty route
suggestion.

### Special Case: Just Before Opening

TE8 Upper-Thomson to TE11 Stevens on 30 December 2021 just before Mount Pleasant and Stevens opened.

[Just Before Opening](src/test/resources/justbeforeopen)

### Travel Spans Until Next Day

[Next Day](src/test/resources/nextday)
