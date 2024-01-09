package fr.quatorze.pcd.codingweekquinze.controllers.loan;

import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import javafx.fxml.FXML;

public class IndexController {

    @FXML
    private void createElement() {
        LayoutManager.setLayout("loan/create_element.fxml", "Create Element");
    }

}
