module org.simpleflatmapper.map {
        requires java.logging;

        requires public org.simpleflatmapper.reflect;

        exports org.simpleflatmapper.map;
        exports org.simpleflatmapper.map.context;
        exports org.simpleflatmapper.map.mapper;
        exports org.simpleflatmapper.map.fieldmapper;
        exports org.simpleflatmapper.map.error;
        exports org.simpleflatmapper.map.property;
        exports org.simpleflatmapper.map.property.time;
}