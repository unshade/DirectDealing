package fr.quatorze.pcd.codingweekquinze.model.calendar;

import com.calendarfx.model.Entry;
import fr.quatorze.pcd.codingweekquinze.model.Loan;
import lombok.Getter;

import java.time.ZoneId;

public final class LoanEntry extends Entry<Loan> {

    public LoanEntry(Loan loan) {
        setTitle(loan.getItem().getName());
        setInterval(loan.getStartDate(), loan.getEndDate());
    }

    public Loan getLoan() {
        return getUserObject();
    }
}
