package fr.quatorze.pcd.codingweekquinze.model;

import fr.quatorze.pcd.codingweekquinze.dao.ElementDAO;
import fr.quatorze.pcd.codingweekquinze.dao.LoanDAO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    public Element(String name, Integer price, String description, User owner, Boolean service, String image) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.owner = owner;
        this.isService = service;
        this.image = image;
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

    public boolean isAvailable(LocalDateTime startDate, LocalDateTime endDate) {
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
