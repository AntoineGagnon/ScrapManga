package elements;

import java.net.URI;

/**
 * Created by Antoine on 18/01/2017.
 */
public class Manga {

    public String name;

    public URI address;
    public String lastRelease;


    public Manga(String name, URI address) {
        this.name = name;
        this.address = address;
    }

    public Manga(String name, URI address, String lastRelease) {
        this.name = name;
        this.address = address;
        this.lastRelease = lastRelease;
    }

    @Override
    public String toString() {
        return "Manga{" +
                "name='" + name + '\'' +
                ", address=" + address +
                ", lastRelease='" + lastRelease + '\'' +
                '}';
    }
}
