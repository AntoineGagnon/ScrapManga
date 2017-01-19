package view;

import elements.Chapter;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

/**
 * Created by Antoine on 18/01/2017.
 */
public class ChapterListCell extends ListCell<Chapter> {

    @Override
    protected void updateItem(Chapter item, boolean empty) {
        super.updateItem(item, empty);
        Label number = new Label();

        if (item != null) {
            if (item.address != null) {
                number.setText("Chapter " + String.valueOf(item.number));
                setGraphic(number);
            }
        }
    }


}
