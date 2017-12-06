package model;

import java.io.IOException;
import java.util.Date;

public class LogEntry {
    public String entryURL;
    public String entryIP;
    public long entryDate;
    public long entryTimeSeconds;

    public LogEntry(String ip, String url, long date, long time) throws IOException {
        entryURL = url;
        entryIP = ip;
        entryDate = (date == 0)? new Date().getTime(): date;
        entryTimeSeconds = time;
    }

    @Override
    public String toString(){
        return "URL: " + entryURL +
                "\nIP: " + entryIP +
                "\nDate: " + new Date(entryDate).toString() +
                "\nTime: " + getFormatTime(entryTimeSeconds);
    }

    private String getFormatTime(long seconds){
        return  seconds/3600 +
                ":" + (((seconds % 3600) >= 600)?(seconds % 3600)/60: "0" + (seconds % 3600)/60) +
                ":" + (((seconds % 60) >= 10)? seconds % 60: "0" + seconds % 60);
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = true;
        final LogEntry other = (LogEntry) obj;
        if (obj == null) {
            result =  false;
        }
        if ((this.entryURL == null) ? (other.entryURL != null) : !this.entryURL.equals(other.entryURL)) {
            result = false;
        }
        if ((this.entryIP == null) ? (other.entryIP != null) : !this.entryIP.equals(other.entryIP)) {
            result = false;
        }
        if (this.entryTimeSeconds != other.entryTimeSeconds) {
            result = false;
        }
        if (this.entryDate != other.entryDate) {
            result = false;
        }
        return result;
    }
}
