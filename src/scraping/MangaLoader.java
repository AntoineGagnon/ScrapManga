package scraping;

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
public class MangaLoader {
    private Document doc;

    public MangaLoader(MANGASITE ms) {

        try {
            doc = Jsoup.connect("http://www.mangamap.com/").get();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<Manga> loadMangas() {
        List<Manga> mangas = new ArrayList<>();

        Elements rows = doc.getElementsByClass("row");

        for (Element row :
                rows) {
            Element link = row.getElementsByClass("ttl").first();

            // Get Manga Name
            String name = link.attr("alt");

            // Get Manga URI
            URI address = null;
            try {
                address = new URI(link.attr("href"));

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            System.out.println("Loading : " + name);
            mangas.add(new Manga(name, address));
        }
        return mangas;
    }
}