package fr.quatorze.pcd.codingweekquinze.controllers.loan;

import fr.quatorze.pcd.codingweekquinze.dao.ElementDAO;
import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.layout.RequiresAuth;
import fr.quatorze.pcd.codingweekquinze.model.Element;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiresAuth
public class MyElementsController {

    @FXML
    private ListView<Element> elements;

    @FXML
    private TextField searchBar;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private ComboBox<String> rating;

    @FXML
    private ComboBox<String> type;




    @FXML
    private void initialize() {

        this.searchBar.textProperty().addListener((observable, oldValue, newValue) -> search());
        this.startDate.valueProperty().addListener((observable, oldValue, newValue) -> search());
        this.endDate.valueProperty().addListener((observable, oldValue, newValue) -> search());
        this.rating.valueProperty().addListener((observable, oldValue, newValue) -> search());
        this.type.valueProperty().addListener((observable, oldValue, newValue) -> search());

        this.rating.setValue("Sélectionnez une note");
        this.type.setValue("Sélectionnez un type");

        // On cherche les éléments de l'utilisateur connecté
        List<Element> elements = ElementDAO.getInstance().getElementsByOwner(AuthService.getInstance().getCurrentUser());
        this.elements.setItems(FXCollections.observableList(elements));
        this.elements.setCellFactory(new Callback<ListView<Element>, ListCell<Element>>() {
            @Override
            public ListCell<Element> call(ListView<Element> listView) {
                return new ListCell<Element>() {
                    @Override
                    protected void updateItem(Element element, boolean empty) {
                        super.updateItem(element, empty);
                        if (empty || element == null) {
                            setText(null);
                            setOnMouseClicked(null);
                        } else {
                            String content = "ID: " + element.getId() + ", Title: " + element.getName() +
                                    ", Price: " + element.getPrice() + ", Description: " + element.getDescription();
                            setText(content);
                            setOnMouseClicked(event -> {
                                if (event.getClickCount() == 2 && (!isEmpty())) {
                                    //LayoutManager.setLayout("index.fxml", "Loan", element);
                                    System.out.println("Double clicked on " + element.getName());
                                }
                            });
                        }
                    }
                };
            }
        });
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

        List<Element> elements = ElementDAO.getInstance().search(search, rating, type,false);
        if (start != null && end != null) {
            elements.removeIf(element -> !element.isAvailable(start, end));
        }
        this.elements.setItems(FXCollections.observableList(elements));
    }
}
