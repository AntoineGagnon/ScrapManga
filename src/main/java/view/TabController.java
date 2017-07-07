package view;

import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSpinner;
import elements.Manga;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;
import scraping.MangaLoader;

public class TabController {

    final ObservableList<Manga> mangas = FXCollections.observableArrayList();
    final ObservableList<Manga> favorites = FXCollections.observableArrayList();

    @FXML
    public JFXListView<Manga> mangaListView;
    @FXML
    public JFXListView<Manga> favoritesListView;
    @FXML
    public TabPane mainTabPane;
    @FXML
    public TextField searchTextField;
    @FXML
    public JFXSpinner mangaLoadingSpinner;


    public Callback cellController = (Callback<JFXListView, JFXListCell>) param -> {
        MangaListCell listCell = new MangaListCell();
        listCell.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    Manga clicked = listCell.getItem();

                    Tab chaptersTab = new Tab();
                    chaptersTab.setClosable(true);
                    chaptersTab.setText(clicked.name);
                    chaptersTab.setContent(new MangaPane(clicked, mainTabPane));
                    mainTabPane.getTabs().add(chaptersTab);

                } else {
                    if (event.isControlDown()) {
                        Main.openInBrowser(listCell.getItem().address);
                    }
                }
            }
        });
        return listCell;
    };

    /**
     * Configure the different listView
     */
    public void configureListViews() {
        mangaListView.setCellFactory(cellController);
        mangaListView.setItems(mangas);

        favoritesListView.setCellFactory(cellController);
        favoritesListView.setItems(favorites);
    }

    public void loadMoreMangas(ActionEvent actionEvent) {
        System.out.println("Loading more mangas");

        mangaLoadingSpinner.setVisible(true);

        MangaLoader mangaLoader = Main.getMangaLoader();
        mangaLoader.nextPage();
        mangas.addAll(mangaLoader.loadMangas());
        favorites.addAll(mangaLoader.loadFavorites());
        mangaLoadingSpinner.setVisible(false);

    }
}
