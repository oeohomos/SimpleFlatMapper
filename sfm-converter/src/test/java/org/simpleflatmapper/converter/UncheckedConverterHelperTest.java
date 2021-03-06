package org.simpleflatmapper.converter;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class UncheckedConverterHelperTest {


    @Test
    public void testUncheckConverterFail() {

        Converter<Object, Object> converter = new Converter<Object, Object>() {
            @Override
            public Object convert(Object in) throws IOException {
                throw new IOException("What!");
            }
        };


        try {
            convertUncheck(converter);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }


    }

    @Test
    public void testUncheckConverterWorj() {

        Converter<Object, Object> converter = new Converter<Object, Object>() {
            @Override
            public Object convert(Object in) throws IOException {
                return "Ok!";
            }
        };

        assertEquals("Ok!", UncheckedConverterHelper.toUnchecked(converter).convert(null));
    }

    private void convertUncheck(Converter<Object, Object> converter) {
        UncheckedConverterHelper.toUnchecked(converter).convert(null);
    }
}