package fr.quatorze.pcd.codingweekquinze.controllers;

import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;
import fr.quatorze.pcd.codingweekquinze.layout.RequiresAuth;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.time.LocalTime;

@RequiresAuth
public class PlanningController {

    @FXML
    public GridPane planning;

    @FXML
    public void initialize() {
        CalendarView calendarView = new CalendarView();

        User user = AuthService.getInstance().getCurrentUser();

        CalendarSource myCalendarSource = new CalendarSource("Mes calendriers");
        myCalendarSource.getCalendars().addAll(user.getLoansCalendar(), user.getMyElementsCalendar());

        calendarView.getCalendarSources().addAll(myCalendarSource);

        calendarView.setRequestedTime(LocalTime.now());

        planning.getChildren().add(calendarView);
    }
}
