package view;

import elements.Chapter;
import elements.Manga;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import scraping.ChaptersLoader;

import java.io.IOException;

/**
 * Created by Antoine on 18/01/2017.
 */
public class ChaptersPane extends AnchorPane {
    private final ObservableList<Chapter> chapters = FXCollections.observableArrayList();
    @FXML
    public ListView<Chapter> chaptersListView;
    private TabPane mainTabPane = null;
    private Callback cellController = (Callback<ListView, ListCell>) param -> {
        ChapterListCell listCell = new ChapterListCell();
        listCell.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {

                    Chapter clicked = listCell.getItem();

                    Tab chapterReader = new Tab();
                    chapterReader.setText(clicked.manga + " " + String.valueOf(clicked.number));
                    chapterReader.setContent(new ChapterReaderPane(clicked));
                    if (mainTabPane != null) {
                        mainTabPane.getTabs().add(chapterReader);
                    }

                }
            }
        });
        return listCell;
    };

    public ChaptersPane(Manga manga, TabPane mainTabPane) {
        this.mainTabPane = mainTabPane;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "layouts/chapters_pane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        chaptersListView.setCellFactory(cellController);
        chaptersListView.setItems(chapters);

        Runnable task = () -> {
            ChaptersLoader cl = new ChaptersLoader(manga);
            chapters.addAll(cl.loadChapters());
        };
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

}
