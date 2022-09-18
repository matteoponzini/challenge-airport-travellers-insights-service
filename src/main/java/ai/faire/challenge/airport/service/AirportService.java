package ai.faire.challenge.airport.service;

import ai.faire.challenge.airport.model.Insights;
import ai.faire.challenge.airport.model.Trip;
import ai.faire.challenge.airport.repository.TripRetrieve;
import ai.faire.challenge.airport.retrieve.RetrieveFromRemote;
import com.amadeus.resources.Prediction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class AirportService {

  public static final String BUSINESS_TRIP = "BUSINESS";
  public static final String LEISURE_TRIP = "LEISURE";
  private final TripRetrieve tripRetrieve;
  private final RetrieveFromRemote<Prediction, Trip> retrieve;

  public AirportService(TripRetrieve tripRetrieve, RetrieveFromRemote<Prediction, Trip> retrieve) {
    this.tripRetrieve = tripRetrieve;
    this.retrieve = retrieve;
  }

  public Insights insights(String airportCode, LocalDate date) {
    if (!StringUtils.hasText(airportCode)) {
      throw new IllegalArgumentException("Airport code must not be null");
    }

    if (date == null) {
      throw new IllegalArgumentException("Date must not be null");
    }

    var leisureNumber = new AtomicInteger();
    var businessNumber = new AtomicInteger();
    var leisureTotalProbability = new AtomicReference<>(0.0);
    var businessTotalProbability = new AtomicReference<>(0.0);

    tripRetrieve.getAll().stream().filter(trip -> thereIsAnyOneInTheAirportThisDay(airportCode, date, trip))
      .forEach(trip -> retrieve.call(trip).ifPresent(prediction -> {
        if (BUSINESS_TRIP.equals(prediction.getResult())) {
          insightCounter(businessNumber, businessTotalProbability, prediction);
        } else if (LEISURE_TRIP.equals(prediction.getResult())) {
          insightCounter(leisureNumber, leisureTotalProbability, prediction);
        }
      }));

    var averageLeisureProbability = getAverageProbability(leisureNumber.get(), leisureTotalProbability.get());
    var averageBusinessProbability = getAverageProbability(businessNumber.get(), businessTotalProbability.get());
    var totalTravellers = leisureNumber.get() + businessNumber.get();

    return new Insights(
      airportCode,
      date,
      totalTravellers,
      leisureNumber.get(),
      averageLeisureProbability,
      businessNumber.get(),
      averageBusinessProbability
    );
  }

  private boolean thereIsAnyOneInTheAirportThisDay(String airportCode, LocalDate date, Trip obj) {
    var thereIsOneDateEquals = obj.getDepartureDate().equals(date) || obj.getReturnDate().equals(date);
    var thereIsOneAirportCodeEquals = obj.getDestinationAirportCode().equals(airportCode)
      || obj.getOriginAirportCode().equals(airportCode);
    return thereIsOneDateEquals && thereIsOneAirportCodeEquals;
  }

  private void insightCounter(AtomicInteger businessNumber,
                              AtomicReference<Double> businessTotalProbability,
                              Prediction prediction) {
    businessNumber.incrementAndGet();
    businessTotalProbability.updateAndGet(v -> v + Double.parseDouble(prediction.getProbability()));
  }

  private double getAverageProbability(Integer businessNumber, Double businessTotalProbability) {
    return (businessTotalProbability == 0) ? 0.0 : businessTotalProbability / businessNumber;
  }

}
