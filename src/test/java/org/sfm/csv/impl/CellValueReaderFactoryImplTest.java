package org.sfm.csv.impl;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.sfm.csv.CellValueReader;
import org.sfm.csv.CsvColumnDefinition;
import org.sfm.csv.impl.cellreader.StringCellValueReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CellValueReaderFactoryImplTest {


    @Test
    public void testDoesNotReaderAReaderForJavaSqlDate() {
        assertNull(new CellValueReaderFactoryImpl().getReader(java.sql.Date.class, 1, null));
    }

    @Test
    public void testReturnStringForObject() {
        CellValueReader<?> reader = new CellValueReaderFactoryImpl().getReader(Object.class, 1, null);
        String object = "string";
        assertEquals(object, reader.read(object.toCharArray(), 0, object.length(), null));
    }


    @Test
    public void testJodaTime() {
        String date = "20150128";
        DateTime dateTime = DateTimeFormat.forPattern("yyyyMMdd").parseDateTime(date);

        CellValueReader<Object> reader = new CellValueReaderFactoryImpl().getReader(DateTime.class, 0, CsvColumnDefinition.dateFormatDefinition("yyyyMMdd"));

        assertEquals(dateTime, reader.read(date.toCharArray(), 0, date.length(), null));
    }

}
