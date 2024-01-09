package fr._14.pcd.codingweek15.model.element;

import javafx.util.Pair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
public final class Availability {

    private LocalDate from;
    private LocalDate to;
    private ChronoUnit chronoUnit;
    private int period;

    public Availability(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
        this.period = 0;
    }

    public Availability(LocalDate from, LocalDate to, ChronoUnit chronoUnit, int period) {
        this.from = from;
        this.to = to;
        this.chronoUnit = chronoUnit;
        this.period = period;
    }

    public Pair<LocalDate, LocalDate> getAvailability() {
        return new Pair<>(from, to);
    }
}
