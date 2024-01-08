package fr._14.pcd.codingweek15.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
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

    @OneToMany(mappedBy = "owner")
    private List<Element> ownedElements;

    @OneToMany(mappedBy = "borrower")
    private List<Loan> borrowedLoans;

    public User(String firstName, String lastName, String email, String password, int flow, boolean sleeping, boolean admin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.flow = flow;
        this.sleeping = sleeping;
        this.admin = admin;
    }

}
