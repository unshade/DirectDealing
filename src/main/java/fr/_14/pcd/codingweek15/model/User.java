package fr._14.pcd.codingweek15.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.List;

@Entity
@Setter
@Getter
public final class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private int flow;

    private boolean sleeping;
    private boolean admin;

    @OneToMany(mappedBy = "lender")
    private List<Loan> lentLoans;

    @OneToMany(mappedBy = "borrower")
    private List<Loan> borrowedLoans;
}
