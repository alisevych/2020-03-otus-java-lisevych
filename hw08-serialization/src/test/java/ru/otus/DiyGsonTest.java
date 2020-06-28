package ru.otus;

import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("DIY Gson.toJson() tests")
public class DiyGsonTest {

    /** Uncomment 1 Gson initializing */
    // Gson myGson = new Gson();
    DiyGson myGson = new DiyGsonImpl();

    @DisplayName("Primitives save to Json")
    @Test
    public void primitivesToJson() {
        System.out.println("======== test ===================");
        var obj = new BagOfPrimitives(22, "test", 10);
        String myJson = myGson.toJson(obj);
        System.out.println(myJson);
        var obj2 = new Gson().fromJson(myJson, BagOfPrimitives.class);
        assertEquals(obj, obj2);
        System.out.println("================================");
    }

    @DisplayName("Array and primitives save to Json")
    @Test
    public void arrayToJson() {
        System.out.println("======== test ===================");
        var obj = new BagWithArray(25, new String[]{"one", "two"}, "three");
        String myJson = myGson.toJson(obj);
        System.out.println(myJson);
        var obj2 = new Gson().fromJson(myJson, BagWithArray.class);
        assertEquals(obj, obj2);
        System.out.println("================================");
    }

    @DisplayName("List and primitives save to Json")
    @Test
    public void listToJson() {
        System.out.println("======== test ===================");
        var obj = new BagWithList(25, Arrays.asList("only","you"), "three");
        String myJson = myGson.toJson(obj);
        System.out.println(myJson);
        var obj2 = new Gson().fromJson(myJson, BagWithList.class);
        assertEquals(obj, obj2);
        System.out.println("================================");
    }

    @DisplayName("Map and primitives save to Json")
    @Test
    public void mapToJson() {
        System.out.println("======== test ===================");
        var map = new TreeMap<String, String>();
        map.put("Dec", "December");
        map.put("Jan", "January");
        map.put("Jun", "June");
        var obj = new BagWithMap(25, map, "three");
        String myJson = myGson.toJson(obj);
        System.out.println(myJson);
        var obj2 = new Gson().fromJson(myJson, BagWithMap.class);
        assertEquals(obj, obj2);
        System.out.println("================================");
    }

    @DisplayName("Two Maps and primitives save to Json")
    @Test
    public void twoMapsToJson() {
        System.out.println("======== test ===================");
        var map1 = new TreeMap<String, String>();
        map1.put("Rose", "Park");
        map1.put("Lavender", "Mountains");
        var map2 = new TreeMap<String, Integer>();
        map2.put("Rose", 65);
        map2.put("Lavender", 18);
        var obj = new BagWithTwoMaps(25, map1, map2, "12");
        String myJson = myGson.toJson(obj);
        System.out.println(myJson);
        var obj2 = new Gson().fromJson(myJson, BagWithTwoMaps.class);
        assertEquals(obj, obj2);
        System.out.println("================================");
    }

    @DisplayName("Null save to Json")
    @Test
    public void nullToJson() {
        System.out.println("======== test ===================");
        String myJson = myGson.toJson(null);
        System.out.println(myJson);
        var obj2 = new Gson().fromJson(myJson, BagOfPrimitives.class);
        assertEquals(null, obj2);
        System.out.println("================================");
    }

    @DisplayName("Empty String save to Json")
    @Test
    public void emptyStringToJson() {
        System.out.println("======== test ===================");
        var obj = new BagOfPrimitives(22, "", 10);
        String myJson = myGson.toJson(obj);
        System.out.println(myJson);
        var obj2 = new Gson().fromJson(myJson, BagOfPrimitives.class);
        assertEquals(obj, obj2);
        System.out.println("================================");
    }

    @DisplayName("Zero length array save to Json")
    @Test
    public void zeroLengthArrayToJson() {
        System.out.println("======== test ===================");
        var obj = new BagWithArray(25, new String[0], "three");
        String myJson = myGson.toJson(obj);
        System.out.println(myJson);
        var obj2 = new Gson().fromJson(myJson, BagWithArray.class);
        assertEquals(obj, obj2);
        System.out.println("================================");
    }

    @DisplayName("Array with nulls save to Json")
    @Test
    public void emptyArrayToJson() {
        System.out.println("======== test ===================");
        var obj = new BagWithArray(25, new String[5], "three");
        String myJson = myGson.toJson(obj);
        System.out.println(myJson);
        var obj2 = new Gson().fromJson(myJson, BagWithArray.class);
        assertEquals(obj, obj2);
        System.out.println("================================");
    }

    @DisplayName("Empty List save to Json")
    @Test
    public void emptyListToJson() {
        System.out.println("======== test ===================");
        var obj = new BagWithList(22, new ArrayList<>(), "three");
        String myJson = myGson.toJson(obj);
        System.out.println(myJson);
        var obj2 = new Gson().fromJson(myJson, BagWithList.class);
        assertEquals(obj, obj2);
        System.out.println("================================");
    }

    @DisplayName("Empty Map save to Json")
    @Test
    public void emptyMapToJson() {
        System.out.println("======== test ===================");
        var obj = new BagWithMap(22, new HashMap<>(), "three");
        String myJson = myGson.toJson(obj);
        System.out.println(myJson);
        var obj2 = new Gson().fromJson(myJson, BagWithMap.class);
        assertEquals(obj, obj2);
        System.out.println("================================");
    }

    @DisplayName("Empty Map and Not empty Map save to Json")
    @Test
    public void emptyAndNotEmptyMapsToJson() {
        System.out.println("======== test ===================");
        var map2 = new TreeMap<String, Integer>();
        map2.put("Ankara", 75);
        map2.put("Osaka", 189);
        var obj = new BagWithTwoMaps(22, new HashMap<>(), map2, "three");
        String myJson = myGson.toJson(obj);
        System.out.println(myJson);
        var obj2 = new Gson().fromJson(myJson, BagWithTwoMaps.class);
        assertEquals(obj, obj2);
        System.out.println("================================");
    }

    @DisplayName("Map of BagOfPrimitives to Json")
    @Test
    public void mapOfBagOfPrimitivesToJson() {
        System.out.println("======== test ===================");
        var map = new TreeMap<String, BagOfPrimitives>();
        map.put("BagWithEleven", new BagOfPrimitives(11, "eleven", 11));
        map.put("BagWithThirteen", new BagOfPrimitives(13, "thirteen", 13));
        var obj = new BagWithMapOfBags(24, map, "twenty four");
        String myJson = myGson.toJson(obj);
        System.out.println(myJson);
        var obj2 = new Gson().fromJson(myJson, BagWithMapOfBags.class);
        assertEquals(obj, obj2);
        System.out.println("================================");
    }

}