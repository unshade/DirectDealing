package fr._14.pcd.codingweek15.controllers.borrow;

import fr._14.pcd.codingweek15.dao.ElementDAO;
import fr._14.pcd.codingweek15.layout.LayoutManager;
import fr._14.pcd.codingweek15.model.Element;
import fr._14.pcd.codingweek15.service.AuthService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.util.Date;
import java.util.List;

public class SelectElementController {

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

        this.searchBar.textProperty().addListener((observable, oldValue, newValue) -> search());
        this.startDate.valueProperty().addListener((observable, oldValue, newValue) -> search());
        this.endDate.valueProperty().addListener((observable, oldValue, newValue) -> search());
        this.rating.valueProperty().addListener((observable, oldValue, newValue) -> search());

        List<Element> elements = ElementDAO.getInstance().getAllElementExceptUser(AuthService.getInstance().getCurrentUser());

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
                                    LayoutManager.setLayout("borrow/create_loan_view.fxml", "Loan", element);
                                }
                            });
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

        Date start;
        Date end;

        if (startDate.getValue() == null) {
            start = null;
        } else {
            start = Date.from(startDate.getValue().atStartOfDay().toInstant(java.time.ZoneOffset.UTC));
        }

        if (endDate.getValue() == null) {
            end = null;
        } else {
            end = Date.from(endDate.getValue().atStartOfDay().toInstant(java.time.ZoneOffset.UTC));
        }

        Integer rating = this.rating.getValue();

        if (this.rating == null) {
            rating = null;
        }

        elements = ElementDAO.getInstance().search(search, start, end, rating);


        if (elements.isEmpty()) {
            LayoutManager.alert("No results found");
        }
        this.elements.setItems(FXCollections.observableList(elements));
    }
}
