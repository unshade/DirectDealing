package fr._14.pcd.codingweek15.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public final class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int rating;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;
}
