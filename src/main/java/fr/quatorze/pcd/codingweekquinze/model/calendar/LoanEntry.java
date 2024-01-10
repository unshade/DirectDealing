package fr.quatorze.pcd.codingweekquinze.model.calendar;

import com.calendarfx.model.Entry;
import fr.quatorze.pcd.codingweekquinze.model.Loan;

import java.time.ZoneId;

public final class LoanEntry extends Entry<Loan> {

    public LoanEntry(Loan loan) {
        setTitle(loan.getItem().getName());
        setInterval(loan.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), loan.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }
}
