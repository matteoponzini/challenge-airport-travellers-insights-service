package ai.faire.challenge.airport.service;

import ai.faire.challenge.airport.model.Trip;
import ai.faire.challenge.airport.repository.TripRetrieve;
import ai.faire.challenge.airport.retrieve.amadeus.AmadeusCall;
import com.amadeus.resources.Prediction;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Map;
import java.util.List;
import java.util.Collections;
import java.util.UUID;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AirportServiceTest {

  @Test
  void insightsWithOnePersonInAirportBusiness() {
    var trips = List.of(
      new Trip("JFK", "LIN",
        LocalDate.of(2020, 10, 10), LocalDate.of(2020, 10, 10)),
      new Trip("MXA", "NPA",
        LocalDate.of(2020, 10, 10), LocalDate.of(2020, 10, 10))
    );
    var airportService = instanceAirportService(
      trips,
      Map.of(trips.get(0), new PredictionParams("0.0215402", "BUSINESS", "trip-purpose"))
    );
    var insights = airportService.insights("JFK", LocalDate.of(2020, 10, 10));
    assertEquals(1, insights.totalTravellers());
    assertEquals(1, insights.businessPurposeTravellers());
    assertEquals(Double.parseDouble("0.0215402"), insights.businessPurposeProbability());
    assertEquals(0, insights.leisurePurposeTravellers());
    assertEquals(0.0, insights.leisurePurposeProbability());
  }

  @Test
  void insightsWithTwoPeopleInSameAirportButOneLeavingOtherArrivingOneBusinessOneLeisure() {
    var trips = List.of(
      new Trip("JFK", "LIN",
        LocalDate.of(2020, 10, 10), LocalDate.of(2020, 10, 10)),
      new Trip("MXA", "JFK",
        LocalDate.of(2020, 9, 10), LocalDate.of(2020, 10, 10)));

    var airportService = instanceAirportService(trips,
      Map.of(
        trips.get(0), new PredictionParams("0.0215402", "BUSINESS", "trip-purpose"),
        trips.get(1), new PredictionParams("0.0215402", "LEISURE", "trip-purpose")
      ));
    var searchDate = LocalDate.of(2020, 10, 10);
    var insights = airportService.insights("JFK", searchDate);

    assertEquals("JFK", insights.airportCode());
    assertEquals(searchDate, insights.date());
    assertEquals(2, insights.totalTravellers());
    assertEquals(1, insights.businessPurposeTravellers());
    assertEquals(Double.parseDouble("0.0215402"), insights.businessPurposeProbability());
    assertEquals(1, insights.leisurePurposeTravellers());
    assertEquals(Double.parseDouble("0.0215402"), insights.leisurePurposeProbability());
  }

  @Test
  void insightsWithTreeInSameAirportButOneBusinessTwoLeisure() {
    var trips = List.of(
      new Trip(
        "JFK",
        "LIN",
        LocalDate.of(2020, 10, 10),
        LocalDate.of(2020, 10, 10)
      ),
      new Trip(
        "MXA",
        "JFK",
        LocalDate.of(2020, 10, 10),
        LocalDate.of(2020, 10, 15)
      ),
      new Trip(
        "MLS",
        "JFK",
        LocalDate.of(2020, 9, 10),
        LocalDate.of(2020, 10, 10)
      ),
      new Trip(
        "JFK",
        "BYG",
        LocalDate.of(2020, 11, 10),
        LocalDate.of(2020, 12, 10)
      ),
      new Trip("MXA",
        "NYK",
        LocalDate.of(2020, 9, 10),
        LocalDate.of(2020, 10, 10)
      ),
      new Trip("LIN",
        "RMA",
        LocalDate.of(2020, 9, 10),
        LocalDate.of(2020, 10, 10)
      ),
      new Trip("SQL",
        "JFK",
        LocalDate.of(2021, 9, 10),
        LocalDate.of(2021, 10, 10)
      ));

    var airportService = instanceAirportService(trips,
      Map.of(
        trips.get(0), new PredictionParams("0.0215402", "BUSINESS", "trip-purpose"),
        trips.get(1), new PredictionParams("0.0215402", "LEISURE", "trip-purpose"),
        trips.get(2), new PredictionParams("0.0215402", "LEISURE", "trip-purpose"),
        trips.get(3), new PredictionParams("0.0215402", "LEISURE", "trip-purpose"),
        trips.get(4), new PredictionParams("0.0215402", "LEISURE", "trip-purpose"),
        trips.get(5), new PredictionParams("0.0215402", "BUSINESS", "trip-purpose"),
        trips.get(6), new PredictionParams("0.0215402", "BUSINESS", "trip-purpose")
      ));


    var searchDate = LocalDate.of(2020, 10, 10);
    var insights = airportService.insights("JFK", searchDate);

    assertEquals("JFK", insights.airportCode());
    assertEquals(searchDate, insights.date());

    assertEquals(3, insights.totalTravellers());
    assertEquals(1, insights.businessPurposeTravellers());
    assertEquals(Double.parseDouble("0.0215402"), insights.businessPurposeProbability());
    assertEquals(2, insights.leisurePurposeTravellers());
    assertEquals(Double.parseDouble("0.0215402"), insights.leisurePurposeProbability());
  }


  @Test
  void insightsWithNoOnePersonInAirport() {
    var trips = List.of(
      new Trip(
        "JFK",
        "LIN",
        LocalDate.of(2020, 10, 10),
        LocalDate.of(2020, 10, 10)
      ),
      new Trip(
        "MXA",
        "NPA",
        LocalDate.of(2020, 10, 10),
        LocalDate.of(2020, 10, 10)
      )
    );
    var airportService = instanceAirportService(trips,
      Map.of(
        trips.get(0),
        new PredictionParams("0.0215402", "BUSINESS", "trip-purpose"))
    );
    var insights = airportService.insights("JFK", LocalDate.of(2020, 11, 10));
    assertEquals(0, insights.totalTravellers());
    assertEquals(0, insights.businessPurposeTravellers());
    assertEquals(0.0, insights.businessPurposeProbability());
    assertEquals(0, insights.leisurePurposeTravellers());
    assertEquals(0.0, insights.leisurePurposeProbability());
  }

  @Test
  void insightsWithNoTrips() {
    var airportService = instanceAirportService(Collections.emptyList(), Collections.emptyMap());
    var insights = airportService.insights("JFK", LocalDate.of(2020, 11, 10));
    assertEquals(0, insights.totalTravellers());
    assertEquals(0, insights.businessPurposeTravellers());
    assertEquals(0.0, insights.businessPurposeProbability());
    assertEquals(0, insights.leisurePurposeTravellers());
    assertEquals(0.0, insights.leisurePurposeProbability());
  }

  @Test
  void insightsShouldFailWhenAirportIsNull() {
    var airportService = instanceAirportService(Collections.emptyList(), Collections.emptyMap());
    var error = assertThrows(IllegalArgumentException.class,
      () -> airportService.insights(null, LocalDate.of(2020, 11, 10)));
    assertEquals("Airport code must not be null", error.getMessage());
  }

  @Test
  void insightsShouldFailWhenAirportIsEmpty() {
    var airportService = instanceAirportService(Collections.emptyList(), Collections.emptyMap());
    var error = assertThrows(IllegalArgumentException.class,
      () -> airportService.insights("", LocalDate.of(2020, 11, 10)));
    assertEquals("Airport code must not be null", error.getMessage());
  }

  @Test
  void insightsShouldFailWhenDateIsNull() {
    var airportService = instanceAirportService(Collections.emptyList(), Collections.emptyMap());
    var error = assertThrows(IllegalArgumentException.class,
      () -> airportService.insights("JFK", null));
    assertEquals("Date must not be null", error.getMessage());
  }

  private Prediction prediction(PredictionParams predictionParams) {
    var jsonObject = new JsonObject();
    jsonObject.add("id", new JsonPrimitive(UUID.randomUUID().toString()));
    jsonObject.add("probability", new JsonPrimitive(predictionParams.probability()));
    jsonObject.add("result", new JsonPrimitive(predictionParams.result()));
    jsonObject.add("subType", new JsonPrimitive(predictionParams.subType()));
    jsonObject.add("type", new JsonPrimitive("prediction"));
    return new GsonBuilder().create().fromJson(jsonObject, Prediction.class);
  }

  private AirportService instanceAirportService(List<Trip> trips, Map<Trip, PredictionParams> tripPrediction) {
    var tripRetrieve = Mockito.mock(TripRetrieve.class);
    Mockito.when(tripRetrieve.getAll()).thenReturn(trips);
    var retrieve = Mockito.mock(AmadeusCall.class);
    tripPrediction.forEach((key, value) -> Mockito.when(retrieve
        .call(key))
      .thenReturn(Optional.ofNullable(prediction(value))));
    return new AirportService(tripRetrieve, retrieve);
  }

  public record PredictionParams(String probability, String result, String subType) {
  }

}
