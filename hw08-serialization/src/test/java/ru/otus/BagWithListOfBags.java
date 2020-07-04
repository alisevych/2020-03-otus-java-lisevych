package ru.otus;

import java.util.List;
import java.util.Objects;

public class BagWithListOfBags {
  private final int value1;
  private final List<BagOfPrimitives> bagList;
  private final String value3;

  BagWithListOfBags(int value1, List<BagOfPrimitives> bagListIni, String value3) {
    this.value1 = value1;
    this.bagList = bagListIni;
    this.value3 = value3;
  }

  @Override
  public String toString() {
    return "BagWithList{" +
        "value1=" + value1 +
        ", bagsList='" + bagList +
        ", value3=" + value3 +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    var that = (BagWithListOfBags) o;
    return value1 == that.value1 &&
            value3.equals(that.value3) &&
            bagList.containsAll(that.bagList) &&
            that.bagList.containsAll(bagList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value1, bagList, value3);
  }
}
