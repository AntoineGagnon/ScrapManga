package view;

import elements.Manga;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;

public class TabController {

    final ObservableList<Manga> mangas = FXCollections.observableArrayList();
    final ObservableList<Manga> favorites = FXCollections.observableArrayList();

    @FXML
    public ListView mangaListView;
    @FXML
    public ListView favoritesListView;
    @FXML
    public TabPane mainTabPane;
    @FXML
    public TextField searchTextField;


    public Callback cellController = (Callback<ListView, ListCell>) param -> {
        MangaListCell listCell = new MangaListCell();
        listCell.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    Manga clicked = listCell.getItem();

                    Tab chaptersTab = new Tab();
                    chaptersTab.setText(clicked.name);
                    chaptersTab.setContent(new ChaptersPane(clicked, mainTabPane));
                    mainTabPane.getTabs().add(chaptersTab);

                }
            }
        });
        return listCell;
    };

    public void loadManga() {
        mangaListView.setCellFactory(cellController);
        mangaListView.setItems(mangas);

        favoritesListView.setCellFactory(cellController);
        favoritesListView.setItems(favorites);
    }
}
