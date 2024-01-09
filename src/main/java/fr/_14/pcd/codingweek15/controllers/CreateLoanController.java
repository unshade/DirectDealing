package fr._14.pcd.codingweek15.controllers;

import fr._14.pcd.codingweek15.layout.LayoutManager;
import fr._14.pcd.codingweek15.model.Element;
import fr._14.pcd.codingweek15.model.User;
import fr._14.pcd.codingweek15.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.Date;

public class CreateLoanController {
    private Element element;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private Label name;

    @FXML
    private TextArea description;

    @FXML
    private Label price;

    public CreateLoanController(Element element) {
        this.element = element;
    }

    @FXML
    private void initialize() {
        this.name.setText(this.element.getName());
        this.description.setText(this.element.getDescription());
        this.price.setText(this.element.getPrice().toString());
    }

    @FXML
    private void submit() {
        Date startDate = Date.from(this.startDate.getValue().atStartOfDay().toInstant(java.time.ZoneOffset.UTC));
        Date endDate = Date.from(this.endDate.getValue().atStartOfDay().toInstant(java.time.ZoneOffset.UTC));
        this.loanElement(element, startDate, endDate);
        LayoutManager.setLayout("home.fxml", "Home");
    }

    private void loanElement(Element element, Date startDate, Date endDate) {
        User user = AuthService.getInstance().getCurrentUser();
        if (element.isAvailable(startDate, endDate)) {
            element.addLoan(startDate, endDate, user);
        } else {
            LayoutManager.alert("This element is not available for the selected dates");
        }
    }
}
