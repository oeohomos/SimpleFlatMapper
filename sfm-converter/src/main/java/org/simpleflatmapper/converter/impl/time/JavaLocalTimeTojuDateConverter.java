package org.simpleflatmapper.converter.impl.time;

import org.simpleflatmapper.converter.Converter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class JavaLocalTimeTojuDateConverter implements Converter<LocalTime, Date> {
    private final ZoneId dateTimeZone;

    public JavaLocalTimeTojuDateConverter(ZoneId dateTimeZone) {
        this.dateTimeZone = dateTimeZone;
    }

    @Override
    public Date convert(LocalTime in) throws Exception {
        if (in == null) return null;
        return Date.from(in.atDate(LocalDate.now()).atZone(dateTimeZone).toInstant());
    }
}
