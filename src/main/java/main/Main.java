package main;

import com.mongodb.*;

import com.mongodb.client.*;

import handlers.Converter;
import handlers.Queries;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;


public class Main {
    public static int main(String args[]){
        try {
            MongoClient mongo = new MongoClient("localhost", 27017);
            MongoDatabase database = mongo.getDatabase("logDB");
            MongoCollection<Document> collection = database.getCollection("logs");
            Bson condition = new Document("$gt", "");
            Bson filter = new Document("entryURL", condition);
            collection.deleteMany(filter);
            Converter.csvToDB("logs.csv", collection);
            //Set<String> a = new Queries(database, collection).getUrlByIp("251.0.59.112");
            //System.out.println(a);
            System.out.println(showResult(new Queries(database, collection).getTopUrlPerPeriod("10.11.2017 00:00:00", "30.11.2017 23:59:59")));
            //System.out.println(showResult(collection.find().iterator()));
            mongo.close();
        }
        catch (ParseException e)
        {
            return 1;
        }
        return 0;
    }

    public static String showResult(Iterator<Document> cursor){
        String result = "";
        while (cursor.hasNext()) {
            result += cursor.next() + "\n";
        }
        return result;
    }
}
