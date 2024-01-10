package fr.quatorze.pcd.codingweekquinze.model;

import com.calendarfx.model.Calendar;
import fr.quatorze.pcd.codingweekquinze.dao.UserDAO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

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

    private int flow = 200;

    private boolean sleeping;
    private boolean admin;

    @OneToMany(mappedBy = "owner")
    private List<Element> ownedElements;

    @OneToMany(mappedBy = "borrower")
    private List<Loan> borrowedLoans;

    @Transient
    private Calendar<?> loansCalendar;
    @Transient
    private Calendar<?> myElementsCalendar;

    public User(String firstName, String lastName, String email, String password, int flow, boolean sleeping, boolean admin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.flow = flow;
        this.sleeping = sleeping;
        this.admin = admin;
    }

    public void pay(User receiver, int amount) {
        UserDAO.getInstance().transferFunds(this, receiver, amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", flow=" + flow +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
