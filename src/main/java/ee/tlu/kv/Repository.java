package ee.tlu.kv;

import java.io.IOException;

/**
 * Created by Konstantin on 19.04.2016.
 */
interface Repository {
    String findAllKVsByKeyWord(String searchWord) throws IOException;
}
