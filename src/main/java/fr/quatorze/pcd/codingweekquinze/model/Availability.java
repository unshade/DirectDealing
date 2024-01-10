package fr.quatorze.pcd.codingweekquinze.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public final class Availability {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "element_id")
    private Element element;

    LocalDate fromDate;
    LocalDate toDate;
    ChronoUnit chronoUnit;
    Integer period;

    public Availability(Element element, LocalDate fromDate, LocalDate toDate, ChronoUnit chronoUnit, Integer period) {
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
        return Objects.equals(id, that.id) && Objects.equals(element, that.element) && Objects.equals(fromDate, that.fromDate) && Objects.equals(toDate, that.toDate) && chronoUnit == that.chronoUnit && Objects.equals(period, that.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, element, fromDate, toDate, chronoUnit, period);
    }
}
