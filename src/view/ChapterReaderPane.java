package view;

import elements.Chapter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * Created by Antoine on 18/01/2017.
 */
public class ChapterReaderPane extends BorderPane {

    @FXML
    public Button previousPage;
    @FXML
    public ImageView imageView;
    @FXML
    public Button nextPage;

    @FXML
    public Text pageCounter;

    @FXML
    public BorderPane borderPane;

    private BlockingQueue<Image> imagesLoadingQueue;

    private List<Image> images = new ArrayList<>();


    private int currentPage = 1;

    private int totalPages;


    private Chapter chapter;

    public ChapterReaderPane(Chapter chapter) {
        this.chapter = chapter;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "layouts/chapter_reader_pane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        nextPage.setOnMouseClicked(event -> loadNextPage());
        previousPage.setOnMouseClicked(event -> loadPreviousPage());
        loadImages();

    }

    private void loadNextPage() {
        if (totalPages > (currentPage)) {
            try {
                Image nextImage = null;
                try {
                    nextImage = images.get(currentPage);
                } catch (Exception e) {
                    nextImage = imagesLoadingQueue.take();
                    images.add(nextImage);
                }

                imageView.setImage(nextImage);
                currentPage++;
                pageCounter.setText(currentPage + "/" + totalPages);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadPreviousPage() {
        if (currentPage > 1) {
            Image previousImage = images.get(currentPage - 2);
            imageView.setImage(previousImage);
            currentPage--;
            pageCounter.setText(currentPage + "/" + totalPages);
        }
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


            Element image = doc.getElementsByClass("manga-page").first();
            String firstImageURL = image.attr("src");

            Image firstImage = getImage(firstImageURL);
            if (firstImage != null) {
                imageView.setImage(firstImage);
                images.add(firstImage);
            }

            Executor exec = Executors.newCachedThreadPool(runnable -> {
                Thread t = new Thread(runnable);
                t.setDaemon(true);
                return t;
            });

            imagesLoadingQueue = new ArrayBlockingQueue<Image>(totalPages);

            exec.execute(() -> {
                for (int i = 2; i <= totalPages; i++) {
                    try {

                        String imageURL = getImageURLFromPage(chapter.address.toString() + i);
                        System.out.println("Page " + i + "url = " + imageURL);
                        imagesLoadingQueue.put(getImage(imageURL));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            });


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



