package ai.faire.challenge.airport.repository;

import ai.faire.challenge.airport.model.Trip;

import java.util.List;

public interface TripRepository {

  Trip saveOrUpdate(Trip trip);

  boolean remove(Trip trip);

  List<Trip> getAll();

  Trip search(Trip trip);

}
