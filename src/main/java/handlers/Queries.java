package handlers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.descending;

import org.bson.Document;
import org.json.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Queries {
    private MongoDatabase database = null;
    private MongoCollection<Document> collection = null;

    public Queries(MongoDatabase database, MongoCollection<Document> collection){
        this.collection = collection;
        this.database = database;
    }

    public Set<String> getIpByUrl(String url){
        MongoCursor<Document> cursor = collection.find(eq("entryURL", url)).iterator();
        Set<String> ipList = new HashSet<String>();
        if (!cursor.hasNext()){
            ipList.add("ERROR! Not find any ip by url: " + url);
            return ipList;
        }
        try {
            while (cursor.hasNext()) {
                String log = cursor.next().toJson();
                JSONObject jsonLog = new JSONObject(log);
                ipList.add(jsonLog.getString("entryIP"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return ipList;
    }

    public Set<String> getUrlByIp(String ip){
        MongoCursor<Document> cursor = collection.find(eq("entryIP", ip)).iterator();
        Set<String> urlList = new HashSet<String>();
        if (!cursor.hasNext()){
            urlList.add("ERROR! Not find any url by ip: " + ip);
            return urlList;
        }
        try {
            while (cursor.hasNext()) {
                String log = cursor.next().toJson();
                JSONObject jsonLog = new JSONObject(log);
                urlList.add(jsonLog.getString("entryURL"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return urlList;
    }

    public Set <String> getUrlByTime(String firstTime, String secondTime) throws ParseException {

        Long firstTimeUnix = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(firstTime).getTime();
        Long secondTimeUnix = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(secondTime).getTime();
        MongoCursor<Document> cursor = collection
                .find(and(gt("entryDate",firstTimeUnix),lt("entryDate",secondTimeUnix)))
                .iterator();

        Set<String> urlList = new HashSet<String>();
        if (!cursor.hasNext()){
            urlList.add("ERROR! Not find any url During this time");
            return urlList;
        }
        try {
            while (cursor.hasNext()) {
                String log = cursor.next().toJson();
                JSONObject jsonLog = new JSONObject(log);
                urlList.add(jsonLog.getString("entryURL"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return urlList;
    }

    public Iterator<Document> getTopUrlBySumDuration(){
        String map = "function() { emit(this.entryURL, this.entryTimeSeconds); }";
        String reduce = "function(key, values) {return Array.sum(values)}";
        collection.mapReduce(map, reduce).collectionName("topUrlBySumDuration").toCollection();
        Iterator<Document> response = database.getCollection("topUrlBySumDuration")
                .find()
                .sort(descending("value"))
                .iterator();
        return response;
    }

    public Iterator<Document> getTopUrlByVisit(){
        String map = "function() { emit(this.entryURL,1); }";
        String reduce = "function(key, values) {return Array.sum(values)}";
        collection.mapReduce(map, reduce).collectionName("topUrlByVisit").toCollection();
        Iterator<Document> response = database.getCollection("topUrlByVisit")
                .find()
                .sort(descending("value"))
                .iterator();
        return response;
    }

    public Iterator<Document> getTopUrlPerPeriod(String firstTime, String secondTime) throws ParseException {
        Long firstTimeUnix = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(firstTime).getTime();
        Long secondTimeUnix = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(secondTime).getTime();

        String map = String.format("function () {" +
                "if (this.entryDate >= %s && this.entryDate <= %s)" +
                "emit(this.entryURL, 1); }", firstTimeUnix.toString(), secondTimeUnix.toString());
        String reduce = "function(key, values) { return Array.sum(values); }";
        collection.mapReduce(map, reduce).collectionName("topUrlPerPeriod").toCollection();
        Iterator<Document> response = database.getCollection("topUrlPerPeriod")
                .find()
                .sort(descending("value"))
                .iterator();
        return response;
    }

    public Iterator<Document> getTopIpByVisitAndDuration(){
        String map = "function(){" +
                "emit(this.entryIP, {count:1, duration:this.entryTimeSeconds});}";
        String reduce = "function(key, values) {" +
                "var count=0; var duration=0;" +
                "for(var x in values)" +
                "{count += values[x].count; duration += values[x].duration;}" +
                "return {count:count, duration:duration}}";
        collection.mapReduce(map, reduce).collectionName("topIpVisitAndDuration").toCollection();
        Iterator<Document> response = database.getCollection("topIpVisitAndDuration")
                .find()
                .sort(descending("value"))
                .iterator();
        return response;
    }
}
