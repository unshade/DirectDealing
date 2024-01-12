package fr.quatorze.pcd.codingweekquinze.layout.component;

import fr.quatorze.pcd.codingweekquinze.service.LocationService;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;

public final class AutocompletionTextField extends MFXTextField {

    private final ContextMenu entriesPopup;

    public AutocompletionTextField() {
        super();
        this.entriesPopup = new ContextMenu();

        setListener();
    }

    private void setListener() {
        textProperty().addListener((observable, oldValue, newValue) -> {
            String enteredText = getText();
            if (enteredText == null || enteredText.isEmpty() || enteredText.isBlank()) {
                entriesPopup.hide();
            } else {
                List<String> filteredEntries = LocationService.getInstance().getCitiesStartingWith(enteredText);
                if (!filteredEntries.isEmpty()) {
                    populatePopup(filteredEntries, enteredText);
                    if (!entriesPopup.isShowing()) {
                        entriesPopup.show(AutocompletionTextField.this, Side.BOTTOM, 0, 0);
                    }
                } else {
                    entriesPopup.hide();
                }
            }
        });

        focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            entriesPopup.hide();
        });
    }

    private void populatePopup(List<String> searchResult, String searchRequest) {
        List<CustomMenuItem> menuItems = new LinkedList<>();
        int maxEntries = 10;
        int count = Math.min(searchResult.size(), maxEntries);
        for (int i = 0; i < count; i++) {
            final String result = searchResult.get(i);
            Label entryLabel = new Label();
            entryLabel.setGraphic(Styles.buildTextFlow(result, searchRequest));
            entryLabel.setPrefHeight(10);
            entryLabel.setTextFill(Color.BLACK);
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            menuItems.add(item);

            item.setOnAction(actionEvent -> {
                setText(result);
                positionCaret(result.length());
                entriesPopup.hide();
            });
        }

        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }
}