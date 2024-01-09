package fr._14.pcd.codingweek15.controllers.borrow;

import fr._14.pcd.codingweek15.dao.LoanDAO;
import fr._14.pcd.codingweek15.layout.LayoutManager;
import fr._14.pcd.codingweek15.model.Loan;
import fr._14.pcd.codingweek15.model.User;
import fr._14.pcd.codingweek15.service.AuthService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.util.List;

public class MyBorrowController {
    @FXML
    private ListView<Loan> borrows;

    @FXML
    private void initialize() {
        User user = AuthService.getInstance().getCurrentUser();
        List<Loan> loans = LoanDAO.getInstance().getAllLoansByUser(user);

        this.borrows.setItems(FXCollections.observableList(loans));
        this.borrows.setCellFactory(new Callback<>() {
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
                            String content = "ID: " + loan.getId() + ", Item: " + loan.getItem().getName() + " ,Emprunté à " + loan.getItem().getOwner().getFirstName() + " " + loan.getItem().getOwner().getLastName();
                            setText(content);
                            setOnMouseClicked(event -> {
                                if (event.getClickCount() == 2 && (!isEmpty())) {
                                    LayoutManager.setLayout("message.fxml", "messages", loan.getBorrower(), loan.getItem().getOwner(), loan);
                                }
                            });
                        }
                    }
                };
            }
        });
    }
}
