package ru.otus;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import javax.json.*;

public class DiyGsonImpl implements DiyGson {

    public String toJson(Object src) {
        if (src == null){
            return "null";
        }
        if ( isObjectSimple(src)) {
            return src.toString();
        } else if ( isObjectLiteral(src)) {
            return "\"" + src + "\"";
        }
        var srcClass = src.getClass();
        if (srcClass.isArray()) {
            return getValuesFromArray(src).build().toString();
        } else if (src instanceof java.util.List) {
            return getValuesFromList((List<?>) src).build().toString();
        } else if (src instanceof java.util.Set) {
            return getValuesFromSet((Set<?>) src).build().toString();
        } else if (src instanceof java.util.Map) {
            return getValuesFromMap((Map<?,?>) src).build().toString();
        } else {
            return parseObject(src).build().toString();
        }
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
        if ( isObjectSimple(value) || isObjectLiteral(value) ){
            fieldJson.add(field.getName(), value.toString());
        } else if (fieldType.isArray()) {
                fieldJson.add(field.getName(), getValuesFromArray(value));
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

    private JsonArrayBuilder getValuesFromArray(Object obj){
        var arrayBuilder = Json.createArrayBuilder();
        var arrayClass = obj.getClass();
        var elType = arrayClass.getComponentType();
        if (elType.isPrimitive()){
            if (elType.equals(int.class)){
                var array = (int[]) obj;
                for (int element: array) {
                    arrayBuilder.add(element);
                }
            } else if (elType.equals(byte.class)){
                var array = (byte[]) obj;
                for (byte element: array) {
                    arrayBuilder.add(element);
                }
            } else if (elType.equals(short.class)){
                var array = (short[]) obj;
                for (short element: array) {
                    arrayBuilder.add(element);
                }
            } else if (elType.equals(long.class)){
                var array = (long[]) obj;
                for (long element: array) {
                    arrayBuilder.add(element);
                }
            } else if (elType.equals(boolean.class)){
                var array = (boolean[]) obj;
                for (boolean element: array) {
                    arrayBuilder.add(element);
                }
            } else if (elType.equals(char.class)){
                var array = (char[]) obj;
                for (char element: array) {
                    arrayBuilder.add(String.valueOf(element));
                }
            }
        } else {
            var array = (Object[]) obj;
            for (int i = 0; i < array.length; i++) {
                parseArrayElement(array[i], arrayBuilder);
            }
        }
        return arrayBuilder;
    }

    private JsonArrayBuilder getValuesFromList(List<?> list) {
        var arrayJson = Json.createArrayBuilder();
        for (Object element : list) {
            parseArrayElement(element, arrayJson);
        }
        return arrayJson;
    }

    private JsonArrayBuilder getValuesFromSet(Set<?> set) {
        var arrayJson = Json.createArrayBuilder();
        for (Object element : set) {
            parseArrayElement(element, arrayJson);
        }
        return arrayJson;
    }

    private JsonObjectBuilder getValuesFromMap(Map<?,?> map) {
        var mapJson = Json.createObjectBuilder();
        map.forEach((k,v) -> {
            if ( isObjectSimple(k) || isObjectLiteral(k) ) {
                if ( isObjectSimple(v) || isObjectLiteral(v) ) {
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

    private void parseArrayElement(Object element, JsonArrayBuilder arrayBuilder) {
        if (element == null) {
            arrayBuilder.addNull();
        } else if ( isObjectLiteral(element) ) {
            arrayBuilder.add(element.toString());
        } else if ( isObjectSimple(element)) {
            addSimpleElementToArrayBuilder(element, arrayBuilder);
        } else {
            arrayBuilder.add(parseObject(element));
        }
    }

    private void addSimpleElementToArrayBuilder(Object element, JsonArrayBuilder arrayBuilder) {
        var objClass = element.getClass();
        if  (objClass.equals(Integer.class)) {
            arrayBuilder.add((Integer) element);
        } else if (objClass.equals(Long.class)) {
            arrayBuilder.add((Long) element);
        } else if (objClass.equals(Short.class)) {
            arrayBuilder.add((Short) element);
        } else if (objClass.equals(Byte.class)) {
            arrayBuilder.add((Byte) element);
        } else if (objClass.equals(Float.class)) {
            arrayBuilder.add((Float) element);
        } else if (objClass.equals(Double.class)) {
            arrayBuilder.add((Double) element);
        } else if (objClass.equals(Boolean.class)) {
            arrayBuilder.add((Boolean) element);
        }
    }

    private boolean isObjectSimple (Object obj) {
        var objClass = obj.getClass();
        return (objClass.isPrimitive() ) ||
                (objClass.equals(Integer.class)) ||
                (objClass.equals(Long.class)) ||
                (objClass.equals(Short.class)) ||
                (objClass.equals(Byte.class)) ||
                (objClass.equals(Float.class)) ||
                (objClass.equals(Double.class)) ||
                (objClass.equals(Boolean.class));
    }

    private boolean isObjectLiteral (Object obj) {
        var objClass = obj.getClass();
        return (objClass.equals(String.class)) ||
                (objClass.equals(Character.class));
    }

}