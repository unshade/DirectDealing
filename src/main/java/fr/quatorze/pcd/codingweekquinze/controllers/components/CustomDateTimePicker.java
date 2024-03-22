package fr.quatorze.pcd.codingweekquinze.controllers.components;

import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.Getter;

import javax.swing.event.ChangeListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
public class CustomDateTimePicker extends VBox {

    private final MFXDatePicker datePicker;

    private final TextField timePicker;

    private LocalDateTime dateTime;

    public CustomDateTimePicker() {
        datePicker = new MFXDatePicker();
        timePicker = new MFXTextField();

        this.getChildren().addAll(datePicker, timePicker);
        initialize();
    }


    private void initialize() {
        this.dateTime = LocalDateTime.now();

        datePicker.setValue(dateTime.toLocalDate());

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (datePicker.getValue() != null && timePicker.getText() != null) {
                dateTime = datePicker.getValue().atTime(LocalTime.from(dateTime));
            }
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        timePicker.setText(dateTime.format(formatter));

        timePicker.textProperty().addListener((observable, oldValue, newValue) -> {
            // si le format est invalide
            if (!newValue.matches("\\d{2}:\\d{2}")) {

                this.timePicker.setStyle("-fx-text-fill: red");
            } else {
                this.timePicker.setStyle("-fx-text-fill: black");
               if (datePicker.getValue() != null && timePicker.getText() != null) {
                   dateTime = datePicker.getValue().atTime(LocalTime.parse(timePicker.getText()));
               }
            }
        });
    }

    public void reset() {
        datePicker.setValue(null);
        timePicker.setText("");
        this.dateTime = null;
    }

    public void setValue(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        datePicker.setValue(dateTime.toLocalDate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        timePicker.setText(dateTime.format(formatter));
    }

    public LocalDateTime getValue() {
        return dateTime;
    }

}
