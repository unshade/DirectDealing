package fr._14.pcd.codingweek15.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Setter
@Getter
public final class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private Date startDate;
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Element item;

    @ManyToOne
    @JoinColumn(name = "borrower_id")
    private User borrower;

    @OneToMany(mappedBy = "loan")
    private List<Message> messages;

    private Integer rating;

    public Loan(Date startDate, Date endDate, Element item, User borrower) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.item = item;
        this.borrower = borrower;
        this.rating = null;
    }

}
