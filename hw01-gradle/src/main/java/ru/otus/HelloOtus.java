package ru.otus;

import com.google.common.collect.Lists;
import com.google.common.math.BigIntegerMath;

import java.util.ArrayList;
import java.util.List;

public class HelloOtus {

    public static void main(String... args) {
        List<Integer> example = new ArrayList<>();
        int min = 0;
        int max = 20;
        for (int i = min; i < max; i++) {
            example.add(i);
        }

        System.out.format("Factorials:\n");
        for (int item: Lists.reverse(example)) {
            System.out.format("%5d  -  %20d%n", item, BigIntegerMath.factorial(item));
        }
    }
}
