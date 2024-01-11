package fr.quatorze.pcd.codingweekquinze.model;

import fr.quatorze.pcd.codingweekquinze.dao.ElementDAO;
import fr.quatorze.pcd.codingweekquinze.dao.LoanDAO;
import javafx.util.Pair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
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

    public void addLoan(User user, Date startDate, Date endDate) {

        Loan loan = LoanDAO.getInstance().createLoan(startDate, endDate, this, user);
        ElementDAO.getInstance().update(this);
    }

    public Integer getRating() {
        Integer rating = 0;
        for (Loan loan : this.loans) {
            rating += loan.getRating();
        }
        rating /= this.loans.size();
        return rating;
    }

    public boolean isAvailable(Date startDate, Date endDate) {
        if (owner.isSleeping()) return false;

        LocalDate startLocalDate = toLocalDate(startDate);
        LocalDate endLocalDate = toLocalDate(endDate);

        return isWithinAvailabilityPeriods(startLocalDate, endLocalDate) &&
                !isOverlappingWithLoans(startLocalDate, endLocalDate);
    }

    private boolean isWithinAvailabilityPeriods(LocalDate start, LocalDate end) {
        for (Availability availability : availabilities) {
            if (!availability.isWithinPeriod(start, end)) {
                return false;
            }
        }
        return true;
    }

    private boolean isOverlappingWithLoans(LocalDate start, LocalDate end) {
        for (Loan loan : loans) {
            if (loan.isOverlapping(java.sql.Date.valueOf(start), java.sql.Date.valueOf(end))) {
                return true;
            }
        }
        return false;
    }

    /*public boolean isAvailable(LocalDateTime startDate, LocalDateTime endDate) {
        return isAvailable(java.sql.Date.valueOf(startDate.toLocalDate()), java.sql.Date.valueOf(endDate.toLocalDate()));
    }

    public boolean isAvailable(Date startDate, Date endDate) {
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

    public List<Pair<LocalDate, LocalDate>> getAvailableDates() {
        List<Pair<LocalDate, LocalDate>> availableDates = new ArrayList<>();

        for (Availability availability : availabilities) {
            List<Pair<LocalDate, LocalDate>> availabilityPeriods = availability.getDates();
            for (Pair<LocalDate, LocalDate> period : availabilityPeriods) {
                List<Pair<LocalDate, LocalDate>> adjustedPeriods = adjustPeriodWithLoans(period);
                availableDates.addAll(adjustedPeriods);
            }
        }

        return availableDates;
    }

    private List<Pair<LocalDate, LocalDate>> adjustPeriodWithLoans(Pair<LocalDate, LocalDate> period) {
        List<Pair<LocalDate, LocalDate>> adjustedPeriods = new ArrayList<>();
        LocalDate currentStart = period.getKey();
        LocalDate currentEnd = period.getValue();

        List<Loan> overlappingLoans = getOverlappingLoans(currentStart, currentEnd);
        overlappingLoans.sort(Comparator.comparing(Loan::getStartDate));

        for (Loan loan : overlappingLoans) {
            LocalDate loanStart = toLocalDate(loan.getStartDate());
            LocalDate loanEnd = toLocalDate(loan.getEndDate());

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


    private List<Loan> getOverlappingLoans(LocalDate start, LocalDate end) {
        List<Loan> overlappingLoans = new ArrayList<>();
        for (Loan loan : loans) {
            System.out.println("loan : " + loan.getStartDate() + " " + loan.getEndDate());
            if (loan.isOverlapping(java.sql.Date.valueOf(start), java.sql.Date.valueOf(end))) {
                overlappingLoans.add(loan);
            }
        }
        return overlappingLoans;
    }

    private LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
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
