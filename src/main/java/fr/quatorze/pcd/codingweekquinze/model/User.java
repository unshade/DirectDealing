package fr.quatorze.pcd.codingweekquinze.model;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import fr.quatorze.pcd.codingweekquinze.controllers.Observable;
import fr.quatorze.pcd.codingweekquinze.controllers.Observer;
import fr.quatorze.pcd.codingweekquinze.dao.NotificationDAO;
import fr.quatorze.pcd.codingweekquinze.dao.UserDAO;
import fr.quatorze.pcd.codingweekquinze.model.calendar.ElementEntry;
import fr.quatorze.pcd.codingweekquinze.model.calendar.LoanEntry;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Setter
@Getter
@NoArgsConstructor
public final class User implements Observable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Transient
    private List<Observer> observers = new ArrayList<>();

    private int flow = 200;

    private boolean sleeping;
    private boolean admin;

    @OneToMany(mappedBy = "owner")
    private List<Element> ownedElements = new ArrayList<>();

    @OneToMany(mappedBy = "borrower")
    private List<Loan> borrowedLoans = new ArrayList<>();

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

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public List<Notification> getNotifications() {
        return NotificationDAO.getInstance().getNotifications(this);
    }

    public void pay(User receiver, int amount) throws IllegalArgumentException {
        if (amount > flow) {
            throw new IllegalArgumentException("Not enough money");
        }
        UserDAO.getInstance().transferFunds(this, receiver, amount);
        this.notifyObservers();
        receiver.notifyObservers();
    }

    public void addLoansCalendar(Loan loan) {
        this.loansCalendar.addEntry(new LoanEntry(loan));
    }

    public void addMyElementsCalendar(Element element) {
        for (Availability availability : element.getAvailabilities()) {
            this.myElementsCalendar.addEntry(new ElementEntry(element, availability));
        }
    }

//    public void removeLoansCalendar(Loan loan) {
//        List<Entry<?>> entriesToRemove = new ArrayList<>();
//        for (Entry<?> entry : this.loansCalendar.findEntries("")) {
//            if (entry instanceof LoanEntry loanEntry) {
//                if (loanEntry.getLoan().equals(loan)) {
//                    entriesToRemove.add(loanEntry);
//                }
//            }
//        }
//        for (Entry<?> entry : entriesToRemove) {
//            this.loansCalendar.removeEntry(entry);
//        }
//    }

    public void removeMyElementsCalendar(Element element) {
        List<Entry<?>> entriesToRemove = new ArrayList<>();
        for (Entry<?> entry : this.myElementsCalendar.findEntries("")) {
            if (entry instanceof ElementEntry elementEntry) {
                if (elementEntry.getElement().equals(element)) {
                    entriesToRemove.add(elementEntry);
                }
            }
        }
        for (Entry<?> entry : entriesToRemove) {
            this.myElementsCalendar.removeEntry(entry);
        }
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

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : this.observers) {
            observer.update();
        }
    }
}
