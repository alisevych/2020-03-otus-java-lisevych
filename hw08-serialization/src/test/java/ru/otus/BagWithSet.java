package ru.otus;

import java.util.Objects;
import java.util.Set;

public class BagWithSet {
  private final int value1;
  private final Set<Integer> set;
  private final String value3;

  BagWithSet(int value1, Set<Integer> set, String value3) {
    this.value1 = value1;
    this.set = set;
    this.value3 = value3;
  }

  @Override
  public String toString() {
    return "BagWithMap{" +
        "value1=" + value1 +
        ", set=" + set +
        ", value3=" + value3 +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    var that = (BagWithSet) o;
    return value1 == that.value1 &&
            value3.equals(that.value3) &&
            this.set.containsAll(that.set) &&
            that.set.containsAll(this.set);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value1, set, value3);
  }
}
