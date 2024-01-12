package fr.quatorze.pcd.codingweekquinze.controllers.loan;

import fr.quatorze.pcd.codingweekquinze.dao.ElementDAO;
import fr.quatorze.pcd.codingweekquinze.dao.LoanDAO;
import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.layout.RequiresAuth;
import fr.quatorze.pcd.codingweekquinze.model.Element;
import fr.quatorze.pcd.codingweekquinze.model.Loan;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableRow;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.util.Comparator;
import java.util.List;

@RequiresAuth
public class MyLoansController {

    @FXML
    private MFXTableView<Loan> loans;

    @FXML
    private void initialize() {

        // On cherche les éléments qui sont possédés par l'utilisateur connecté
//        List<Loan> loans = LoanDAO.getInstance().getLoansByUserAndBorrowing(AuthService.getInstance().getCurrentUser());
//        this.loans.setItems(FXCollections.observableList(loans));
//        this.loans.setCellFactory(new Callback<>() {
//            @Override
//            public ListCell<Loan> call(ListView<Loan> listView) {
//                return new ListCell<>() {
//                    @Override
//                    protected void updateItem(Loan loan, boolean empty) {
//                        super.updateItem(loan, empty);
//                        if (empty || loan == null) {
//                            setText(null);
//                            setOnMouseClicked(null);
//                        } else {
//                            String content = "ID: " + loan.getId() + ", Title: " + loan.getItem().getName() +
//                                    ", Price: " + loan.getItem().getPrice() + ", Description: " + loan.getItem().getDescription() + ", Borrower: " + loan.getBorrower().getFirstName() + " " + loan.getBorrower().getLastName();
//                            setText(content);
//                            if (loan.getStatus() == 1) {
//                                setStyle("-fx-background-color: #00ff00");
//                            }
//                            setOnMouseClicked(event -> {
//                                if (event.getClickCount() == 2 && (!isEmpty())) {
//                                    LayoutManager.setLayout("loan/loan-view.fxml", "Loan", loan);
//                                }
//                            });
//                        }
//                    }
//                };
//            }
//        });
        setupTable();
        loans.autosizeColumnsOnInitialization();
    }

    private void setupTable() {
        MFXTableColumn<Loan> nameColumn = new MFXTableColumn<>("Nom", false, Comparator.comparing(loan -> loan.getItem().getName()));
        MFXTableColumn<Loan> descColumn = new MFXTableColumn<>("Description", false, Comparator.comparing(loan -> loan.getItem().getPrice()));
        MFXTableColumn<Loan> priceColumn = new MFXTableColumn<>("Prix", false, Comparator.comparing(loan -> loan.getItem().getPrice()));
        MFXTableColumn<Loan> borrowerColumn = new MFXTableColumn<>("Emprunteur", false, Comparator.comparing(loan -> loan.getBorrower().getFullName()));
        //MFXTableColumn<Loan> ratingColumn = new MFXTableColumn<>("Note", false, Comparator.comparing(Loan::getRating));

        loans.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            nameColumn.setPrefWidth(width * 0.3);
            descColumn.setPrefWidth(width * 0.3);
            priceColumn.setPrefWidth(width * 0.2);
            borrowerColumn.setPrefWidth(width * 0.1);
            //ratingColumn.setPrefWidth(width * 0.2);
        });

        nameColumn.setRowCellFactory(person -> new MFXTableRowCell<>(loan -> loan.getItem().getName()));
        descColumn.setRowCellFactory(person -> new MFXTableRowCell<>(loan -> loan.getItem().getDescription()));
        priceColumn.setRowCellFactory(person -> new MFXTableRowCell<>(loan -> loan.getItem().getPrice()));
        //ratingColumn.setRowCellFactory(person -> new MFXTableRowCell<>(element -> element.getRating() == 0 ? "Inconnu" : element.getRating()));
        borrowerColumn.setRowCellFactory(person -> new MFXTableRowCell<>(loan -> loan.getBorrower().getFullName()));


        loans.getTableColumns().addAll(nameColumn, descColumn, priceColumn, borrowerColumn);


        loans.setTableRowFactory(tableView -> {
            MFXTableRow<Loan> row = new MFXTableRow<>(loans, tableView);

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    Loan loan = row.getData();
                    LayoutManager.setLayout("loan/loan-view.fxml", "Loan", loan);

                }
            });

            row.buildCells();
            row.getCells().forEach(cell -> cell.setMouseTransparent(true));

            return row;
        });

        loans.setFooterVisible(false);

        List<Loan> elems = LoanDAO.getInstance().getLoansByUserAndBorrowing(AuthService.getInstance().getCurrentUser());
        //List<Loan> elems = LoanDAO.getInstance().getAllLoansByUser(AuthService.getInstance().getCurrentUser());

        this.loans.setItems(FXCollections.observableList(elems));
    }
}
