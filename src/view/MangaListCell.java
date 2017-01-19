package view;

import elements.Manga;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

/**
 * Created by Antoine on 18/01/2017.
 */
public class MangaListCell extends ListCell<Manga> {

    @Override
    protected void updateItem(Manga item, boolean empty) {
        super.updateItem(item, empty);
        Label text = new Label();
        Label url = new Label();
        if (item != null) {
            if (item.address != null) {
                text.setText(item.name);
                url.setText(item.address.toString());
                setGraphic(text);
            }
        }
    }


}
