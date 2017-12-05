package main;

import com.mongodb.*;

import com.mongodb.client.*;

import static com.mongodb.client.model.Filters.eq;

import handlers.Converter;
import handlers.Queries;
import model.LogEntry;
import org.bson.Document;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;


public class Main {
    public static void main(String args[]) throws IOException, ParseException {
        MongoClient mongo = new MongoClient( "localhost" , 27017 );
        MongoDatabase database = mongo.getDatabase("logDB");
        MongoCollection<Document> collection = database.getCollection("logs");
        //Converter.csvToDB("logs.csv", collection);
        System.out.println(showResult(new Queries(database, collection).getTopUrlPerPeriod("01.11.2017 00:00:00","25.11.2017 23:59:59")));
        //System.out.println(showResult(collection.find().iterator()));
        mongo.close();
    }

    public static String showResult(Iterator<Document> cursor){
        String result = "";
        while (cursor.hasNext()) {
            result += cursor.next() + "\n";
        }
        return result;
    }
}
