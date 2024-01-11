package fr.quatorze.pcd.codingweekquinze.layout.component;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public final class Styles {

    public static TextFlow buildTextFlow(String suggestion) {
        TextFlow textFlow = new TextFlow();

        Text suggestionText = new Text(suggestion);
        textFlow.getChildren().add(suggestionText);

        return textFlow;
    }

    public static TextFlow buildTextFlow(String suggestion, String searchRequest) {
        TextFlow textFlow = new TextFlow();
        int startIndex = suggestion.toLowerCase().indexOf(searchRequest.toLowerCase());
        int endIndex = startIndex + searchRequest.length();

        if (startIndex >= 0 && endIndex <= suggestion.length()) {
            Text preText = new Text(suggestion.substring(0, startIndex));
            Text highlightedText = new Text(suggestion.substring(startIndex, endIndex));
            highlightedText.setStyle("-fx-fill: blue;");
            Text postText = new Text(suggestion.substring(endIndex));

            textFlow.getChildren().addAll(preText, highlightedText, postText);
        } else {
            Text suggestionText = new Text(suggestion);
            textFlow.getChildren().add(suggestionText);
        }

        return textFlow;
    }
}
