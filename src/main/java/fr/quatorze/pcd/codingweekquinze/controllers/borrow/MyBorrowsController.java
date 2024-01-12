package fr.quatorze.pcd.codingweekquinze.controllers.borrow;

import fr.quatorze.pcd.codingweekquinze.dao.LoanDAO;
import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.layout.RequiresAuth;
import fr.quatorze.pcd.codingweekquinze.model.Loan;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableRow;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import java.util.Comparator;
import java.util.List;

@RequiresAuth
public class MyBorrowsController {
    @FXML
    private MFXTableView<Loan> borrows;

    @FXML
    private void initialize() {
//        User user = AuthService.getInstance().getCurrentUser();
//        List<Loan> loans = LoanDAO.getInstance().getAllLoansByUser(user);
//
//        this.borrows.setItems(FXCollections.observableList(loans));
//        this.borrows.setCellFactory(new Callback<>() {
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
//                            FXMLLoaderUtil.inject("components/element.fxml", new ElementComponent(loan.getItem()), this::setGraphic);
//                            setOnMouseClicked(event -> {
//                                if (event.getClickCount() == 2 && (!isEmpty())) {
//                                    LayoutManager.setLayout("borrow/borrow-view.fxml", "Borrow", loan);
//                                }
//                            });
//                        }
//                    }
//                };
//            }
//        });
        setupTable();
        borrows.autosizeColumnsOnInitialization();
    }

    private void setupTable() {
        MFXTableColumn<Loan> nameColumn = new MFXTableColumn<>("Nom", false, Comparator.comparing(loan -> loan.getItem().getName()));
        MFXTableColumn<Loan> descColumn = new MFXTableColumn<>("Description", false, Comparator.comparing(loan -> loan.getItem().getPrice()));
        MFXTableColumn<Loan> priceColumn = new MFXTableColumn<>("Prix", false, Comparator.comparing(loan -> loan.getItem().getPrice()));
        MFXTableColumn<Loan> ownerColumn = new MFXTableColumn<>("Prêteur", false, Comparator.comparing(loan -> loan.getItem().getOwner().getFullName()));
        //MFXTableColumn<Loan> ratingColumn = new MFXTableColumn<>("Note", false, Comparator.comparing(Loan::getRating));

        borrows.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            nameColumn.setPrefWidth(width * 0.3);
            descColumn.setPrefWidth(width * 0.3);
            priceColumn.setPrefWidth(width * 0.2);
            ownerColumn.setPrefWidth(width * 0.1);
            //ratingColumn.setPrefWidth(width * 0.2);
        });

        nameColumn.setRowCellFactory(person -> new MFXTableRowCell<>(loan -> loan.getItem().getName()));
        descColumn.setRowCellFactory(person -> new MFXTableRowCell<>(loan -> loan.getItem().getDescription()));
        priceColumn.setRowCellFactory(person -> new MFXTableRowCell<>(loan -> loan.getItem().getPrice()));
        //ratingColumn.setRowCellFactory(person -> new MFXTableRowCell<>(element -> element.getRating() == 0 ? "Inconnu" : element.getRating()));
        ownerColumn.setRowCellFactory(person -> new MFXTableRowCell<>(loan -> loan.getItem().getOwner().getFullName()));


        borrows.getTableColumns().addAll(nameColumn, descColumn, priceColumn, ownerColumn);


        borrows.setTableRowFactory(tableView -> {
            MFXTableRow<Loan> row = new MFXTableRow<>(borrows, tableView);

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    Loan loan = row.getData();
                    LayoutManager.setLayout("borrow/borrow-view.fxml", "Borrow", loan);

                }
            });

            row.buildCells();
            row.getCells().forEach(cell -> cell.setMouseTransparent(true));

            return row;
        });

        borrows.setFooterVisible(false);

        User user = AuthService.getInstance().getCurrentUser();
        List<Loan> elems = LoanDAO.getInstance().getAllLoansByUserNoOutdatedLoans(user);
        //List<Loan> elems = LoanDAO.getInstance().getLoansByUserAndBorrowing(AuthService.getInstance().getCurrentUser());
        //List<Loan> elems = LoanDAO.getInstance().getAllLoansByUser(AuthService.getInstance().getCurrentUser());

        this.borrows.setItems(FXCollections.observableList(elems));
    }
}
