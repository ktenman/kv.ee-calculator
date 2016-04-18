package ee.tlu.kv;

/**
 * Created by Konstantin on 18.04.2016.
 */
public class KV {
    private String price;

    public KV(String price) {
        this.price = price;
    }

    public Double getPrice() {
        return Double.parseDouble(price);
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
