package ee.tlu.kv;

import com.google.gson.Gson;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * Created by Konstantin on 18.04.2016.
 */
@Controller
@RequestMapping("/api")
public class MainController {

    @RequestMapping(value = "/{searchWord}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> findAllAverageGrades(@PathVariable("searchWord") String searchWord) throws IOException {
        return new ResponseEntity<String>(findAllKVsByKeyWord(searchWord), HttpStatus.OK);
    }

    public static void main(String[] args) throws IOException {
        new MainController();
    }


    public String findAllKVsByKeyWord(String searchWord) throws IOException {
        List<KV> kvs = new ArrayList<>();
        DescriptiveStatistics stats = new DescriptiveStatistics();
        String kvLink = "http://kinnisvaraportaal-kv-ee.postimees.ee/?act=search.simple&company_id=&page=1&orderby=ob&page_size=100&deal_type=2&dt_select=2&county=0&keyword="
                + searchWord;
        Document document = Jsoup.connect(kvLink).timeout(0).get();
        int kvPageCounter = findKvPageCounter("[class=list jump-pagination-list] li.item a.count", document);
        List<String> searchLinks = IntStream.range(0, kvPageCounter)
                .mapToObj(i ->
                        "http://kinnisvaraportaal-kv-ee.postimees.ee/?act=search.simple&page=" +
                                i +
                                "&orderby=ob&page_size=100&deal_type=2&dt_select=2&price_type=1&keyword=" +
                                searchWord
                )
                .collect(toList());
        for (String link : searchLinks) {
            document = Jsoup.connect(link).timeout(0).get();
            Elements prices = document.select("p.object-price-value");
            for (Element price : prices) {
                kvs.add(
                        new KV(
                                //kvLinks.get(i).attr("href"),
                                price.text().split(" €")[0].replaceAll("[^\\d.]+", "")
                        )
                );

            }
        }
        for (int i = 0; i < kvs.size(); i++) {
            stats.addValue(kvs.get(i).getPrice());
        }

        double mean = stats.getMean();
        System.out.println(mean);
        double std = stats.getStandardDeviation();
        System.out.println(std);
        double median = stats.getPercentile(50);
        System.out.println(median);
        JSON json;
        if (kvs.size() != 0) {
            json = new JSON(
                    searchWord,
                    stats.getMean(),
                    stats.getStandardDeviation(),
                    stats.getPercentile(50)
            );
        } else {
            json = new JSON(
                    searchWord,
                    0,
                    0,
                    0
            );
        }
        return new Gson().toJson(json);
    }

    public int findKvPageCounter(String s, Document document) {
        int counter = 0;
        try {
            Element element = document.select(s).get(0);
            counter = Integer.parseInt(element.text());
        } catch (Exception e) {
        }
        return counter;
    }
}
