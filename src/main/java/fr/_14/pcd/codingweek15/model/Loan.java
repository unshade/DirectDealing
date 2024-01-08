package fr._14.pcd.codingweek15.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Arrays;
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

    @OneToMany(mappedBy = "loan")
    private List<Rating> ratings;
}
