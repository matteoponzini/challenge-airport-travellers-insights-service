package ai.faire.challenge.airport.model;

import java.time.LocalDate;

public record Insights(String airportCode, LocalDate date, Integer totalTravellers, Integer leisurePurposeTravellers,
                       Double leisurePurposeProbability, Integer businessPurposeTravellers,
                       Double businessPurposeProbability) {
}
