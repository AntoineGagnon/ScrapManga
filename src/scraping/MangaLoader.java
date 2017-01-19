package scraping;

import elements.Manga;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
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
            doc = Jsoup.connect("http://www.mangamap.com/latest-chapters/").get();
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
            if(isFavorite(name)) {

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
        }
        return mangas;
    }

    private List<String> favorites = null;

    private boolean isFavorite(String name) {

        if(favorites == null){
            favorites = new ArrayList<>();
            try {
                BufferedReader in;
                in = new BufferedReader(new FileReader("favorites.txt"));
                String fetchedName;
                while((  fetchedName = in.readLine())!= null){
                    favorites.add(fetchedName);
                    System.out.println("Added favorite : " + fetchedName);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return favorites.contains(name);
    }
}