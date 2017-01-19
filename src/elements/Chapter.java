package elements;

import java.net.URI;

/**
 * Created by Antoine on 18/01/2017.
 */
public class Chapter {

    public String manga;

    public double number;

    public URI address;

    public Chapter(String manga, double number, URI address) {
        this.manga = manga;
        this.number = number;
        this.address = address;
    }
}
