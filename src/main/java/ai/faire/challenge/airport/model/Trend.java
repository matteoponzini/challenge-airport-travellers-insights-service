package ai.faire.challenge.airport.model;

import java.time.LocalDate;

public record Trend(LocalDate date,
                    Integer travellers,
                    Integer trend,
                    Integer businessTravellers,
                    Integer businessTravellersTrend,
                    Integer leisureTravellers,
                    Integer leisureTravellersTrend) {
}
