package fr.quatorze.pcd.codingweekquinze.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Entity
@Setter
@Getter
@NoArgsConstructor
public final class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;

    @CreationTimestamp
    private LocalDateTime date;

    @ManyToOne(targetEntity = Loan.class)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    public Message(User sender, User receiver, Loan loan, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.loan = loan;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        System.out.println("Message : " + message.date.truncatedTo(java.time.temporal.ChronoUnit.SECONDS));
        System.out.println("This : " + this.date.truncatedTo(java.time.temporal.ChronoUnit.SECONDS));
        return Objects.equals(id, message.id) && Objects.equals(content, message.content) && Objects.equals(date.truncatedTo(java.time.temporal.ChronoUnit.SECONDS), message.date.truncatedTo(java.time.temporal.ChronoUnit.SECONDS)) && Objects.equals(loan, message.loan) && Objects.equals(sender, message.sender) && Objects.equals(receiver, message.receiver);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, date, loan, sender, receiver);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", loan=" + loan +
                ", sender=" + sender +
                ", receiver=" + receiver +
                '}';
    }
}
