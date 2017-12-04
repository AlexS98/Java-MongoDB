package model;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class LogEntryTest {
    @Test
    public void ToString() throws Exception {
        LogEntry logEntry = new LogEntry("http://www.google.com.ua", "192.168.0.1", new Date(956165616000L), 1596);
        assertEquals(logEntry.toString().substring(0, logEntry.toString().indexOf('D')), "URL: http://www.google.com.ua\n" + "IP: 192.168.0.1\n");
        assertEquals(logEntry.toString().substring(logEntry.toString().indexOf('T')), "T 2000\n" + "Time: 0:26:36");
    }

}