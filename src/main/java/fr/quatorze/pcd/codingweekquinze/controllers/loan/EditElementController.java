package fr.quatorze.pcd.codingweekquinze.controllers.loan;

import fr.quatorze.pcd.codingweekquinze.controllers.components.CustomDateTimePicker;
import fr.quatorze.pcd.codingweekquinze.dao.AvailabilityDAO;
import fr.quatorze.pcd.codingweekquinze.dao.ElementDAO;
import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.layout.RequiresAuth;
import fr.quatorze.pcd.codingweekquinze.layout.component.DateTimePicker;
import fr.quatorze.pcd.codingweekquinze.model.Availability;
import fr.quatorze.pcd.codingweekquinze.model.Element;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import io.github.palexdev.materialfx.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;


@RequiresAuth
public class EditElementController {
    @FXML
    private MFXListView<Availability> periodList;
    @FXML
    private MFXCheckbox serviceBox;
    @FXML
    private MFXTextField name;

    @FXML
    private TextArea description;

    @FXML
    private MFXTextField price;

    @FXML
    private MFXComboBox<String> period;

    @FXML
    private CustomDateTimePicker startDatePicker;

    @FXML
    private CustomDateTimePicker endDatePicker;

    @FXML
    private GridPane gridPane;

    @FXML
    private MFXTextField field;

    @FXML
    private io.github.palexdev.mfxcore.controls.Label photoName;

    @FXML
    private ImageView photo;

    @FXML
    private MFXButton deletePhoto;

    @FXML
    private MFXButton choosePhoto;

    private ChronoUnit chronoUnit;

    private final ObservableList<Availability> availability = FXCollections.observableArrayList();

    private final Element element;


    public EditElementController(Element element) {
        this.element = element;
    }

    @FXML
    private void initialize() {

        this.name.setText(element.getName());
        this.description.setText(element.getDescription());
        this.price.setText(String.valueOf(element.getPrice()));
        this.serviceBox.setSelected(element.getIsService());
        this.photoName.setText(element.getImage());
        if (element.getImage() != null && !element.getImage().isEmpty()) {
            this.photo.setImage(new Image(element.getImage()));
            this.photo.setFitWidth(100);
            this.photo.setPreserveRatio(true);
            this.photo.setVisible(true);
            this.deletePhoto.setVisible(true);
            this.choosePhoto.setVisible(false);
        }
        this.photo.setFitWidth(100);
        this.photo.setPreserveRatio(true);
        this.photo.setVisible(true);
        this.deletePhoto.setVisible(true);
        this.choosePhoto.setVisible(false);
        this.periodList.setItems(availability);
        this.availability.addAll(element.getAvailabilities());

        setupFilter(price);

        /*this.periodList.setCellFactory(new Callback<ListView<Availability>, ListCell<Availability>>() {
            @Override
            public ListCell<Availability> call(ListView<Availability> param) {
                return new ListCell<>() {

                    private final Button deleteButton = new Button("Supprimer");
                    private final Label label = new Label();
                    private final HBox hbox = new HBox();

                    {
                        hbox.getChildren().addAll(label, deleteButton);
                        deleteButton.setOnAction(event -> {
                            availability.remove(getItem());
                            // Supprimer le bouton
                            getListView().getItems().remove(getItem());
                            periodList.setItems(FXCollections.observableList(availability));
                        });
                        hbox.setSpacing(10);
                        HBox.setHgrow(label, javafx.scene.layout.Priority.ALWAYS);
                    }

                    @Override
                    protected void updateItem(Availability item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            label.setText(item.toString());
                            setGraphic(hbox);
                        } else {
                            label.setText("");
                            setGraphic(null);
                        }
                    }
                };
            }
        });*/
        period.setValue("Aucune");
        updateViewBasedOnPeriod("Aucune");
    }

    private void setupFilter(TextField field) {
        field.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!"0123456789".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
            }
        });
    }

    private void setupPeriod(String label, String prompt, GridPane gridPane) {
        setupFilter(field);
        field.setPromptText(prompt);
        gridPane.add(new Label(label), 1, 9);
        gridPane.add(field, 2, 9);
        gridPane.add(new Label("Date de début:"), 1, 10);
        gridPane.add(startDatePicker, 2, 10);
        gridPane.add(new Label("Date de fin:"), 1, 11);
        gridPane.add(endDatePicker, 2, 11);
    }

    private void updateViewBasedOnPeriod(String period) {
        // On supprime les éléments de la grille
        clearGridPaneRow(gridPane, 9);
        clearGridPaneRow(gridPane, 10);
        clearGridPaneRow(gridPane, 11);
        field = new MFXTextField();
        startDatePicker = new CustomDateTimePicker();
        endDatePicker = new CustomDateTimePicker();
        switch (period) {
            case "Semaine":
                setupPeriod("Nombre de semaines:", "Nombre de semaines", gridPane);
                chronoUnit = ChronoUnit.WEEKS;
                break;
            case "Mois":
                setupPeriod("Nombre de mois:", "Nombre de mois", gridPane);
                chronoUnit = ChronoUnit.MONTHS;
                break;
            case "Année":
                setupPeriod("Nombre d'années:", "Nombre d'années", gridPane);
                chronoUnit = ChronoUnit.YEARS;
                break;
            default:
                chronoUnit = ChronoUnit.DAYS;
                // Configurez le datePicker comme nécessaire
                gridPane.add(new Label("Date de début:"), 1, 9);
                gridPane.add(startDatePicker, 2, 9);
                gridPane.add(new Label("Date de fin:"), 1, 10);
                gridPane.add(endDatePicker, 2, 10);
                break;
        }
    }

    @FXML
    private void handlePeriodChange() {
        String selectedPeriod = period.getValue();
        updateViewBasedOnPeriod(selectedPeriod);
    }

    @FXML
    private void handleAddPeriod() {
        String value = field.getText();
        if (!Objects.equals(period.getValue(), "Aucune") && value.isEmpty()) {
            LayoutManager.alert("Veuillez entrer une valeur");
            return;
        }
        int period = value.isEmpty() ? 0 : Integer.parseInt(value);
        if (period < 0) {
            LayoutManager.alert("Veuillez entrer une valeur positive");
            return;
        }
        LocalDateTime startDate = this.startDatePicker.getValue();
        LocalDateTime endDate = this.endDatePicker.getValue();
        if (startDate == null || endDate == null) {
            LayoutManager.alert("Veuillez entrer une date de début et une date de fin");
            return;
        }
        if (startDate.isAfter(endDate)) {
            LayoutManager.alert("La date de début doit être avant la date de fin");
            return;
        }
        if (startDate.isBefore(LocalDateTime.now())) {
            LayoutManager.alert("La date de début doit être après aujourd'hui");
            return;
        }

        // Vérifier que les dates sont cohérentes en fonction de la période choisie, par exemple si la période est récurrente en semaine, les dates de début et de fin doivent appartenir à la même semaine
        if (chronoUnit == ChronoUnit.WEEKS) {
            if (ChronoUnit.WEEKS.between(startDate, endDate) != 0) {
                LayoutManager.alert("La date de fin doit être dans la même semaine que la date de début");
                return;
            }
        } else if (chronoUnit == ChronoUnit.MONTHS) {
            if (startDate.getMonth() != endDate.getMonth()) {
                LayoutManager.alert("La date de début doit être dans le même mois que la date de fin");
                return;
            }
        } else if (chronoUnit == ChronoUnit.YEARS) {
            if (startDate.getYear() != endDate.getYear()) {
                LayoutManager.alert("La date de début doit être dans la même année que la date de fin");
                return;
            }
        }

        // On check si les périodes se chevauchent
        for (Availability availability : availability) {
            if (availability.isOverlapPeriod(startDate, endDate, chronoUnit, period)) {
                LayoutManager.alert("La période chevauche une période existante");
                return;
            }
        }

        // Ajouter la période
        availability.add(new Availability(this.element, startDate, endDate, chronoUnit, period));
        // On affiche dans la console celui qu'on vient d'ajouter
        System.out.println(availability.get(availability.size() - 1));
        // Mettre à jour la vue
        updateViewBasedOnPeriod(this.period.getValue());
    }

    private void clearGridPaneRow(GridPane grid, int rowIndex) {
        Set<Node> nodesToRemove = new HashSet<>();
        for (Node child : grid.getChildren()) {
            // Vérifiez si le nœud est dans la ligne spécifiée
            if (GridPane.getRowIndex(child) != null && GridPane.getRowIndex(child) == rowIndex) {
                nodesToRemove.add(child);
            }
        }
        // Supprimer les nœuds trouvés
        grid.getChildren().removeAll(nodesToRemove);
    }

    @FXML
    private void submit() {

        User user = AuthService.getInstance().getCurrentUser();

        if (name.getText().isEmpty()) {
            LayoutManager.alert("Veuillez entrer un nom");
            return;
        }

        if (price.getText().isEmpty()) {
            LayoutManager.alert("Veuillez entrer un prix");
            return;
        }

        if (description.getText().isEmpty()) {
            LayoutManager.alert("Veuillez entrer une description");
            return;
        }

        if (availability.isEmpty()) {
            LayoutManager.alert("Veuillez entrer au moins une période");
            return;
        }

        user.removeMyElementsCalendar(element);

        // On vérifie si il y a des périodes qui ont été supprimées
        // Si oui, on les supprime de la base de données
        List<Availability> availabilities = this.element.getAvailabilities();
        for (Availability availability : availabilities) {
            if (!this.availability.contains(availability)) {
                AvailabilityDAO.getInstance().deleteAvailability(availability);
            }
        }

        this.element.setName(name.getText());
        this.element.setDescription(description.getText());
        this.element.setPrice(Integer.parseInt(price.getText()));
        this.element.setIsService(serviceBox.isSelected());
        this.element.setImage(photoName.getText());
        this.element.setAvailabilities(availability);

        for (Availability availability : availability) {
            if (availability.getId() == null) {
                availability.setElement(element);
                AvailabilityDAO.getInstance().createAvailability(availability);
            } else {
                AvailabilityDAO.getInstance().updateAvailability(availability);
            }
        }

        ElementDAO.getInstance().updateElement(element);
        ElementDAO.getInstance().refresh(element);

        user.addMyElementsCalendar(element);

        LayoutManager.success("Élément modifié");
        LayoutManager.setLayout("loan/index.fxml", "My Elements");
    }

    @FXML
    private void handleChoosePhoto() {

        // On choisit une photo avec un filechoose
        // On récupère le chemin de la photo

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

        String path = fileChooser.showOpenDialog(null).toURI().toString();

        // On affiche la photo dans la vue

        photoName.setText(path);
        photo.setImage(new Image(path));
        photo.setFitWidth(100);
        photo.setPreserveRatio(true);
        photo.setVisible(true);
        deletePhoto.setVisible(true);
        choosePhoto.setVisible(false);
    }

    @FXML
    private void handleDeletePhoto() {
        photoName.setText("");
        photo.setImage(null);
        photo.setVisible(false);
        deletePhoto.setVisible(false);
        choosePhoto.setVisible(true);
    }

}
