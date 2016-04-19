package ee.tlu.kv;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by Konstantin on 18.04.2016.
 */
@Controller
@RequestMapping("/api")
public class MainController {
    @Autowired
    Repository repository;

    @RequestMapping(value = "/{searchWord}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> findAllAverageGrades(@PathVariable("searchWord") String searchWord) throws IOException {
        if (!searchWord.equals("undefined")) {
            searchWord = URLEncoder.encode(searchWord, "UTF-8");
            searchWord = repository.findAllKVsByKeyWord(searchWord);
            return new ResponseEntity<>(searchWord, HttpStatus.OK);
        }
        return new ResponseEntity<>(new Gson().toJson("wait"), HttpStatus.OK);
    }
}
