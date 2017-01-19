package scraping;

import elements.Chapter;
import elements.Manga;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antoine on 18/01/2017.
 */
public class ChaptersLoader {

    private Document doc;

    private Manga manga;

    public ChaptersLoader(Manga manga) {
        this.manga = manga;

        try {
            doc = Jsoup.connect(manga.address.toString()).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<Chapter> loadChapters() {
        List<Chapter> chapters = new ArrayList<>();

        Elements rows = doc.getElementsByAttributeValueStarting("title", manga.name + " Chapter");

        for (Element row :
                rows) {

            String chapterNumber = row.attr("title").substring(manga.name.length() + 9);

            System.out.println("Loading : " + chapterNumber);

            try {
                // Get Manga Name
                double number = Double.parseDouble(chapterNumber);

                // Get Manga URI
                URI address = null;
                try {
                    address = new URI(row.attr("href"));

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                chapters.add(new Chapter(manga.name, number, address));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return chapters;
    }
}
