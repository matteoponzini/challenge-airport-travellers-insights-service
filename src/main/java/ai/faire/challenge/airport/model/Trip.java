package ai.faire.challenge.airport.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Trip {
  private String uid;
  private String originAirportCode;
  private String destinationAirportCode;
  private LocalDate departureDate;
  private LocalDate returnDate;

  public Trip(String uid, String originAirportCode, String destinationAirportCode, LocalDate departureDate, LocalDate returnDate) {
    this.uid = uid;
    this.originAirportCode = originAirportCode;
    this.destinationAirportCode = destinationAirportCode;
    this.departureDate = departureDate;
    this.returnDate = returnDate;
  }

  public Trip(String originAirportCode, String destinationAirportCode, LocalDate departureDate, LocalDate returnDate) {
    this(null, originAirportCode, destinationAirportCode, departureDate, returnDate);
  }
}
