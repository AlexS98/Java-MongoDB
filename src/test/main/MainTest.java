package main;

import org.junit.Test;
import org.bson.Document;
import org.junit.Assert;

import java.util.ArrayList;

public class MainTest {
    @Test
    public void showResultTest() throws Exception {
        ArrayList<Document> list = new ArrayList<Document>();
        list.add(new Document("1", "22"));
        list.add(new Document("333", "4444"));
        Assert.assertEquals(Main.showResult(list.iterator()), "Document{{1=22}}\nDocument{{333=4444}}\n");
    }

}