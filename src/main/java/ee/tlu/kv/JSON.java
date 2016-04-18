package ee.tlu.kv;

/**
 * Created by Konstantin on 18.04.2016.
 */
public class JSON {
    private String city;
    private double mean;
    private double std;
    private double median;

    public JSON(String city, double mean, double std, double median) {
        this.city = city;
        this.mean = mean;
        this.std = std;
        this.median = median;
    }


}
