package fr.quatorze.pcd.codingweekquinze.controllers;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.time.LocalTime;

public class PlanningController {

    @FXML
    public GridPane planning;

    @FXML
    public void initialize() {
        CalendarView calendarView = new CalendarView();

        Calendar birthdays = new Calendar("Mes emprunts");
        Calendar holidays = new Calendar("Mes prêts et services");

        birthdays.setStyle(Calendar.Style.STYLE1);
        holidays.setStyle(Calendar.Style.STYLE2);

        CalendarSource myCalendarSource = new CalendarSource("Mes calendriers");
        myCalendarSource.getCalendars().addAll(birthdays, holidays);

        calendarView.getCalendarSources().addAll(myCalendarSource);

        calendarView.setRequestedTime(LocalTime.now());

        planning.getChildren().add(calendarView);
    }
}
