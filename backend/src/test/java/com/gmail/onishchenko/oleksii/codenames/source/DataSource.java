package com.gmail.onishchenko.oleksii.codenames.source;

import com.gmail.onishchenko.oleksii.codenames.entity.Role;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public interface DataSource {
    static LongStream longs() {
        return ThreadLocalRandom.current().longs(5, 1, Long.MAX_VALUE);
    }

    static IntStream integers() {
        return ThreadLocalRandom.current().ints(5, 1, Integer.MAX_VALUE);
    }

    static Stream<String> strings() {
        return Stream.of(
                "first",
                "second",
                "value",
                "Awesome String"
        );
    }

    static Stream<Role> roles() {
        return Stream.of(Role.CIVILIAN, Role.RED, Role.BLUE, Role.KILLER);
    }
}
