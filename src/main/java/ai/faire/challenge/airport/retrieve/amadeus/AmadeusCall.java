package ai.faire.challenge.airport.retrieve.amadeus;

import ai.faire.challenge.airport.model.Trip;
import ai.faire.challenge.airport.retrieve.RetrieveFromRemote;
import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.resources.Prediction;
import com.amadeus.travel.predictions.TripPurpose;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AmadeusCall implements RetrieveFromRemote<Prediction, Trip> {
  private final Amadeus amadeusClient;

  public AmadeusCall(@Value("${amadeus.api-key}") String apiKey, @Value("${amadeus.api-secret}") String apiSecret) {
    amadeusClient = Amadeus
      .builder(apiKey, apiSecret)
      .build();
  }

  @Override
  @SneakyThrows
  public Prediction call(Trip trip) {
    TripPurpose tripPurpose = amadeusClient.travel.predictions.tripPurpose;
    Params params = Params.with("originLocationCode", trip.getOriginAirportCode())
      .and("destinationLocationCode", trip.getDestinationAirportCode())
      .and("departureDate", trip.getDepartureDate())
      .and("returnDate", trip.getReturnDate())
      .and("searchDate", trip.getReturnDate().minusYears(1));
    return tripPurpose.get(params);
  }

}
