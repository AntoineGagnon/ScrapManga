package view;

import com.jfoenix.controls.JFXListCell;
import elements.Manga;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
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
        Button favorite = new Button("Add to favorite");

        favorite.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.getMangaLoader().addToFavorites(item.name, true);
            }
        });
        infoDisplay.setLeft(mangaName);
        infoDisplay.setRight(lastUpdate);
        infoDisplay.setCenter(favorite);
        if (item != null) {
            if (item.address != null) {
                mangaName.setText(item.name);
                lastUpdate.setText(item.lastRelease);
                setGraphic(infoDisplay);
            }
        }
    }


}
