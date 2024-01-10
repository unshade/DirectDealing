package fr.quatorze.pcd.codingweekquinze.model.calendar;

import com.calendarfx.model.Entry;
import fr.quatorze.pcd.codingweekquinze.model.Availability;
import fr.quatorze.pcd.codingweekquinze.model.Element;

import java.time.temporal.ChronoUnit;

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
        switch (chronoUnit) {
            case MINUTES -> elementEntry.setRecurrenceRule("RRULE:FREQ=MINUTELY");
            case HOURS -> elementEntry.setRecurrenceRule("RRULE:FREQ=HOURLY");
            case DAYS -> elementEntry.setRecurrenceRule("RRULE:FREQ=DAILY");
            case WEEKS -> elementEntry.setRecurrenceRule("RRULE:FREQ=WEEKLY");
            case MONTHS -> elementEntry.setRecurrenceRule("RRULE:FREQ=MONTHLY");
            case YEARS -> elementEntry.setRecurrenceRule("RRULE:FREQ=YEARLY");
        }
        return elementEntry;
    }
}
