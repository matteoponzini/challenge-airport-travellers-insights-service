package ai.faire.challenge.airport.service;

import ai.faire.challenge.airport.model.Insights;
import ai.faire.challenge.airport.model.Trip;
import ai.faire.challenge.airport.repository.TripRetrieve;
import ai.faire.challenge.airport.retrieve.RetrieveFromRemote;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Prediction;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class AirportService {

  private final TripRetrieve tripRetrieve;
  private final RetrieveFromRemote<Prediction, Trip> retrieve;

  public AirportService(TripRetrieve tripRetrieve, RetrieveFromRemote<Prediction, Trip> retrieve) {
    this.tripRetrieve = tripRetrieve;
    this.retrieve = retrieve;
  }

  public Insights insights(String airportCode, LocalDate date) {
    var trips = tripRetrieve.getAll();
    var filteredList = trips
      .stream()
      .filter(obj ->
        (obj.getDepartureDate().equals(date) || obj.getReturnDate().equals(date)) && (obj.getDestinationAirportCode().equals(airportCode) || obj.getOriginAirportCode().equals(airportCode))
      ).toList();

    AtomicInteger leisureNumber = new AtomicInteger();
    AtomicInteger businessNumber = new AtomicInteger();
    AtomicReference<Double> leisureProbability = new AtomicReference<>(0.0);
    AtomicReference<Double> businessProbability = new AtomicReference<>(0.0);
    filteredList.forEach(trip -> {
      try {
        Prediction prediction = retrieve.call(trip);
        if ("BUSINESS".equals(prediction.getResult())) {
          businessNumber.incrementAndGet();
          businessProbability.updateAndGet(v -> v + Double.parseDouble(prediction.getProbability()));
        } else {
          leisureNumber.incrementAndGet();
          leisureProbability.updateAndGet(v -> v + Double.parseDouble(prediction.getProbability()));
        }
      } catch (ResponseException e) {
        throw new RuntimeException(e);
      }
    });
    return new Insights(airportCode, date, filteredList.size(), leisureNumber.get(), leisureProbability.get() / leisureNumber.get(), businessNumber.get(), businessProbability.get() / businessNumber.get());
  }

}
