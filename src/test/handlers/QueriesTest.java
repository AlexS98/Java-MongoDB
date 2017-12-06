package handlers;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static main.Main.showResult;

public class QueriesTest {
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private Queries query = null;

    @Before
    public void initialize() {
        MongoClient client = new MongoClient("localhost", 27017);
        this.database = client.getDatabase("logDB");
        this.collection = database.getCollection("logs");
        this.query = new Queries(database, collection);
        Bson condition = new Document("$gt", "");
        Bson filter = new Document("entryURL", condition);
        collection.deleteMany(filter);
        Converter.csvToDB("logs.csv", collection);
    }

    @Test
    public void getIpByUrlTest(){
        Set<String> expected = new HashSet<String>();
        expected.add("251.0.59.112");
        expected.add("78.159.65.38");
        expected.add("80.195.30.28");
        expected.add("128.0.198.12");
        expected.add("156.28.31.40");
        Assert.assertEquals(query.getIpByUrl("www.youtube.com"), expected);
    }

    @Test
    public void getUrlByIpTest(){
        Set<String> expected = new HashSet<String>();
        expected.add("github.com");
        expected.add("travis-ci.org");
        expected.add("www.google.com");
        expected.add("www.codacy.com");
        expected.add("www.youtube.com");
        Assert.assertEquals(query.getUrlByIp("251.0.59.112"), expected);
    }

    @Test
    public void getTopUrlPerPeriodTest() throws ParseException {
        String expected = "Document{{_id=www.youtube.com, value=25.0}}\n" +
                            "Document{{_id=travis-ci.org, value=21.0}}\n" +
                            "Document{{_id=www.codacy.com, value=20.0}}\n" +
                            "Document{{_id=www.google.com, value=19.0}}\n" +
                            "Document{{_id=github.com, value=15.0}}\n";
        Assert.assertEquals(showResult(query.getTopUrlPerPeriod("10.11.2017 00:00:00","30.11.2017 23:59:59")), expected);
    }

    @Test
    public void getTopUrlByTimeTest() throws ParseException {
        Set<String> expected = new HashSet<String>();
        expected.add("github.com");
        expected.add("travis-ci.org");
        expected.add("www.google.com");
        expected.add("www.codacy.com");
        expected.add("www.youtube.com");
        Assert.assertEquals(query.getUrlByTime("10.11.2017 00:00:00","30.11.2017 23:59:59"), expected);
    }

    @Test
    public void getTopUrlBySumDurationTest(){
        String expected = "Document{{_id=www.youtube.com, value=20927.0}}\n" +
                            "Document{{_id=travis-ci.org, value=16517.0}}\n" +
                            "Document{{_id=www.google.com, value=16352.0}}\n" +
                            "Document{{_id=www.codacy.com, value=14325.0}}\n" +
                            "Document{{_id=github.com, value=12858.0}}\n";
        Assert.assertEquals(showResult(query.getTopUrlBySumDuration()), expected);
    }

    @Test
    public void getTopUrlByVisit() throws Exception {
        ArrayList<Document> topList = toArrayList(query.getTopUrlByVisit());
        Assert.assertEquals(topList.get(0).get("_id"), "www.youtube.com");
        Assert.assertEquals(topList.get(0).get("value"), 25.0);
    }

    @Test
    public void getTopUrlPerPeriod() throws Exception {
        ArrayList<Document> topList = toArrayList(query.getTopUrlPerPeriod("10.11.2017 12:00:00", "27.11.2017 00:00:00"));
        Assert.assertEquals(topList.get(1).get("_id"), "travis-ci.org");
        Assert.assertEquals(topList.get(1).get("value"), 21.0);
    }

    @Test
    public void getTopIpByVisitAndDuration() throws Exception {
        ArrayList<Document> topList = toArrayList(query.getTopIpByVisitAndDuration());
        Assert.assertEquals(topList.get(1).get("_id"), "251.0.59.112");
        Assert.assertEquals(topList.get(1).get("value"), new Document("count", 20.0).append("duration", 16667.0));
    }

    private ArrayList<Document> toArrayList(Iterator<Document> documentFindIterable) {
        ArrayList<Document> result = new ArrayList<>();
        for (Iterator<Document> it = documentFindIterable; it.hasNext(); ) {
            Document d = it.next();
            result.add(d);
        }
        return result;
    }
}