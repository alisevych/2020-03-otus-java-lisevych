package ru.otus;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class BagWithMapOfBags {
  private final int value1;
  private final Map<String, BagOfPrimitives> map;
  private final String value3;

  BagWithMapOfBags(int value1, Map<String, BagOfPrimitives> map, String value3) {
    this.value1 = value1;
    this.map = map;
    this.value3 = value3;
  }

  @Override
  public String toString() {
    return "BagWithMapOfBags{" +
        "value1=" + value1 +
        ", map='" + map +
        "', value3=" + value3 +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    var that = (BagWithMapOfBags) o;
    Set<Map.Entry<String , BagOfPrimitives>> mapEntries = map.entrySet();
    Set<Map.Entry<String , BagOfPrimitives>> mapEntries2 = that.map.entrySet();
    return value1 == that.value1 &&
            value3.equals(that.value3) &&
            mapEntries.containsAll(mapEntries2) &&
            mapEntries2.containsAll(mapEntries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value1, map, value3);
  }
}
