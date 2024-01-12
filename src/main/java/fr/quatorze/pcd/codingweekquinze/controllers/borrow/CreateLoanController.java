package fr.quatorze.pcd.codingweekquinze.controllers.borrow;

import fr.quatorze.pcd.codingweekquinze.dao.LoanDAO;
import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.layout.RequiresAuth;
import fr.quatorze.pcd.codingweekquinze.layout.component.DateTimePicker;
import fr.quatorze.pcd.codingweekquinze.model.Availability;
import fr.quatorze.pcd.codingweekquinze.model.Element;
import fr.quatorze.pcd.codingweekquinze.model.Loan;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiresAuth
public class CreateLoanController {
    private final Element element;

    @FXML
    private DateTimePicker startDate;

    @FXML
    private DateTimePicker endDate;

    @FXML
    private Label name;

    @FXML
    private TextArea description;

    @FXML
    private Label price;

    @FXML
    private ListView<Pair<LocalDateTime, LocalDateTime>> reservationList;

    public CreateLoanController(Element element) {
        this.element = element;
    }

    @FXML
    private void initialize() {
        this.name.setText(this.element.getName());
        this.description.setText(this.element.getDescription());
        this.price.setText(this.element.getPrice().toString());

        this.reservationList.setItems(FXCollections.observableList(this.element.getAvailableDates()));

        this.reservationList.setCellFactory(new Callback<ListView<Pair<LocalDateTime,LocalDateTime>>, ListCell<Pair<LocalDateTime,LocalDateTime>>>() {
            @Override
            public ListCell<Pair<LocalDateTime,LocalDateTime>> call(ListView<Pair<LocalDateTime,LocalDateTime>> listView) {
                return new ListCell<Pair<LocalDateTime,LocalDateTime>>() {
                    @Override
                    protected void updateItem(Pair<LocalDateTime,LocalDateTime> period, boolean empty) {
                        super.updateItem(period, empty);
                        if (empty || period == null) {
                            setText(null);
                            setOnMouseClicked(null);
                        } else {
                            LocalDateTime start = period.getKey();
                            LocalDateTime end = period.getValue();
                            if (start.isEqual(end)) {
                                setText(start.toString());
                            } else {
                                setText(start.toString() + " - " + end.toString());
                            }
                            setOnMouseClicked(event -> {
                                startDate.setDateTimeValue(period.getKey());
                                endDate.setDateTimeValue(period.getValue());
                            });
                        }
                    }
                };

            }
        });
    }

    @FXML
    private void reserve() {
        LocalDateTime startDate = this.startDate.getDateTimeValue();
        LocalDateTime endDate = this.endDate.getDateTimeValue();

        if (startDate.isAfter(endDate)) {
            LayoutManager.alert("La date de début doit être avant la date de fin");
            return;
        }
        if (!element.isAvailable(startDate, endDate)) {
            LayoutManager.alert("La période n'est pas disponible");
            return;
        }
        User user = AuthService.getInstance().getCurrentUser();

        Loan loan = LoanDAO.getInstance().createLoan(startDate, endDate, element, user);
        user.addLoansCalendar(loan);
        LayoutManager.success("Loan created");
        LayoutManager.setLayout("borrow/my-borrows.fxml", "Home");
    }
}
