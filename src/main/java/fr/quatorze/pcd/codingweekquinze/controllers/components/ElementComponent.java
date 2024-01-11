package fr.quatorze.pcd.codingweekquinze.controllers.components;

import fr.quatorze.pcd.codingweekquinze.model.Element;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class ElementComponent {
    private final Element element;
    @FXML
    private ImageView productImage;
    @FXML
    private Label productName;
    @FXML
    private Label productPrice;
    @FXML
    private Label productType;
    @FXML
    private Label productGeo;
    @FXML
    private Label owner;

    public ElementComponent(Element element) {
        this.element = element;
    }

    @FXML
    private void initialize() {
        //productImage.setImage(new Image(element.getImage()));
        productName.setText(element.getName());
        productPrice.setText(element.getPrice() + "€");
        productType.setText(element.getDescription());
        productGeo.setText(element.getDescription());
        owner.setText(element.getOwner().getFirstName() + " " + element.getOwner().getLastName());
    }
}
