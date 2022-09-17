package ai.faire.challenge.airport.repository;

import ai.faire.challenge.airport.model.Trip;

import java.util.List;

public interface TripRepository {

  Trip saveOrUpdate(Trip trip);

  Trip remove(Trip trip);

  Trip remove(String uid);

  List<Trip> getAll();

  Trip search(String uid);

  Trip search(Trip trip);

}
