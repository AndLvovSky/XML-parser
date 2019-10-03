package com.andlvovsky.mapper;

import com.andlvovsky.parser.XmlAttribute;
import com.andlvovsky.parser.XmlTag;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TagObjectMapper {

    private Class<?> cls;

    private Object obj;

    public TagObjectMapper(Class<?> cls) {
        this.cls = cls;
    }

    public Object map(XmlTag tag) {
        obj = createObject();
        for (XmlAttribute attr : tag.attrs) {
            mapPrimitiveField(attr.name, attr.value);
        }
        for (XmlTag subtag : tag.tags) {
            if (subtag.containsText) {
                mapPrimitiveField(subtag.name, subtag.text);
            } else {
                mapComplexField(subtag);
            }
        }
        return obj;
    }

    private Object createObject() {
        Constructor<?> ctr;
        try {
            ctr = cls.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("No default constructor");
        }
        try {
            return ctr.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Default construction failed");
        }
    }

    private void mapPrimitiveField(String name, String value) {
        Class<?> fldCls = getFieldClass(name);
        setPrimitiveField(name, value, fldCls);
    }

    private Class<?> getFieldClass(String name) {
        Field fld;
        try {
            fld = cls.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("No field " + name);
        }
        return fld.getType();
    }

    private void setPrimitiveField(String name, String value, Class<?> fldCls) {
        Method setter = getSetterMethod(name, fldCls);
        try {
            if (fldCls.equals(Integer.class) || fldCls.equals(int.class)) {
                setter.invoke(cls.cast(obj), Integer.parseInt(value));
            } else if (fldCls.equals(Double.class) || fldCls.equals(double.class)) {
                setter.invoke(cls.cast(obj), Double.parseDouble(value));
            } else if (fldCls.equals(Boolean.class) || fldCls.equals(boolean.class)) {
                setter.invoke(cls.cast(obj), Boolean.parseBoolean(value));
            } else if (fldCls.isEnum()) {
                setter.invoke(cls.cast(obj), Enum.valueOf((Class<Enum>)fldCls, value.toUpperCase()));
            } else {
                setter.invoke(cls.cast(obj), value);
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("Setting " + name + " field failed");
        }
    }

    private Method getSetterMethod(String name, Class<?> fldCls) {
        String methodName = createSetterName(name);
        try {
            return cls.getMethod(methodName, fldCls);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("No such method " + methodName);
        }
    }

    private String createSetterName(String name) {
        return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private void mapComplexField(XmlTag tag) {
        Class<?> fldCls = getFieldClass(tag.name);
        TagObjectMapper mapper = new TagObjectMapper(fldCls);
        Object objToSet = mapper.map(tag);
        Method setter = getSetterMethod(tag.name, fldCls);
        try {
            setter.invoke(cls.cast(obj), fldCls.cast(objToSet));
        } catch (Exception e) {
            throw new IllegalArgumentException("Setting " + tag.name + " field failed");
        }
    }

}
