package main;

import com.mongodb.*;

import com.mongodb.client.*;

import static com.mongodb.client.model.Filters.eq;

import model.LogEntry;
import org.bson.Document;

import java.io.IOException;
import java.util.*;


public class Main {
    public static void main(String args[]) throws IOException {
        MongoClient mongo = new MongoClient( "localhost" , 27017 );
        System.out.println("Connected to the database successfully");

        MongoDatabase database = mongo.getDatabase("logDB");

        MongoCollection<Document> collection = database.getCollection("logs");
        LogEntry[] logs = new LogEntry[] {
                new LogEntry("mySite1.com","192.168.0.1",new Date(),122),
                new LogEntry("mySite2.com","192.168.0.2",new Date(),562),
                new LogEntry("mySite3.com","192.168.0.3",new Date(),781),
                new LogEntry("mySite4.com","192.168.0.4",new Date(),1818),
                new LogEntry("mySite5.com","192.168.0.5",new Date(),163)
        };
        /*
        for (model.LogEntry log: logs) {
            collection.insertOne(convertToDoc(log));
        }
        for(int i = 0; i < logs.length; i++) {
            collection.deleteOne(eq("URL", "mySite" + (i+1) +".com"));
        }
        */
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println((Document)cursor.next());
            }
        }finally {
            cursor.close();
        }
        mongo.close();
    }

    private static Document convertToDoc(LogEntry myLog){
        return new Document("URL", myLog.entryURL)
                .append("IP",myLog.entryIP)
                .append("Date", myLog.entryDate)
                .append("Time", myLog.entryTimeSeconds);
    }

    private static LogEntry convertFromDoc(Document myDoc){
        return null;
    }
}
