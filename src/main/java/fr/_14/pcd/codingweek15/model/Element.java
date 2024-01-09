package fr._14.pcd.codingweek15.model;

import fr._14.pcd.codingweek15.dao.ElementDAO;
import fr._14.pcd.codingweek15.dao.LoanDAO;
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

    public Element(String name, Integer price, String description, User owner) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.loans = new ArrayList<>();
        this.owner = owner;
    }

    public void addLoan(Date startDate, Date endDate, User user) {
        Loan loan = new Loan(startDate, endDate, this, user);
        this.loans.add(loan);
        LoanDAO.getInstance().create(loan);
        ElementDAO.getInstance().update(this);
    }

    public boolean isAvailable(Date startDate, Date endDate) {
        for (Loan loan : this.loans) {
            if (loan.isOverlapping(startDate, endDate)) {
                return false;
            }
        }
        return true;
    }
}
