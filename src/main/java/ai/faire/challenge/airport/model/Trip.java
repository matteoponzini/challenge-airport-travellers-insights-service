package ai.faire.challenge.airport.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Trip {
  private String originAirportCode;
  private String destinationAirportCode;
  private LocalDate departureDate;
  private LocalDate returnDate;

  public Trip(String originAirportCode, String destinationAirportCode, LocalDate departureDate, LocalDate returnDate) {
    this.originAirportCode = originAirportCode;
    this.destinationAirportCode = destinationAirportCode;
    this.departureDate = departureDate;
    this.returnDate = returnDate;
  }
}
