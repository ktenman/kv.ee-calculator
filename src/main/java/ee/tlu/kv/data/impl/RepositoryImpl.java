package ee.tlu.kv.data.impl;

import com.google.gson.Gson;
import ee.tlu.kv.data.Repository;
import ee.tlu.kv.model.JSON;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by Konstantin on 19.04.2016.
 */
@Component
public class RepositoryImpl implements Repository {

    @Override
    public String findAllKVsByKeyWord(String searchWord) throws IOException {
        List<Double> kvs = new ArrayList<>();
        String kvLink = "http://kinnisvaraportaal-kv-ee.postimees.ee/?act=search.simple&company_id=&page=1&orderby=ob&page_size=100&deal_type=2&dt_select=2&county=0&keyword="
                + searchWord;
        Document document = Jsoup.connect(kvLink).timeout(0).get();
        int kvPageCounter = findKvPageCounter("[class=list jump-pagination-list] li.item a.count", document);
        List<String> searchLinks = new ArrayList<>();
        for (int i = 0; i <= kvPageCounter; i++) {
            searchLinks.add("http://kinnisvaraportaal-kv-ee.postimees.ee/?act=search.simple&page=" + i +
                    "&orderby=ob&page_size=100&deal_type=2&dt_select=2&price_type=1&keyword=" +
                    searchWord);
        }
        for (String link : searchLinks) {
            document = Jsoup.connect(link).timeout(0).get();
            Elements prices = document.select("p.object-price-value");
            kvs.addAll(prices.stream()
                    .map(price -> Double.parseDouble(price.text().split(" €")[0].replaceAll("[^\\d.]+", "")))
                    .collect(toList()));
        }
        return new Gson().toJson(calculateStats(kvs, searchWord));
    }

    private JSON calculateStats(List<Double> kvs, String searchWord) throws UnsupportedEncodingException {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (Double kv : kvs) {
            stats.addValue(kv);
        }
        JSON json;
        searchWord = URLDecoder.decode(searchWord, "UTF-8");
        searchWord = searchWord.substring(0, 1).toUpperCase() + searchWord.substring(1);
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
        return json;
    }

    private int findKvPageCounter(String s, Document document) {
        int counter = 0;
        try {
            Element element = document.select(s).get(0);
            counter = Integer.parseInt(element.text());
        } catch (Exception e) {
        }
        return counter;
    }
}
