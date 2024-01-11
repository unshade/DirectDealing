package fr.quatorze.pcd.codingweekquinze.model;

import javafx.util.Pair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public final class Availability {

    LocalDateTime fromDate;
    LocalDateTime toDate;
    ChronoUnit chronoUnit;
    Integer period;
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "element_id")
    private Element element;

    public Availability(Element element, LocalDateTime fromDate, LocalDateTime toDate, ChronoUnit chronoUnit, Integer period) {
        this.element = element;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.chronoUnit = chronoUnit;
        this.period = period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Availability that = (Availability) o;
        return Objects.equals(id, that.id) && Objects.equals(element, that.element) && Objects.equals(fromDate.truncatedTo(java.time.temporal.ChronoUnit.SECONDS), that.fromDate.truncatedTo(java.time.temporal.ChronoUnit.SECONDS)) && Objects.equals(toDate.truncatedTo(java.time.temporal.ChronoUnit.SECONDS), that.toDate.truncatedTo(java.time.temporal.ChronoUnit.SECONDS)) && chronoUnit == that.chronoUnit && Objects.equals(period, that.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, element, fromDate, toDate, chronoUnit, period);
    }

    public boolean isWithinPeriod(LocalDateTime start, LocalDateTime end) {
        if (period == 0) {
            return (start.isEqual(fromDate) || start.isAfter(fromDate)) &&
                    (end.isEqual(toDate) || end.isBefore(toDate));
        }

        LocalDateTime currentStart = fromDate;
        LocalDateTime periodEnd = toDate;

        // La date de fin de la période récurrente.
        LocalDateTime recurringPeriodEnd = fromDate.plus(period, chronoUnit);

        while (currentStart.isBefore(recurringPeriodEnd)) {
            if ((start.isEqual(currentStart) || start.isAfter(currentStart)) &&
                    (end.isEqual(periodEnd) || end.isBefore(periodEnd))) {
                return true;
            }

            // Préparer pour la prochaine période récurrente
            currentStart = currentStart.plus(1, chronoUnit);
            periodEnd = periodEnd.plus(1, chronoUnit);
        }

        return false;
    }


    public boolean isOverlapPeriod(LocalDateTime start, LocalDateTime end, ChronoUnit chronoUnitPeriod, Integer recurrence) {

        // La date de fin de la période récurrente
        LocalDateTime recurringPeriodEnd = fromDate.plus(period, chronoUnit);

        // La date de fin de la période récurrente de la période donnée en paramètre
        LocalDateTime recurringPeriodEndParam = start.plus(recurrence, chronoUnitPeriod);

        while (start.isBefore(recurringPeriodEndParam) || start.isEqual(recurringPeriodEndParam)) {
            // On veut que la la période donnée en paramètre ne chevauche pas une période déjà existante
            LocalDateTime currentStart = fromDate;
            LocalDateTime periodEnd = toDate;
            while (currentStart.isBefore(recurringPeriodEnd) || currentStart.isEqual(recurringPeriodEnd)) {
                if ((start.isEqual(currentStart) || start.isAfter(currentStart)) &&
                        (end.isEqual(periodEnd) || end.isBefore(periodEnd))) {
                    return true;
                }

                if ((start.isBefore(currentStart) || start.isEqual(currentStart)) &&
                        (end.isAfter(periodEnd) || end.isEqual(periodEnd))) {
                    return true;
                }

                if ((start.isAfter(currentStart) || start.isEqual(currentStart)) &&
                        (start.isBefore(periodEnd) || start.isEqual(periodEnd))) {
                    return true;
                }

                if ((end.isAfter(currentStart) || end.isEqual(currentStart)) &&
                        (end.isBefore(periodEnd) || end.isEqual(periodEnd))) {
                    return true;
                }

                // Préparer pour la prochaine période récurrente
                currentStart = currentStart.plus(1, chronoUnit);
                periodEnd = periodEnd.plus(1, chronoUnit);
            }
            // Préparer pour la prochaine période récurrente de la période donnée en paramètre
            start = start.plus(1, chronoUnitPeriod);
            end = end.plus(1, chronoUnitPeriod);
        }
        return false;
    }


    public List<Pair<LocalDateTime, LocalDateTime>> getDates() {
        List<Pair<LocalDateTime, LocalDateTime>> dates = new ArrayList<Pair<LocalDateTime, LocalDateTime>>();
        if (period == 0) {
            dates.add(new Pair<LocalDateTime, LocalDateTime>(fromDate, toDate));
        } else {
            for(int i = 0; i <= period; i++) {
                dates.add(new Pair<LocalDateTime, LocalDateTime>(fromDate.plus(i, chronoUnit), toDate.plus(i, chronoUnit)));
            }
        }
        return dates;
    }

    @Override
    public String toString() {
        return switch (chronoUnit) {
            case WEEKS -> "Du " + fromDate + " au " + toDate + " toutes les semaines pendant " + period + " semaines";
            case MONTHS -> "Du " + fromDate + " au " + toDate + " tous les mois pendant " + period + " mois";
            case YEARS -> "Du " + fromDate + " au " + toDate + " tous les ans pendant " + period + " ans";
            default -> "Du " + fromDate + " au " + toDate;
        };
    }
}
