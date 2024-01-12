package fr.quatorze.pcd.codingweekquinze.service;

import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;

import java.util.function.Function;

@Getter
public class ImageMFXTableRowCell<T> extends MFXTableRowCell<T, String> {
    private final ImageView imageView = new ImageView();

    private final Function<T, String> extractor;

    public ImageMFXTableRowCell(Function<T, String> extractor) {
        super(extractor);
        this.extractor = extractor;
    }

    @Override
    public void update(T item) {
        if (getExtractor() != null) {
            String imageUrl = getExtractor().apply(item);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                imageView.setImage(new Image(imageUrl));
                imageView.setFitHeight(80); // Ajustez la hauteur et la largeur comme vous le souhaitez
                imageView.setFitWidth(80);
                setGraphic(imageView);
            } else {
                setGraphic(null);
            }
        }
    }
}