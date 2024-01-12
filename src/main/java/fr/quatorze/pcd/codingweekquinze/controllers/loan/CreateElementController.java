package fr.quatorze.pcd.codingweekquinze.controllers.loan;

import fr.quatorze.pcd.codingweekquinze.dao.AvailabilityDAO;
import fr.quatorze.pcd.codingweekquinze.dao.ElementDAO;
import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.layout.RequiresAuth;
import fr.quatorze.pcd.codingweekquinze.model.Availability;
import fr.quatorze.pcd.codingweekquinze.model.Element;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.collections.FXCollections;
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
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;


@RequiresAuth
public class CreateElementController {
    @FXML
    private MFXListView<Availability> periodList;
    @FXML
    private CheckBox serviceBox;
    @FXML
    private MFXTextField name;

    @FXML
    private TextArea description;

    @FXML
    private MFXTextField price;

    @FXML
    private ChoiceBox<String> period;

    @FXML
    private MFXDatePicker startDatePicker;

    @FXML
    private MFXDatePicker endDatePicker;

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

    private final List<Availability> availability = new ArrayList<>();

    @FXML
    private void initialize() {

        setupFilter(price);

        StringConverter<Availability> converter = FunctionalStringConverter.to(availability -> (availability == null) ? "" : availability.toString());

        this.periodList.setConverter(converter);

        this.periodList.setCellFactory(availability -> new MFXListCell<>(this.periodList, availability) {
            @Override
            protected void render(Availability item) {
                super.render(item);
                System.out.println(item.getFromDate());
            }
        });

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
        gridPane.add(new Label(label), 0, 9);
        gridPane.add(field, 1, 9);
        gridPane.add(new Label("Date de début:"), 0, 10);
        gridPane.add(startDatePicker, 1, 10);
        gridPane.add(new Label("Date de fin:"), 0, 11);
        gridPane.add(endDatePicker, 1, 11);
    }

    private void updateViewBasedOnPeriod(String period) {
        // On supprime les éléments de la grille
        clearGridPaneRow(gridPane, 9);
        clearGridPaneRow(gridPane, 10);
        clearGridPaneRow(gridPane, 11);
        field = new MFXTextField();
        startDatePicker = new MFXDatePicker();
        endDatePicker = new MFXDatePicker();
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
                gridPane.add(new Label("Date de début:"), 0, 9);
                gridPane.add(startDatePicker, 1, 9);
                gridPane.add(new Label("Date de fin:"), 0, 10);
                gridPane.add(endDatePicker, 1, 10);
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
        LocalDate startDate = this.startDatePicker.getValue();
        LocalDate endDate = this.endDatePicker.getValue();
        if (startDate == null || endDate == null) {
            LayoutManager.alert("Veuillez entrer une date de début et une date de fin");
            return;
        }
        if (startDate.isAfter(endDate)) {
            LayoutManager.alert("La date de début doit être avant la date de fin");
            return;
        }
        if (startDate.isBefore(LocalDate.now())) {
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

        // Ajouter la période
        availability.add(new Availability(null, startDate, endDate, chronoUnit, period));
        // Mettre à jour la vue
        this.periodList.setItems(FXCollections.observableList(availability));
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

        Element element = ElementDAO.getInstance().createElement(name.getText(), Integer.parseInt(price.getText()), description.getText(), user, serviceBox.isSelected(), photoName.getText());
        for (Availability availability : availability) {
            availability.setElement(element);
            AvailabilityDAO.getInstance().createAvailability(availability);
        }

        LayoutManager.success("Element created");
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
