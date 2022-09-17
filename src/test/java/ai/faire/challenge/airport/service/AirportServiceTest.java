package ai.faire.challenge.airport.service;

import ai.faire.challenge.airport.model.Trip;
import ai.faire.challenge.airport.repository.TripRetrieve;
import ai.faire.challenge.airport.retrieve.amadeus.AmadeusCall;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Prediction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AirportServiceTest {


  @Autowired
  private TripService tripService;

  @Autowired
  private AirportService airportService;

  @Test
  void insights() throws ResponseException {
    List<Trip> trips = List.of(
      new Trip("JFK", "LIN", LocalDate.of(2020, 10, 10), LocalDate.of(2020, 10, 10)),
      new Trip("MXA", "NPA", LocalDate.of(2020, 10, 10), LocalDate.of(2020, 10, 10))
    );
    AirportService airportService = instanceAirportService(trips, trips.get(0), "0.0215402", "BUSINESS", "trip-purpose");
    assertEquals(1, airportService.insights("JFK", LocalDate.of(2020, 10, 10)).totalTravellers());
  }

  @Test
  void insightsA() {
    Trip tripA = new Trip("JFK", "LIN", LocalDate.of(2020, 10, 10), LocalDate.of(2020, 10, 10));

    Trip tripB = new Trip("MXA", "JFK", LocalDate.of(2020, 9, 10), LocalDate.of(2020, 10, 10));

    tripService.saveOrUpdate(tripA);
    tripService.saveOrUpdate(tripB);
    assertEquals(2, airportService.insights("JFK", LocalDate.of(2020, 10, 10)).totalTravellers());
  }

  @Test
  void insightsB() {
    Trip tripA = new Trip("JFK", "LIN", LocalDate.of(2020, 10, 10), LocalDate.of(2020, 10, 10));

    Trip tripB = new Trip("MXA", "JFK", LocalDate.of(2020, 9, 10), LocalDate.of(2020, 10, 10));

    tripService.saveOrUpdate(tripA);
    tripService.saveOrUpdate(tripB);
    assertEquals(2, airportService.insights("JFK", LocalDate.of(2020, 10, 10)).totalTravellers());
  }

  @Test
  void insightsC() {
    Trip tripA = new Trip("JFK", "LIN", LocalDate.of(2020, 10, 10), LocalDate.of(2020, 10, 10));
    Trip tripB = new Trip("MXA", "JFK", LocalDate.of(2020, 9, 10), LocalDate.of(2020, 10, 10));
    Trip tripF = new Trip("MLS", "JFK", LocalDate.of(2020, 9, 10), LocalDate.of(2020, 10, 10));

    Trip tripC = new Trip("MXA", "NYK", LocalDate.of(2020, 9, 10), LocalDate.of(2020, 10, 10));
    Trip tripD = new Trip("LIN", "RMA", LocalDate.of(2020, 9, 10), LocalDate.of(2020, 10, 10));

    Trip tripE = new Trip("JFK", "BYG", LocalDate.of(2020, 11, 10), LocalDate.of(2020, 12, 10));
    Trip tripG = new Trip("SQL", "JFK", LocalDate.of(2021, 9, 10), LocalDate.of(2021, 10, 10));

    tripService.saveOrUpdate(tripA);
    tripService.saveOrUpdate(tripB);
    tripService.saveOrUpdate(tripC);
    tripService.saveOrUpdate(tripD);
    tripService.saveOrUpdate(tripE);
    tripService.saveOrUpdate(tripF);
    tripService.saveOrUpdate(tripG);
    assertEquals(3, airportService.insights("JFK", LocalDate.of(2020, 10, 10)).totalTravellers());
  }


  private Prediction prediction(String probability, String result, String subType) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("id", new JsonPrimitive(UUID.randomUUID().toString()));
    jsonObject.add("probability", new JsonPrimitive(probability));
    jsonObject.add("result", new JsonPrimitive(result));
    jsonObject.add("subType", new JsonPrimitive(subType));
    jsonObject.add("type", new JsonPrimitive("prediction"));
    Gson gson = new GsonBuilder().create();
    return gson.fromJson(jsonObject, Prediction.class);
  }

  private AirportService instanceAirportService(List<Trip> trips, Trip queryParam, String probability, String result, String subType) {
    TripRetrieve tripRetrieve = Mockito.mock(TripRetrieve.class);
    Mockito.when(tripRetrieve.getAll()).thenReturn(trips);

    AmadeusCall retrieve = Mockito.mock(AmadeusCall.class);
    Mockito.when(retrieve
        .call(queryParam))
      .thenReturn(prediction("0.9984415", "BUSINESS", "trip-purpose"));

    return new AirportService(tripRetrieve, retrieve);
  }


}
