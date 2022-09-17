package ai.faire.challenge.airport.service;

import ai.faire.challenge.airport.model.Trip;
import ai.faire.challenge.airport.repository.TripRetrieve;
import org.springframework.stereotype.Service;

@Service
public class TripService {

  private final TripRetrieve tripRetrieve = new TripRetrieve();

  public Trip saveOrUpdate(Trip trip){
    return tripRetrieve.saveOrUpdate(trip);
  }


}
