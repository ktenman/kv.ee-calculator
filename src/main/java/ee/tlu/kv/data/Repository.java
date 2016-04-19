package ee.tlu.kv.data;

import java.io.IOException;

/**
 * Created by Konstantin on 19.04.2016.
 */
public interface Repository {
    String findAllKVsByKeyWord(String searchWord) throws IOException;
}
