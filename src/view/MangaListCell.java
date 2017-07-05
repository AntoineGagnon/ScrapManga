package view;

import com.jfoenix.controls.JFXListCell;
import elements.Manga;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * Created by Antoine on 18/01/2017.
 */
public class MangaListCell extends JFXListCell<Manga> {

    @Override
    public void updateItem(Manga item, boolean empty) {
        super.updateItem(item, empty);
        BorderPane infoDisplay = new BorderPane();
        Label mangaName = new Label();
        Label lastUpdate = new Label();


        infoDisplay.setLeft(mangaName);
        infoDisplay.setRight(lastUpdate);
        if (item != null) {
            if (item.address != null) {
                mangaName.setText(item.name);
                lastUpdate.setText(item.lastRelease);
                setGraphic(infoDisplay);
            }
        }
    }


}
