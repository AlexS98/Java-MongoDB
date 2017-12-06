package handlers;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.opencsv.CSVReader;
import model.LogEntry;
import org.bson.Document;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Converter {
    public static List<LogEntry> fromCSVtoLogs(String path) {
        ArrayList<LogEntry> logs = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(path));
            String[] line;
            while ((line = reader.readNext()) != null) {
                Long time = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(line[2]).getTime();
                LogEntry log = new LogEntry(line[0], line[1], time, Integer.parseInt(line[3]));
                logs.add(log);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return logs;
    }

    public static String fromLogsToJSON(List<LogEntry> logs){
        Gson gson = new Gson();
        String logsString = "";
        for (LogEntry log:logs ){
            String logString = gson.toJson(log);
            logsString += logString + ", ";
        }
        return logsString;
    }

    public static void csvToDB(String path, MongoCollection<Document> collection){
        List<LogEntry> singleLogList = fromCSVtoLogs(path);
        String jsonStrings = fromLogsToJSON(singleLogList);
        ArrayList<String> jsonStringList = new ArrayList<>(Arrays.asList(jsonStrings.split("}, ")));
        for(String jsonStr:jsonStringList){
            Document doc = Document.parse(jsonStr+"}");
            collection.insertOne(doc);
        }
    }
}