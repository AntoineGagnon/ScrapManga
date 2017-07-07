package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import scraping.MANGASITE;
import scraping.MangaLoader;

import java.awt.*;
import java.net.URI;

public class Main extends Application {


    private static MangaLoader mangaLoader;
    private static TabController controller;
    private MANGASITE DEFAULT_SITE = MANGASITE.MANGAMAP;

    public static TabController getController() {
        return controller;
    }

    public static MangaLoader getMangaLoader() {
        return mangaLoader;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void openInBrowser(URI address) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(address);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_window.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        controller.configureListViews();
        primaryStage.setTitle("Manga Reader");
        Scene newScene = new Scene(root, 800, 600);
        primaryStage.setScene(newScene);
        primaryStage.setMaximized(true);
        newScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.W && event.isControlDown()) {
                Tab currentTab = controller.mainTabPane.getSelectionModel().getSelectedItem();
                if (currentTab.isClosable())
                    controller.mainTabPane.getTabs().remove(currentTab);
            }

        });
        primaryStage.show();

        Runnable task = () -> {
            mangaLoader = new MangaLoader();
            controller.mangas.addAll(getMangaLoader().loadMangas());
            controller.favorites.addAll(getMangaLoader().loadFavorites());
            System.out.println("Done loading first round");
            controller.mangaLoadingSpinner.setVisible(false);
        };
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();

    }
}
