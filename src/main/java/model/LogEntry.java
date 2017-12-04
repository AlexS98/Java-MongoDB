package model;

import java.io.IOException;
import java.util.Date;

public class LogEntry {
    public String entryURL;
    public String entryIP;
    public Date entryDate;
    public long entryTimeSeconds;

    public LogEntry(String url, String ip, Date date, long time) throws IOException {
        entryURL = url;
        entryIP = ip;
        entryDate = (date == null)? new Date(): date;
        entryTimeSeconds = time;
    }

    @Override
    public String toString(){
        return "URL: " + entryURL +
                "\nIP: " + entryIP +
                "\nDate: " + entryDate.toString() +
                "\nTime: " + getFormatTime(entryTimeSeconds);
    }

    private String getFormatTime(long seconds){
        return  seconds/360 +
                ":" + (((seconds % 3600) >= 600)?(seconds % 360)/60: "0" + (seconds % 360)/60) +
                ":" + (((seconds % 60) >= 10)? seconds % 60: "0" + seconds % 60);
    }


}
