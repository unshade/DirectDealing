package fr._14.pcd.codingweek15.controllers;

import fr._14.pcd.codingweek15.dao.ElementDAO;
import fr._14.pcd.codingweek15.layout.LayoutManager;
import fr._14.pcd.codingweek15.model.Element;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.util.List;

public class SelectLoanController {

    @FXML
    private ListView<Element> elements;

    @FXML
    private TextField searchBar;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private ComboBox<Integer> rating;


    @FXML
    private void initialize() {

        List<Element> elements = ElementDAO.getInstance().getAllElements();
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
                        } else {
                            String content = "ID: " + element.getId() + ", Title: " + element.getName() +
                                    ", Price: " + element.getPrice() + ", Description: " + element.getDescription();
                            setText(content);
                        }
                    }
                };
            }
        });
    }

    @FXML
    private void search() {
        String search = searchBar.getText();
        Element e;
        List<Element> elements;

        if (search.isEmpty()) {
            search = null;
        }

        e = new Element(search, null, search, null);
        elements = ElementDAO.getInstance().search(e);


        if (elements.isEmpty()) {
            LayoutManager.alert("No results found");
        }
        this.elements.setItems(FXCollections.observableList(elements));
    }
}
