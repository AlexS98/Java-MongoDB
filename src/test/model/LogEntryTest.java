package model;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class LogEntryTest {
    @Test
    public void ToString() throws Exception {
        LogEntry logEntry = new LogEntry("http://www.google.com.ua", "192.168.0.1", new Date(), 1596);
        assertEquals(logEntry.toString(), "");
    }

}