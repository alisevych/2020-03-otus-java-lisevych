package ru.otus;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.json.*;

public class DiyGsonImpl implements DiyGson {

    public String toJson(Object src) {
        if (src == null){
            return "null";
        }
        if ( isObjectSimple(src)) {
            return "\"" + src.toString() + "\"";
        }
        return parseObject(src).build().toString();
    }

    private JsonObjectBuilder parseObject(Object obj) {
        var objBuilder = Json.createObjectBuilder();
        var fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            var fieldJson = parseField(field, obj);
            if (fieldJson != null) {
                objBuilder.addAll(fieldJson);
            }
        }
        return objBuilder;
    }

    private JsonObjectBuilder parseField(Field field, Object object) {
        var fieldJson = Json.createObjectBuilder();
        var fieldType = field.getType();
        Object value = getObjectValueFromField(field, object);
        if (value == null) {
            return null;
        }
        if ( isObjectSimple(value) ){
            fieldJson.add(field.getName(), value.toString());
        } else if (fieldType.isArray()) {
                fieldJson.add(field.getName(), getValuesFromArray((Object[]) value));
        } else if (value instanceof java.util.List) {
            fieldJson.add(field.getName(), getValuesFromList((List<?>) value));
        } else if (value instanceof java.util.Set) {
        fieldJson.add(field.getName(), getValuesFromSet((Set<?>) value));
        } else if (value instanceof java.util.Map) {
            fieldJson.add(field.getName(), getValuesFromMap((Map<?,?>) value));
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

    private JsonArrayBuilder getValuesFromList(List<?> list) {
        var arrayJson = Json.createArrayBuilder();
        for (Object element : list) {
            parseElement(element, arrayJson);
        }
        return arrayJson;
    }

    private JsonArrayBuilder getValuesFromSet(Set<?> set) {
        var arrayJson = Json.createArrayBuilder();
        for (Object element : set) {
            parseElement(element, arrayJson);
        }
        return arrayJson;
    }

    private JsonObjectBuilder getValuesFromMap(Map<?,?> map) {
        var mapJson = Json.createObjectBuilder();
        map.forEach((k,v) -> {
            if (isObjectSimple(k)) {
                if (isObjectSimple(v)) {
                    mapJson.add(k.toString(), v.toString());
                } else {
                    mapJson.add(k.toString(), parseObject(v));
                }
            } else {
                throw new UnsupportedOperationException("Key in Map is not of simple type: "+ k.getClass());
            }
        });
        return mapJson;
    }

    private void parseElement (Object element, JsonArrayBuilder arrayBuilder) {
        if (element == null) {
            arrayBuilder.addNull();
        } else if ( isObjectSimple( element)) {
            arrayBuilder.add(element.toString());
        } else {
            arrayBuilder.add(parseObject(element));
        }
    }

    private boolean isObjectSimple (Object obj) {
        var objClass = obj.getClass();
        return (objClass.isPrimitive() ) ||
                (objClass.equals(String.class)) ||
                (objClass.equals(Integer.class)) ||
                (objClass.equals(Long.class)) ||
                (objClass.equals(Short.class)) ||
                (objClass.equals(Byte.class)) ||
                (objClass.equals(Boolean.class));
    }

}
