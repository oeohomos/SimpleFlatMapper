package org.simpleflatmapper.reflect.meta;

import org.simpleflatmapper.reflect.Getter;
import org.simpleflatmapper.reflect.ReflectionService;
import org.simpleflatmapper.reflect.ScoredGetter;
import org.simpleflatmapper.reflect.ScoredSetter;
import org.simpleflatmapper.reflect.Setter;

import java.lang.reflect.Type;

public class ObjectPropertyMeta<T, P> extends PropertyMeta<T, P> {

	private final ScoredSetter<T, P> setter;
    private final ScoredGetter<T, P> getter;
	private final Type type;

	public ObjectPropertyMeta(String name, Type ownerType, ReflectionService reflectService, Type propertyType, ScoredGetter<T, P> getter, ScoredSetter<T, P> setter) {
		super(name, ownerType, reflectService);
		this.type = propertyType;
        this.getter = getter;
        this.setter = setter;
	}


    public PropertyMeta<T, P> getterSetter(ScoredGetter<T, P> getter, ScoredSetter<T, P> setter) {
        return new ObjectPropertyMeta<T, P>(getName(), getOwnerType(), reflectService, type, this.getter.best(getter), this.setter.best(setter));
    }

	@Override
	public Setter<? super T, ? super P> getSetter() {
		return setter.getSetter();
	}

    @Override
    public Getter<? super T, ? extends P> getGetter() {
        return getter.getGetter();
    }

    @Override
	public Type getPropertyType() {
		return type;
	}

	@Override
	public String getPath() {
		return getName();
	}

    @Override
    public String toString() {
        return "ObjectPropertyMeta{" +
                "name="+ getName() +
                ", type=" + type +
                ", setter=" + setter +
                ", getter=" + getter +
                '}';
    }

}
