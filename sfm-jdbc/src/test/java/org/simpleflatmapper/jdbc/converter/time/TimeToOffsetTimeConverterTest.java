package org.simpleflatmapper.jdbc.converter.time;

import org.junit.Test;

import java.sql.Time;
import java.time.OffsetTime;
import java.time.ZoneOffset;

import static org.junit.Assert.*;

public class TimeToOffsetTimeConverterTest {

    ZoneOffset offset = ZoneOffset.MIN;
    TimeToOffsetTimeConverter converter = new TimeToOffsetTimeConverter(offset);
    @Test
    public void testConvertTime() throws Exception {
        Time time = new Time(System.currentTimeMillis());
        OffsetTime offsetTime = converter.convert(time);
        assertEquals(time.toLocalTime().atOffset(offset), offsetTime);
    }

    @Test
    public void testConvertNull() throws Exception {
        assertNull(converter.convert(null));
    }
}