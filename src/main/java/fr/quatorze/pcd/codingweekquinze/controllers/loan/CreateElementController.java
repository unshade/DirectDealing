package fr.quatorze.pcd.codingweekquinze.controllers.loan;

import fr.quatorze.pcd.codingweekquinze.dao.ElementDAO;
import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.layout.RequiresAuth;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


@RequiresAuth
public class CreateElementController {
    @FXML
    private TextField name;

    @FXML
    private TextArea description;

    @FXML
    private TextField price;

    @FXML
    private void submit() {
        User user = AuthService.getInstance().getCurrentUser();
        ElementDAO.getInstance().createElement(name.getText(), Integer.parseInt(price.getText()), description.getText(), user);
        LayoutManager.success("Element created");
        LayoutManager.setLayout("loan/index.fxml", "My Elements");
    }
}
