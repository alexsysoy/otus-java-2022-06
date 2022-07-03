package ru.otus;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

/**
 * first homework assignment solution
 */
public class HelloOtus {
    public static void main(String[] args) {
        System.out.println("first homework assignment solution");
        ImmutableSet<String> set = ImmutableSet.<String>builder()
                .add("first")
                .add("homework")
                .add("assignment")
                .add("solution")
                .build();

        Set<List<String>> lists = Sets.cartesianProduct(set, set);
        lists.forEach(System.out::println);
    }
}
