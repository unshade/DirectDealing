package fr.quatorze.pcd.codingweekquinze.controllers.borrow;

import fr.quatorze.pcd.codingweekquinze.dao.LoanDAO;
import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.layout.RequiresAuth;
import fr.quatorze.pcd.codingweekquinze.model.Element;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.Date;

@RequiresAuth
public class CreateLoanController {
    private final Element element;

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
    }

    private void loanElement(Element element, Date startDate, Date endDate) {
        User user = AuthService.getInstance().getCurrentUser();

        if (element.isAvailable(startDate, endDate)) {
            LoanDAO.getInstance().createLoan(startDate, endDate, element, user);
            LayoutManager.success("Loan created");
            LayoutManager.setLayout("borrow/my-borrow.fxml", "Home");

        } else {
            LayoutManager.alert("This element is not available for the selected dates");
        }
    }
}
