package fr.quatorze.pcd.codingweekquinze.controllers.loan;

import fr.quatorze.pcd.codingweekquinze.dao.ElementDAO;
import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.layout.RequiresAuth;
import fr.quatorze.pcd.codingweekquinze.layout.component.DateTimePicker;
import fr.quatorze.pcd.codingweekquinze.model.Element;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RequiresAuth
public class MyElementsController {

    @FXML
    private MFXTableView<Element> elements;

    @FXML
    private MFXTextField searchBar;

    @FXML
    private MFXComboBox<String> rating;

    @FXML
    private MFXComboBox<String> type;




    @FXML
    private void initialize() {

        this.searchBar.textProperty().addListener((observable, oldValue, newValue) -> search());
        this.rating.valueProperty().addListener((observable, oldValue, newValue) -> search());
        this.type.valueProperty().addListener((observable, oldValue, newValue) -> search());
        this.rating.setValue("Tous");
        this.type.setValue("Tous");

//        // On cherche les éléments de l'utilisateur connecté
//        List<Element> elements = ElementDAO.getInstance().getElementsByOwner(AuthService.getInstance().getCurrentUser());
//        this.elements.setItems(FXCollections.observableList(elements));
//        this.elements.setCellFactory(new Callback<ListView<Element>, ListCell<Element>>() {
//            @Override
//            public ListCell<Element> call(ListView<Element> listView) {
//                return new ListCell<Element>() {
//                    @Override
//                    protected void updateItem(Element element, boolean empty) {
//                        super.updateItem(element, empty);
//                        if (empty || element == null) {
//                            setText(null);
//                            setOnMouseClicked(null);
//                        } else {
//                            String content = "ID: " + element.getId() + ", Title: " + element.getName() +
//                                    ", Price: " + element.getPrice() + ", Description: " + element.getDescription();
//                            setText(content);
//                            setOnMouseClicked(event -> {
//                                if (event.getClickCount() == 2 && (!isEmpty())) {
//                                    LayoutManager.setLayout("loan/edit_element.fxml", "Edit Element", element);
//                                }
//                            });
//                        }
//                    }
//                };
//            }
//        });
        setupTable();

        elements.autosizeColumnsOnInitialization();
    }

    private void setupTable() {
        MFXTableColumn<Element> nameColumn = new MFXTableColumn<>("Nom", false, Comparator.comparing(Element::getName));
        MFXTableColumn<Element> descColumn = new MFXTableColumn<>("Description", false, Comparator.comparing(Element::getDescription));
        MFXTableColumn<Element> ownerColumn = new MFXTableColumn<>("Propriétaire", false, Comparator.comparing(element -> element.getOwner().getFullName()));
        MFXTableColumn<Element> typeColumn = new MFXTableColumn<>("Type", false, Comparator.comparing(Element::getIsService));
        MFXTableColumn<Element> ratingColumn = new MFXTableColumn<>("Note", false, Comparator.comparing(Element::getRating));

        elements.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            nameColumn.setPrefWidth(width * 0.3);
            descColumn.setPrefWidth(width * 0.3);
            ownerColumn.setPrefWidth(width * 0.2);
            typeColumn.setPrefWidth(width * 0.1);
            ratingColumn.setPrefWidth(width * 0.2);
        });

        nameColumn.setRowCellFactory(person -> new MFXTableRowCell<>(Element::getName));
        descColumn.setRowCellFactory(person -> new MFXTableRowCell<>(Element::getDescription));
        ownerColumn.setRowCellFactory(person -> new MFXTableRowCell<>(element -> element.getOwner().getFullName()));
        ratingColumn.setRowCellFactory(person -> new MFXTableRowCell<>(element -> element.getRating() == 0 ? "Inconnu" : element.getRating()));
        typeColumn.setRowCellFactory(person -> new MFXTableRowCell<>(element -> element.getIsService() ? "Service" : "Objet"));


        elements.getTableColumns().addAll(nameColumn, descColumn, ownerColumn, typeColumn, ratingColumn);


        elements.setTableRowFactory(tableView -> {
            MFXTableRow<Element> row = new MFXTableRow<>(elements, tableView);

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {
                    Element element = row.getData();
                    LayoutManager.setLayout("loan/edit_element.fxml", "Editer", element);

                }
            });

            row.buildCells();
            row.getCells().forEach(cell -> cell.setMouseTransparent(true));

            return row;
        });

        elements.setFooterVisible(false);

        //List<Element> elems = ElementDAO.getInstance().getAllElementExceptUser(AuthService.getInstance().getCurrentUser());
        List<Element> elems = ElementDAO.getInstance().getElementsByOwner(AuthService.getInstance().getCurrentUser());

        this.elements.setItems(FXCollections.observableList(elems));
    }

    private void search() {
        String search = searchBar.getText().isEmpty() ? null : searchBar.getText();

        LocalDateTime start = startDate.getValue() == null
                ? null
                : startDate.getValue().atStartOfDay();

        LocalDateTime end = endDate.getValue() == null
                ? null
                : endDate.getValue().atStartOfDay();

        Integer rating = this.rating.getValue() != null && !this.rating.getValue().equals("Tous")
                ? Integer.parseInt(this.rating.getValue()) : null;

        String type = this.type.getValue() != null && !this.type.getValue().equals("Tous")
                ? this.type.getValue() : null;

        List<Element> elements = ElementDAO.getInstance().search(search, rating, type,false);
        if (start != null && end != null) {
            elements.removeIf(element -> !element.isAvailable(start, end));
        }
        this.elements.setItems(FXCollections.observableList(elements));
    }

    @FXML
    private void createElement() {
        LayoutManager.setLayout("loan/create_element.fxml", "Create Element");
    }
}
