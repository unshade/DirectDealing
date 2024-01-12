package fr.quatorze.pcd.codingweekquinze.controllers.borrow;

import fr.quatorze.pcd.codingweekquinze.controllers.components.ElementComponent;
import fr.quatorze.pcd.codingweekquinze.dao.ElementDAO;
import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.layout.RequiresAuth;
import fr.quatorze.pcd.codingweekquinze.layout.component.AutocompletionTextField;
import fr.quatorze.pcd.codingweekquinze.model.Element;
import fr.quatorze.pcd.codingweekquinze.model.Loan;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import fr.quatorze.pcd.codingweekquinze.service.ImageMFXTableRowCell;
import fr.quatorze.pcd.codingweekquinze.util.FXMLLoaderUtil;
import io.github.palexdev.materialfx.beans.BiPredicateBean;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.EnumFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.utils.others.observables.When;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.net.URL;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;

import static java.util.Comparator.nullsLast;


@RequiresAuth
public class SelectElementController {

    @FXML
    private MFXTableView<Element> elements;

    @FXML
    private MFXTextField searchBar;

    @FXML
    private MFXDatePicker startDate;

    @FXML
    private MFXDatePicker endDate;

    @FXML
    private MFXComboBox<String> rating;

    @FXML
    private MFXComboBox<String> type;
    @FXML
    private AutocompletionTextField cityBar;


    @FXML
    private void initialize() {

        this.searchBar.textProperty().addListener((observable, oldValue, newValue) -> search());
        this.startDate.valueProperty().addListener((observable, oldValue, newValue) -> search());
        this.endDate.valueProperty().addListener((observable, oldValue, newValue) -> search());
        this.rating.valueProperty().addListener((observable, oldValue, newValue) -> search());
        this.type.valueProperty().addListener((observable, oldValue, newValue) -> search());

        this.rating.setValue("Tous");
        this.type.setValue("Tous");


        setupTable();

        elements.autosizeColumnsOnInitialization();

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
//                                    LayoutManager.setLayout("borrow/create_loan_view.fxml", "Loan", element);
//                                }
//                            });
//
//                        }
//                    }
//                };
//            }
//        });
    }

    private void setupTable() {
        MFXTableColumn<Element> imageColumn = new MFXTableColumn<>("Image", false, null);
        MFXTableColumn<Element> nameColumn = new MFXTableColumn<>("Nom", false, Comparator.comparing(Element::getName));
        MFXTableColumn<Element> descColumn = new MFXTableColumn<>("Description", false, Comparator.comparing(Element::getDescription));
        MFXTableColumn<Element> ownerColumn = new MFXTableColumn<>("Propriétaire", false, Comparator.comparing(element -> element.getOwner().getFullName()));
        MFXTableColumn<Element> typeColumn = new MFXTableColumn<>("Type", false, Comparator.comparing(Element::getIsService));
        MFXTableColumn<Element> ratingColumn = new MFXTableColumn<>("Note", false, Comparator.comparing(Element::getRating));

        elements.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            imageColumn.setPrefWidth(width * 0.1);
            nameColumn.setPrefWidth(width * 0.3);
            descColumn.setPrefWidth(width * 0.3);
            ownerColumn.setPrefWidth(width * 0.2);
            typeColumn.setPrefWidth(width * 0.1);
            ratingColumn.setPrefWidth(width * 0.2);
        });
        imageColumn.setRowCellFactory(person -> {
            ImageMFXTableRowCell<Element> cell = new ImageMFXTableRowCell<>(Element::getImage);
            cell.setPrefHeight(50);
            return cell;
                });
        nameColumn.setRowCellFactory(person -> {
            MFXTableRowCell<Element, String> cell = new MFXTableRowCell<>(Element::getName);
            cell.setPrefWidth(50);
            return cell;
                });
        descColumn.setRowCellFactory(person -> {
            MFXTableRowCell<Element, String> cell = new MFXTableRowCell<>(Element::getDescription);
            cell.setPrefWidth(50);
            return cell;
                });
        ownerColumn.setRowCellFactory(person -> {
            MFXTableRowCell<Element, String> cell = new MFXTableRowCell<>(element -> element.getOwner().getFullName());
            cell.setPrefWidth(50);
            return cell;
                });
        ratingColumn.setRowCellFactory(person -> {
            MFXTableRowCell<Element, Integer> cell = new MFXTableRowCell<>(Element::getRating);
            cell.setPrefWidth(50);
            return cell;
                });
        typeColumn.setRowCellFactory(person -> {
            MFXTableRowCell<Element, Boolean> cell = new MFXTableRowCell<>(Element::getIsService);
            cell.setPrefWidth(50);
            return cell;
                });
        elements.getTableColumns().addAll(imageColumn, nameColumn, descColumn, ownerColumn, typeColumn, ratingColumn);


        elements.setTableRowFactory(tableView -> {
            MFXTableRow<Element> row = new MFXTableRow<>(elements, tableView);

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {
                    Element element = row.getData();
                    LayoutManager.setLayout("borrow/create_loan_view.fxml", "Loan", element);

                }
            });

            row.buildCells();
            row.getCells().forEach(cell -> cell.setMouseTransparent(true));

            return row;
        });

        elements.setFooterVisible(false);

        List<Element> elems = ElementDAO.getInstance().getAllElementExceptUser(AuthService.getInstance().getCurrentUser());
        this.elements.setItems(FXCollections.observableList(elems));
    }

    private void search() {
        String search = searchBar.getText().isEmpty() ? null : searchBar.getText();

        Date start = startDate.getValue() == null
                ? null
                : Date.from(startDate.getValue().atStartOfDay().toInstant(ZoneOffset.UTC));

        Date end = endDate.getValue() == null
                ? null
                : Date.from(endDate.getValue().atStartOfDay().toInstant(ZoneOffset.UTC));

        Integer rating = this.rating.getValue() != null && !this.rating.getValue().equals("Tous")
                ? Integer.parseInt(this.rating.getValue()) : null;

        String type = this.type.getValue() != null && !this.type.getValue().equals("Tous")
                ? this.type.getValue() : null;

        List<Element> elements = ElementDAO.getInstance().search(search, rating, type, true);
        if (start != null && end != null) {
            elements.removeIf(element -> !element.isAvailable(start, end));
        }
        this.elements.setItems(FXCollections.observableList(elements));
    }

}
