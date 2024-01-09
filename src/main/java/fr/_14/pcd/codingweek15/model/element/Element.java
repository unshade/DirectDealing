package fr._14.pcd.codingweek15.model.element;

import fr._14.pcd.codingweek15.dao.ElementDAO;
import fr._14.pcd.codingweek15.dao.LoanDAO;
import fr._14.pcd.codingweek15.model.Loan;
import fr._14.pcd.codingweek15.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "item")
    private List<Loan> loans;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Lob
    private List<Availability> availabilities;

    public Element(String name, Integer price, String description, User owner) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.loans = new ArrayList<>();
        this.owner = owner;
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
        for (Loan loan : this.loans) {
            System.out.println(loan.getStartDate() + " " + loan.getEndDate());
            if (loan.isOverlapping(startDate, endDate)) {
                return false;
            }
        }
        return true;
    }
}
