package handlers;

import model.LogEntry;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ConverterTest {
    @Test
    public void fromCSVtoLogsTest() throws Exception {
        ArrayList<LogEntry> singleList = new ArrayList<LogEntry>();
        singleList.add(new LogEntry("www.google.com", "132.65.13.255", 1512055538000L, 452));
        assertEquals(Converter.fromCSVtoLogs("src/test/single.csv"), singleList);
    }
}