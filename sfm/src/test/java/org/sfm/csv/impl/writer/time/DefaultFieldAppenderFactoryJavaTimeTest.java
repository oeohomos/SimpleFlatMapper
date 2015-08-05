package org.sfm.csv.impl.writer.time;

import org.junit.Test;
import org.sfm.csv.CsvColumnKey;
import org.sfm.csv.impl.writer.CsvCellWriter;
import org.sfm.csv.DefaultFieldAppenderFactory;
import org.sfm.map.FieldMapper;
import org.sfm.map.context.MappingContextFactory;
import org.sfm.map.column.time.JavaDateTimeFormatterProperty;
import org.sfm.map.column.FieldMapperColumnDefinition;
import org.sfm.map.mapper.PropertyMapping;
import org.sfm.map.context.KeySourceGetter;
import org.sfm.map.context.MappingContextFactoryBuilder;
import org.sfm.reflect.ReflectionService;
import org.sfm.reflect.meta.ClassMeta;
import org.sfm.reflect.meta.DefaultPropertyNameMatcher;
import org.sfm.reflect.meta.PropertyMeta;

import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

public class DefaultFieldAppenderFactoryJavaTimeTest {

    private DefaultFieldAppenderFactory defaultFieldAppenderFactory = DefaultFieldAppenderFactory.instance();

    private ClassMeta<JavaTimeObject> javaTimeObjectClassMeta = ReflectionService.newInstance().getClassMeta(JavaTimeObject.class);


    static class JavaTimeObject {
        public LocalDate localDate;
        public LocalDateTime localDateTime;
        public LocalTime localTime;
        public OffsetDateTime offsetDateTime;
        public OffsetTime offsetTime;
        public ZonedDateTime zonedDateTime;
        public Instant instant;
        public MonthDay monthDay;
        public Year year;
        public YearMonth yearMonth;
    }


    JavaTimeObject javaTimeObject = new JavaTimeObject();
    {
        javaTimeObject.zonedDateTime = ZonedDateTime.parse("2011-12-03T10:15:30+01:00[Europe/Paris]");

        javaTimeObject.localDateTime = javaTimeObject.zonedDateTime.toLocalDateTime();
        javaTimeObject.localDate = javaTimeObject.zonedDateTime.toLocalDate();
        javaTimeObject.localTime = javaTimeObject.zonedDateTime.toLocalTime();

        javaTimeObject.offsetDateTime = javaTimeObject.zonedDateTime.toOffsetDateTime();
        javaTimeObject.offsetTime = javaTimeObject.zonedDateTime.toOffsetDateTime().toOffsetTime();

        javaTimeObject.instant = javaTimeObject.zonedDateTime.toInstant();
        javaTimeObject.monthDay = MonthDay.from(javaTimeObject.zonedDateTime);
        javaTimeObject.year = Year.from(javaTimeObject.zonedDateTime);
        javaTimeObject.yearMonth = YearMonth.from(javaTimeObject.zonedDateTime);
    }

    @Test
    public void testZonedDateTimeAppender() throws Exception {
        testFieldMapperForClassAndProp("2011-12-03T10:15:30+01:00[Europe/Paris]", "zonedDateTime", javaTimeObjectClassMeta);
    }

    @Test
    public void testLocalDateTimeAppender() throws Exception {
        testFieldMapperForClassAndProp("2011-12-03T10:15:30", "localDateTime", javaTimeObjectClassMeta);
    }

    @Test
    public void testLocalDateAppender() throws Exception {
        testFieldMapperForClassAndProp("2011-12-03", "localDate", javaTimeObjectClassMeta);
    }

    @Test
    public void testLocalTimeAppender() throws Exception {
        testFieldMapperForClassAndProp("10:15:30", "localTime", javaTimeObjectClassMeta);
    }

    @Test
    public void testOffsetDateTimeAppender() throws Exception {
        testFieldMapperForClassAndProp("2011-12-03T10:15:30+01:00", "offsetDateTime", javaTimeObjectClassMeta);
    }

    @Test
    public void testOffsetTimeAppender() throws Exception {
        testFieldMapperForClassAndProp("10:15:30+01:00", "offsetTime", javaTimeObjectClassMeta);
    }

    @Test
    public void testInstantAppender() throws Exception {
        testFieldMapperForClassAndProp("2011-12-03T09:15:30Z", "instant", javaTimeObjectClassMeta);
    }

    @Test
    public void testMonthDayAppender() throws Exception {
        testFieldMapperForClassAndProp("--12-03", "monthDay", javaTimeObjectClassMeta);
    }

    @Test
    public void testYearAppender() throws Exception {
        testFieldMapperForClassAndProp("2011", "year", javaTimeObjectClassMeta);
    }

    @Test
    public void testYearMonthAppender() throws Exception {
        testFieldMapperForClassAndProp("2011-12", "yearMonth", javaTimeObjectClassMeta);
    }


    @Test
    public void testLocalDateTimeWithFormaterAppender() throws Exception {
        MappingContextFactoryBuilder<JavaTimeObject, CsvColumnKey> builder = getMappingContextBuilder();
        FieldMapper<JavaTimeObject, Appendable> fieldMapper = defaultFieldAppenderFactory.newFieldAppender(
                newPropertyMapping("localDateTime", javaTimeObjectClassMeta, FieldMapperColumnDefinition.<CsvColumnKey>identity().add(new JavaDateTimeFormatterProperty(DateTimeFormatter.ofPattern("dd/MM/yyyy")))),
                CsvCellWriter.DEFAULT_WRITER, builder);
        testFieldMapper("03/12/2011", fieldMapper, javaTimeObject, builder.newFactory());

    }

    public void testFieldMapperForClassAndProp(String expected, String propName, ClassMeta<JavaTimeObject> classMeta) throws Exception {
        MappingContextFactoryBuilder<JavaTimeObject, CsvColumnKey> builder = getMappingContextBuilder();
        FieldMapper<JavaTimeObject, Appendable> fieldMapper = defaultFieldAppenderFactory.newFieldAppender(newPropertyMapping(propName, classMeta), CsvCellWriter.DEFAULT_WRITER, builder);
        testFieldMapper(expected, fieldMapper, javaTimeObject, builder.newFactory());
    }

    private <T> void testFieldMapper(String expected, FieldMapper<T, Appendable> fieldMapper, T source, MappingContextFactory<T> dbObjectMappingContextFactory) throws Exception {
        StringBuilder sb = new StringBuilder();
        fieldMapper.mapTo(source, sb, dbObjectMappingContextFactory.newContext());
        assertEquals(expected, sb.toString());
    }

    private <T> PropertyMapping<T, String, CsvColumnKey, FieldMapperColumnDefinition<CsvColumnKey>> newPropertyMapping(String col, ClassMeta<T> classMeta) {
        return newPropertyMapping(col, classMeta, FieldMapperColumnDefinition.<CsvColumnKey>identity());
    }

    private <T> PropertyMapping<T, String, CsvColumnKey, FieldMapperColumnDefinition<CsvColumnKey>> newPropertyMapping(String col, ClassMeta<T> classMeta, FieldMapperColumnDefinition<CsvColumnKey> columnDefinition) {
        PropertyMeta<T, String> propertyMeta = classMeta.newPropertyFinder().findProperty(DefaultPropertyNameMatcher.of(col));
        if (propertyMeta == null) throw new IllegalArgumentException("cannot find prop " + col);
        return new PropertyMapping<T, String, CsvColumnKey, FieldMapperColumnDefinition<CsvColumnKey>>(
                propertyMeta,
                new CsvColumnKey(col, 1),
                columnDefinition);
    }

    public <T> MappingContextFactoryBuilder<T, CsvColumnKey> getMappingContextBuilder() {
        return new MappingContextFactoryBuilder<T, CsvColumnKey>(new KeySourceGetter<CsvColumnKey, T>() {
            @Override
            public Object getValue(CsvColumnKey key, T source) throws SQLException {
                return null;
            }
        });
    }

}