package fr.quatorze.pcd.codingweekquinze.model;

import fr.quatorze.pcd.codingweekquinze.dao.ElementDAO;
import fr.quatorze.pcd.codingweekquinze.dao.LoanDAO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private LocalDate fromDate;
    private LocalDate toDate;
    private ChronoUnit chronoUnit;
    private Integer period;

    @OneToMany(mappedBy = "item")
    private List<Loan> loans;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    public Element(String name, Integer price, String description, User owner, LocalDate from, LocalDate to) {
        this(name, price, description, owner, from, to, ChronoUnit.DAYS, 0);
    }

    public Element(String name, Integer price, String description, User owner, LocalDate from, LocalDate to, ChronoUnit chronoUnit, int period) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.loans = new ArrayList<>();
        this.owner = owner;
        this.fromDate = from;
        this.toDate = to;
        this.chronoUnit = chronoUnit;
        this.period = period;
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
        System.out.println("check : " + startDate + " " + endDate);

        if (period > 0) {
            // We first get the nearest from date
            LocalDate fromDate = this.fromDate;
            while (fromDate.isBefore(LocalDate.now())) {
                fromDate = fromDate.plus(period, chronoUnit);
            }
            fromDate = fromDate.minus(period, chronoUnit);

            // We now get the nearest to date
            long duration = ChronoUnit.DAYS.between(this.fromDate, this.toDate);
            LocalDate toDate = fromDate.plus(duration, chronoUnit);

            if (startDate.before(java.sql.Date.valueOf(fromDate)) || endDate.after(java.sql.Date.valueOf(toDate))) {
                return false;
            }
        } else {
            // The two dates must be between the from and to dates
            if (this.fromDate != null && this.toDate != null) {
                if (startDate.before(java.sql.Date.valueOf(this.fromDate)) || endDate.after(java.sql.Date.valueOf(this.toDate))) {
                    return false;
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
}
