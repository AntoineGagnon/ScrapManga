package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scraping.MANGASITE;
import scraping.MangaLoader;

public class Main extends Application {

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
        controller.loadManga();
        primaryStage.setTitle("Manga Reader");
        Scene newScene = new Scene(root, 800, 600);
        primaryStage.setScene(newScene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        Runnable task = () -> {
            MangaLoader ml = new MangaLoader(DEFAULT_SITE);
            controller.mangas.addAll(ml.loadMangas());
        };
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }
}
