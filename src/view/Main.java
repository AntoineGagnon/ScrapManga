package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scraping.MANGASITE;
import scraping.MangaLoader;

public class Main extends Application {

    public static MangaLoader mangaLoader;
    private TabController controller;
    private MANGASITE DEFAULT_SITE = MANGASITE.MANGAMAP;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("layouts/main_window.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        controller.configureListViews();
        primaryStage.setTitle("Manga Reader");
        Scene newScene = new Scene(root, 800, 600);
        primaryStage.setScene(newScene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        Runnable task = () -> {
            mangaLoader = new MangaLoader();
            controller.mangas.addAll(mangaLoader.loadMangas());
            controller.favorites.addAll(mangaLoader.loadFavorites());
            System.out.println("Done loading first round");
            controller.mangaLoadingSpinner.setVisible(false);
        };
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }
}
