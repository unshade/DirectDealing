package fr._14.pcd.codingweek15.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
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
}
