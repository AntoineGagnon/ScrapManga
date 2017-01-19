package view;

import elements.Chapter;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;


/**
 * Created by Antoine on 18/01/2017.
 */
public class ChapterReaderPane extends AnchorPane {

    @FXML
    public Button previousPage;
    @FXML
    public ImageView imageView;
    @FXML
    public Button nextPage;

    private Semaphore mutex = new Semaphore(1);

    private List<Image> images;

    private int currentPage = 1;

    private int totalPages;


    private Chapter chapter;

    public ChapterReaderPane(Chapter chapter) {
        this.chapter = chapter;
        this.images = new ArrayList<>();


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "layouts/chapter_reader_pane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        nextPage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (totalPages > (currentPage)) {
                    while(images.get(currentPage).getProgress() != 1){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    imageView.setImage(images.get(currentPage));
                    currentPage++;
                }
            }
        });

        loadImages();

    }

    private void loadImages() {
        Document doc = null;
        try {
            doc = Jsoup.connect(chapter.address.toString()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (doc != null) {
            Element pagesList = doc.getElementsByClass("cbo_wpm_pag").first();
            Element lastPage = pagesList.getElementsByTag("option").last();
            totalPages = Integer.parseInt(lastPage.val());

            for (int i = 0; i < totalPages; i++) {
                this.images.add(null);
            }

            Element image = doc.getElementsByClass("manga-page").first();
            String firstImageURL = image.attr("src");

            Image firstImage = getImage(firstImageURL);
            if (firstImage != null) {
                imageView.setImage(firstImage);
                images.add(firstImage);
            }


            for (int i = 2; i <= totalPages; i++) {
                int finalI = i;
                Runnable task = () -> {
                    try {
                        mutex.acquire();

                        String imageURL = getImageURLFromPage(chapter.address.toString() + finalI);
                        images.add(finalI - 1, getImage(imageURL));

                        mutex.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                };
                Thread backgroundThread = new Thread(task);
                backgroundThread.setDaemon(true);
                backgroundThread.start();

            }
        }


    }

    private String getImageURLFromPage(String url) {
        Document doc = null;
        String imageURL = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (doc != null) {
            Element image = doc.getElementsByClass("manga-page").first();
            imageURL = image.attr("src");
        }
        return imageURL;
    }

    private Image getImage(String url) {
        try {
            HttpURLConnection httpcon = (HttpURLConnection) new URL(url).openConnection();
            httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
            InputStream stream = httpcon.getInputStream();
            return new Image(stream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}



