package fr._14.pcd.codingweek15.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
public final class Element {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Integer price;
    private String description;

    @OneToMany(mappedBy = "item")
    private List<Loan> loans;

}
