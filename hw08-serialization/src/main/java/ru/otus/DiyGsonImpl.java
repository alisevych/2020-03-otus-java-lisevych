package ru.otus;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

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
            var fieldJson = parseField(field, obj);
            if (fieldJson != null) {
                bigJson.addAll(fieldJson);
            }
        }
        return bigJson;
    }

    private JsonObjectBuilder parseField(Field field, Object object) {
        var fieldJson = Json.createObjectBuilder();
        var fieldType = field.getType();
        Object value = getObjectValueFromField(field, object);
        if (value == null) {
            return null;
        }
        if ( (fieldType.isPrimitive())
                ||  (fieldType.equals(String.class)) ){
            fieldJson.add(field.getName(), value.toString());
        } else if (fieldType.isArray()) {
                fieldJson.add(field.getName(), getValuesFromArray((Object[]) value));
        } else if (value instanceof java.util.List) {
            fieldJson.add(field.getName(), getValuesFromList(value));
        //} else if (value instanceof java.util.Set) {
        //fieldJson.add(field.getName(), getValuesFromSet(value));
        //} else if (value instanceof java.util.Map) {
        //    fieldJson.add(field.getName(), getValuesFromMap(value));
        }
        return fieldJson;
    }

    private Object getObjectValueFromField(Field field, Object src) {
        if (Modifier.isPrivate(field.getModifiers()))
            field.setAccessible(true);
        Object value;
        try {
            value = field.get(src);
        } catch ( IllegalAccessException e) {
            return null;
        }
        return value;
    }

    private JsonArrayBuilder getValuesFromArray(Object[] array){
        if (!array.getClass().isArray())
            throw new IllegalArgumentException("Array type expected but found " + array.getClass());
        var arrayJson = Json.createArrayBuilder();
        for (Object element: array) {
            parseElement(element, arrayJson);
        }
        return arrayJson;
    }

    private JsonArrayBuilder getValuesFromList(Object list) {
        var arrayJson = Json.createArrayBuilder();
        for (Object element : (List) list) {
            parseElement(element, arrayJson);
        }
        return arrayJson;
    }

    private void parseElement (Object element, JsonArrayBuilder arrayBuilder) {
        if (element == null) {
            arrayBuilder.addNull();
        } else if ( (element.getClass().isPrimitive() )
                || (element.getClass().equals(String.class))) {
            arrayBuilder.add(element.toString());
        } else {
            arrayBuilder.add(parseObject(element));
        }
    }

}
