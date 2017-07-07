package scraping;

import elements.Manga;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antoine on 18/01/2017.
 */
public class MangaLoader {
    private static final String WEBSITE_ADDRESS = "http://www.mangadeep.com/latest-chapters";

    private Document doc;
    private int currentPage = 1;
    private List<String> favorites = null;

    public MangaLoader() {
        try {
            doc = Jsoup.connect(WEBSITE_ADDRESS + '/' + currentPage).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToFavorites(String favorite) {
        addToFavorites(favorite, false);
    }

    public void addToFavorites(String favorite, boolean addToFile) {
        if (!favorites.contains(favorite)) {
            favorites.add(favorite);
            if (addToFile) {
                try {
                    System.out.println("Adding " + favorite + " to file");
                    ClassLoader cl = getClass().getClassLoader();
                    PrintWriter pw = new PrintWriter(cl.getResource("favorites.txt").getFile());
                    pw.print(favorite);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isFavorite(String name) {

        if (favorites == null) {
            favorites = new ArrayList<String>();
            try {
                BufferedReader in;
                in = new BufferedReader(new InputStreamReader(this.getClass().getResource(
                        "/favorites.txt").openConnection().getInputStream()));
                String fetchedName;
                while ((fetchedName = in.readLine()) != null) {
                    favorites.add(fetchedName);
                    System.out.println("Added favorite : " + fetchedName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return favorites.contains(name);
    }

    private List<Manga> getMangasFromDoc(Document doc, boolean filterFavorite) {
        List<Manga> mangas = new ArrayList<Manga>();
        if (doc == null) {
            return mangas;
        }

        Elements rows = doc.getElementsByClass("row");

        for (Element row :
                rows) {
            Manga m = getMangaFromLink(row);

            if (filterFavorite) {
                if (isFavorite(m.name)) {
                    mangas.add(m);
                }
            } else {
                mangas.add(m);

            }
        }
        return mangas;
    }

    private Manga getMangaFromLink(Element row) {
        Element link = row.getElementsByClass("ttl").first();

        // Get Manga Name
        String name = link.attr("alt");

        // Get Manga URI
        URI address = null;
        String lastUpdate = "";
        try {
            address = new URI(link.attr("href"));
            lastUpdate = row.getElementsByClass("dte").first().html();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        System.out.println("Loading : " + name);
        return new Manga(name, address, lastUpdate);
    }

    public List<Manga> loadMangas() {

        System.out.println("Loading mangas from page " + currentPage);
        return getMangasFromDoc(doc, false);

    }

    public List<Manga> loadFavorites() {
        System.out.println("Loading favorites from page " + currentPage);

        return getMangasFromDoc(doc, true);
    }

    public void nextPage() {
        currentPage++;
        refreshDoc();
    }

    public void previousPage() {
        currentPage--;
        refreshDoc();
    }

    private void refreshDoc() {
        try {
            System.out.println("Refreshing doc");
            doc = Jsoup.connect(WEBSITE_ADDRESS + '/' + currentPage).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}