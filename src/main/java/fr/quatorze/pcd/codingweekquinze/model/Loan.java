package fr.quatorze.pcd.codingweekquinze.model;

import fr.quatorze.pcd.codingweekquinze.dao.LoanDAO;
import fr.quatorze.pcd.codingweekquinze.enums.LoanStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Entity
@Setter
@Getter
@NoArgsConstructor
public final class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime startDate;
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Element item;

    @ManyToOne
    @JoinColumn(name = "borrower_id")
    private User borrower;

    @OneToMany(mappedBy = "loan")
    @ColumnDefault("[]")
    private List<Message> messages;

    private Integer status = LoanStatus.PENDING.ordinal();
    private Integer rating;

    public Loan(LocalDateTime startDate, LocalDateTime endDate, Element item, User borrower) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.item = item;
        this.borrower = borrower;
        this.rating = null;
    }

    public void awaitRating() {
        LoanDAO.getInstance().awaitRating(this);
    }

    public void end() {
        LoanDAO.getInstance().endLoan(this);
    }

    public void accept() {
        LoanDAO.getInstance().accept(this);
    }

    public void cancel() {
        LoanDAO.getInstance().cancel(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return Objects.equals(id, loan.id) && Objects.equals(startDate.truncatedTo(java.time.temporal.ChronoUnit.SECONDS), loan.startDate.truncatedTo(java.time.temporal.ChronoUnit.SECONDS)) && Objects.equals(endDate.truncatedTo(java.time.temporal.ChronoUnit.SECONDS), loan.endDate.truncatedTo(java.time.temporal.ChronoUnit.SECONDS)) && Objects.equals(item, loan.item) && Objects.equals(borrower, loan.borrower) && Objects.equals(rating, loan.rating);
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", borrower=" + borrower +
                ", itemOwner=" + item.getOwner() +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, endDate, item, borrower, rating);
    }

    public boolean isOverlapping(LocalDateTime startDate, LocalDateTime endDate) {
        if (this.startDate.equals(startDate) || this.endDate.equals(endDate)) {
            System.out.println("equals");
            return true;
        }
        if (this.startDate.isBefore(startDate) && this.endDate.isAfter(startDate)) {
            return true;
        }
        if (this.startDate.isBefore(endDate) && this.endDate.isAfter(endDate)) {
            return true;
        }
        return this.startDate.isAfter(startDate) && this.endDate.isBefore(endDate);
    }
}
