package fr.quatorze.pcd.codingweekquinze.model.calendar;

import com.calendarfx.model.Entry;
import fr.quatorze.pcd.codingweekquinze.model.Availability;
import fr.quatorze.pcd.codingweekquinze.model.Element;
import lombok.Getter;

import java.time.temporal.ChronoUnit;

@Getter
public final class ElementEntry extends Entry<Element> {

    private final Element element;
    private final Availability availability;
    private final ChronoUnit chronoUnit;
    private final Integer period;

    public ElementEntry(Element element, Availability availability) {
        setTitle(element.getName());

        setInterval(availability.getFromDate(), availability.getToDate());

        this.element = element;
        this.availability = availability;
        this.chronoUnit = availability.getChronoUnit();
        this.period = availability.getPeriod();
    }

    @Override
    public ElementEntry createRecurrence() {
        if (chronoUnit == null || period == null) {
            return this;
        }
        ElementEntry elementEntry = new ElementEntry(element, availability);
        // ReccurenceRule où FREQ est la période et INTERVAL est le nombre de périodes
        switch (chronoUnit) {
            case WEEKS:
                elementEntry.setRecurrenceRule("FREQ=WEEKLY;COUNT=" + period);
                break;
            case MONTHS:
                elementEntry.setRecurrenceRule("FREQ=MONTHLY;COUNT=" + period);
                break;
            case YEARS:
                elementEntry.setRecurrenceRule("FREQ=YEARLY;COUNT=" + period);
                break;
        }
        return elementEntry;
    }
}
