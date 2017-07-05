package view;

import com.jfoenix.controls.JFXListCell;
import elements.Chapter;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * Created by Antoine on 18/01/2017.
 */
public class ChapterListCell extends JFXListCell<Chapter> {

    @Override
    public void updateItem(Chapter item, boolean empty) {
        super.updateItem(item, empty);
        BorderPane infoDisplay = new BorderPane();
        Label number = new Label();
        Label releaseDate = new Label();

        infoDisplay.setLeft(number);
        infoDisplay.setRight(releaseDate);

        if (item != null) {
            if (item.address != null) {
                String finalNumber;
                if (item.number == (long) item.number)
                    finalNumber = String.format("%d", (long) item.number);
                else
                    finalNumber = String.format("%s", item.number);

                number.setText("Chapter " + finalNumber);
                releaseDate.setText(item.releaseDate);
                setGraphic(infoDisplay);
            }
        }
    }


}
