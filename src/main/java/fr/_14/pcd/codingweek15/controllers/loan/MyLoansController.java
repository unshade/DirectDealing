package fr._14.pcd.codingweek15.controllers.loan;

import fr._14.pcd.codingweek15.dao.LoanDAO;
import fr._14.pcd.codingweek15.layout.LayoutManager;
import fr._14.pcd.codingweek15.model.Loan;
import fr._14.pcd.codingweek15.service.AuthService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.util.List;

public class MyLoansController {

    @FXML
    private ListView<Loan> loans;

    @FXML
    private void initialize() {

        // On cherche les éléments qui sont possédés par l'utilisateur connecté
        List<Loan> loans = LoanDAO.getInstance().getLoansByUserAndBorrowing(AuthService.getInstance().getCurrentUser());
        this.loans.setItems(FXCollections.observableList(loans));
        this.loans.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Loan> call(ListView<Loan> listView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Loan loan, boolean empty) {
                        super.updateItem(loan, empty);
                        if (empty || loan == null) {
                            setText(null);
                            setOnMouseClicked(null);
                        } else {
                            String content = "ID: " + loan.getId() + ", Title: " + loan.getItem().getName() +
                                    ", Price: " + loan.getItem().getPrice() + ", Description: " + loan.getItem().getDescription() + ", Borrower: " + loan.getBorrower().getFirstName() + " " + loan.getBorrower().getLastName();
                            setText(content);
                            setOnMouseClicked(event -> {
                                if (event.getClickCount() == 2 && (!isEmpty())) {
                                    LayoutManager.setLayout("message.fxml", "messages", AuthService.getInstance().getCurrentUser(), loan.getBorrower(), loan);
                                }
                            });
                        }
                    }
                };
            }
        });
    }
}
