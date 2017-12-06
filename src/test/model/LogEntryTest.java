package model;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class LogEntryTest {
    @Test
    public void toStringTest() throws IOException {
        LogEntry logEntry = new LogEntry("192.168.0.1", "http://www.google.com.ua", 956165616000L, 1596);
        assertEquals(logEntry.toString().substring(0, logEntry.toString().indexOf('D')), "URL: http://www.google.com.ua\nIP: 192.168.0.1\n");
    }
}