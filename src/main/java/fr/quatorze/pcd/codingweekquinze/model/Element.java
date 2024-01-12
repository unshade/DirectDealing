package fr.quatorze.pcd.codingweekquinze.model;

import fr.quatorze.pcd.codingweekquinze.dao.ElementDAO;
import fr.quatorze.pcd.codingweekquinze.dao.LoanDAO;
import javafx.util.Pair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public final class Element {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private String image;


    private Boolean isService;

    @OneToMany(mappedBy = "element")
    private List<Availability> availabilities = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private List<Loan> loans = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private String city;

    public Element(String name, Integer price, String description, User owner, Boolean service, String image, String city) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.owner = owner;
        this.isService = service;
        this.image = image;
        this.city = city;
    }

    public Integer getRating() {

        if (this.loans.isEmpty()) return 0;

        Integer rating = 0;
        for (Loan loan : this.loans) {
            if (loan.getRating() != null){
                rating += loan.getRating();
            }
        }
        rating /= this.loans.size();
        return rating;
    }

   /*public boolean isAvailable(LocalDateTime startDate, LocalDateTime endDate) {
        return isAvailable(startDate, endDate);
    }*/

    public boolean isAvailable(LocalDateTime startDate, LocalDateTime endDate) {
        if (owner.isSleeping()) return false;


        return isWithinAvailabilityPeriods(startDate, endDate) &&
                !isOverlappingWithLoans(startDate, endDate);
    }

    private boolean isWithinAvailabilityPeriods(LocalDateTime start, LocalDateTime end) {
        for (Availability availability : availabilities) {
            if (!availability.isWithinPeriod(start, end)) {
                return false;
            }
        }
        return true;
    }

    private boolean isOverlappingWithLoans(LocalDateTime start, LocalDateTime end) {
        for (Loan loan : loans) {
            if (loan.isOverlapping(start, end)) {
                return true;
            }
        }
        return false;
    }

    /*public boolean isAvailable(Date startDate, Date endDate) {
        System.out.println("check : " + startDate + " " + endDate);
        if (owner.isSleeping()) return false;

        for (Availability availability : availabilities) {
            ChronoUnit chronoUnit = availability.getChronoUnit();
            int period = availability.getPeriod();

            if (period > 0) {
                // We first get the nearest from date
                LocalDate fromDate = availability.fromDate;
                while (fromDate.isBefore(LocalDate.now())) {
                    fromDate = fromDate.plus(period, chronoUnit);
                }
                fromDate = fromDate.minus(period, chronoUnit);

                // We now get the nearest to date
                long duration = ChronoUnit.DAYS.between(availability.fromDate, availability.toDate);
                LocalDate toDate = fromDate.plus(duration, chronoUnit);

                if (startDate.before(java.sql.Date.valueOf(fromDate)) || endDate.after(java.sql.Date.valueOf(toDate))) {
                    return false;
                }
            } else {
                // The two dates must be between the from and to dates
                if (availability.fromDate != null && availability.toDate != null) {
                    if (startDate.before(java.sql.Date.valueOf(availability.fromDate)) || endDate.after(java.sql.Date.valueOf(availability.toDate))) {
                        return false;
                    }
                }
            }
        }

        for (Loan loan : this.loans) {
            System.out.println(loan.getStartDate() + " " + loan.getEndDate());
            if (loan.isOverlapping(startDate, endDate)) {
                return false;
            }
        }
        return true;
    }*/

    public List<Pair<LocalDateTime, LocalDateTime>> getAvailableDates() {
        List<Pair<LocalDateTime, LocalDateTime>> availableDates = new ArrayList<>();

        for (Availability availability : availabilities) {
            List<Pair<LocalDateTime, LocalDateTime>> availabilityPeriods = availability.getDates();
            for (Pair<LocalDateTime, LocalDateTime> period : availabilityPeriods) {
                List<Pair<LocalDateTime, LocalDateTime>> adjustedPeriods = adjustPeriodWithLoans(period);
                availableDates.addAll(adjustedPeriods);
            }
        }

        return availableDates;
    }

    private List<Pair<LocalDateTime, LocalDateTime>> adjustPeriodWithLoans(Pair<LocalDateTime, LocalDateTime> period) {
        List<Pair<LocalDateTime, LocalDateTime>> adjustedPeriods = new ArrayList<>();
        LocalDateTime currentStart = period.getKey();
        LocalDateTime currentEnd = period.getValue();

        List<Loan> overlappingLoans = getOverlappingLoans(currentStart, currentEnd);
        overlappingLoans.sort(Comparator.comparing(Loan::getStartDate));

        for (Loan loan : overlappingLoans) {
            LocalDateTime loanStart = loan.getStartDate();
            LocalDateTime loanEnd = loan.getEndDate();

            if (loanStart.isAfter(currentStart)) {
                adjustedPeriods.add(new Pair<>(currentStart, loanStart.minusDays(1)));
            }

            currentStart = loanEnd.plusDays(1);
        }

        if (currentStart.isBefore(currentEnd) || currentStart.isEqual(currentEnd)) {
            adjustedPeriods.add(new Pair<>(currentStart, currentEnd));
        }

        return adjustedPeriods;
    }


    private List<Loan> getOverlappingLoans(LocalDateTime start, LocalDateTime end) {
        List<Loan> overlappingLoans = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.isOverlapping(start, end)) {
                overlappingLoans.add(loan);
            }
        }
        return overlappingLoans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        return Objects.equals(id, element.id) && Objects.equals(name, element.name) && Objects.equals(price, element.price) && Objects.equals(description, element.description) && Objects.equals(owner, element.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, description, loans, owner);
    }
}
