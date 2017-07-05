package elements;

import java.net.URI;

/**
 * Created by Antoine on 18/01/2017.
 */
public class Chapter {

    public String manga;

    public double number;

    public URI address;

    public String releaseDate;

    public Chapter(String manga, double number, URI address, String chapterDate) {
        this.manga = manga;
        this.number = number;
        this.address = address;
        this.releaseDate = chapterDate;
    }
}
