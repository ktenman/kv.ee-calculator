package ee.tlu.kv.model;

/**
 * Created by Konstantin on 18.04.2016.
 */
public class JSON {
    private final String city;
    private final double mean;
    private final double std;
    private final double median;

    public JSON(String city, double mean, double std, double median) {
        this.city = city;
        this.mean = mean;
        this.std = std;
        this.median = median;
    }
}