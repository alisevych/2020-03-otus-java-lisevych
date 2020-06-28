package ru.otus;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.json.*;

public class DiyGsonImpl implements DiyGson {

    public String toJson(Object src) {
        if (src == null){
            return "null";
        }
        return parseObject(src).build().toString();
    }

    private JsonObjectBuilder parseObject(Object obj) {
        var bigJson = Json.createObjectBuilder();
        for (Field field : obj.getClass().getDeclaredFields()) {
            bigJson.addAll(parseField(field, obj));
        }
        return bigJson;
    }

    private JsonObjectBuilder parseField(Field field, Object object) {
        var smallJson = Json.createObjectBuilder();
        var fieldType = field.getType();
        if ((fieldType.isPrimitive())
                || (fieldType.equals(String.class))){
            smallJson.add(field.getName(), getValueFromField(field, object));
        } else if (fieldType.isArray()) {
            smallJson.add(field.getName(), getValuesFromArray(field, object));
        } else {
            throw new UnsupportedOperationException("Field type is " + fieldType);
            //List, Set, Map, other objects
        }
        return smallJson;
    }

    private String getValueFromField(Field field, Object obj) {
        if (Modifier.isPrivate(field.getModifiers()))
            field.setAccessible(true);
        try {
            return field.get(obj).toString();
        } catch ( IllegalAccessException e) {
            return null;
        }
    }

    private JsonArrayBuilder getValuesFromArray(Field field, Object src){
        if (Modifier.isPrivate(field.getModifiers()))
            field.setAccessible(true);
        Object value;
        try {
            value = field.get(src);
        } catch ( IllegalAccessException e) {
            return null;
        }
        if (value == null) {
            return null;
        }
        if (!value.getClass().isArray())
            throw new IllegalArgumentException("Array type expected but found " + value.getClass());
        var arrayJson = Json.createArrayBuilder();
        for (Object element: (Object[]) value) {
            if (element == null) {
                arrayJson.addNull();
            } else if ( (element.getClass().isPrimitive() )
                    || (element.getClass().equals(String.class))) {
                arrayJson.add(element.toString());
            } else {
                //ToDo for Object types
                arrayJson.add(parseObject(element));
            }
        }
        return arrayJson;
    }

}
