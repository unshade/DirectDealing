package fr.quatorze.pcd.codingweekquinze.controllers.borrow;

import fr.quatorze.pcd.codingweekquinze.dao.ElementDAO;
import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.layout.RequiresAuth;
import fr.quatorze.pcd.codingweekquinze.layout.component.AutocompletionTextField;
import fr.quatorze.pcd.codingweekquinze.model.Element;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.utils.others.observables.When;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.time.ZoneOffset;
import java.util.*;

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

        this.rating.setValue("Sélectionnez une note");
        this.type.setValue("Sélectionnez un type");


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
        MFXTableColumn<Element> nameColumn = new MFXTableColumn<>("Titre", true, Comparator.comparing(Element::getName));
        MFXTableColumn<Element> surnameColumn = new MFXTableColumn<>("Prix", true, Comparator.comparing(Element::getPrice));
        MFXTableColumn<Element> ageColumn = new MFXTableColumn<>("Description", true, Comparator.comparing(Element::getDescription));

        nameColumn.setRowCellFactory(person -> new MFXTableRowCell<>(Element::getName));
        surnameColumn.setRowCellFactory(person -> new MFXTableRowCell<>(Element::getPrice));
        ageColumn.setRowCellFactory(person -> new MFXTableRowCell<>(Element::getDescription) {{
            setAlignment(Pos.CENTER_RIGHT);
        }});
        ageColumn.setAlignment(Pos.CENTER_RIGHT);

        elements.getTableColumns().addAll(nameColumn, surnameColumn, ageColumn);
//        elements.getFilters().addAll(
//                new StringFilter<>("Name", Element::getName),
//                new StringFilter<>("Surname", Element::getPrice),
//                new IntegerFilter<>("Age", Element::getDescription)
//        );
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

        Integer rating = this.rating.getValue() != null && !this.rating.getValue().equals("Sélectionnez une note")
                ? Integer.parseInt(this.rating.getValue()) : null;

        String type = this.type.getValue() != null && !this.type.getValue().equals("Sélectionnez un type")
                ? this.type.getValue() : null;

        List<Element> elements = ElementDAO.getInstance().search(search, rating, type,true);
        if (start != null && end != null) {
            elements.removeIf(element -> !element.isAvailable(start, end));
        }
        this.elements.setItems(FXCollections.observableList(elements));
    }

}
