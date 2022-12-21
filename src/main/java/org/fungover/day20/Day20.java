package org.fungover.day20;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day20 {
    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day20/day20.txt"));
//        String s = """
//                1
//                2
//                -3
//                3
//                -2
//                0
//                4
//                """;
        //Step1
        var numbers = s.lines().map(Long::parseLong).toList();
        List<TrackedNumber> result = decrypt(numbers, 1);
        System.out.println(groveCoordinates(result));

        //Step2
        numbers = s.lines().mapToLong(Long::parseLong).map(v -> v * 811589153).boxed().toList();
        result = decrypt(numbers, 10);
        System.out.println(groveCoordinates(result));
    }

    private static List<TrackedNumber> decrypt(List<Long> originalList, int mixCount) {
        List<TrackedNumber> mixed = new ArrayList<>();
        for (int i = 0; i < originalList.size(); i++) {
            mixed.add(new TrackedNumber(i, originalList.get(i)));
        }

        for (int m = 0; m < mixCount; m++) {
            for (int i = 0; i < originalList.size(); i++) {
                final TrackedNumber node = new TrackedNumber(i, originalList.get(i));
                long currentIndex = mixed.indexOf(node);
                mixed.remove(node);
                currentIndex += node.value();
                currentIndex = modulo(currentIndex, originalList.size());
                mixed.add((int) currentIndex, node);
            }
        }
        return mixed;
    }

    private static long modulo(long index, int size) {
        final int denominator = size - 1;
        long modulo = index % denominator;
        if (modulo < 0) {
            modulo += denominator;
        }
        return modulo;

    }

    private static long groveCoordinates(List<TrackedNumber> list) {
        var zero = findZero(list);
        return Stream.of(1000, 2000, 3000)
                .map(i -> list.get((zero + i) % list.size()))
                .mapToLong(TrackedNumber::value)
                .sum();
    }

    private static int findZero(List<TrackedNumber> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).value() == 0)
                return i;
        }
        throw new IllegalStateException();
    }
}


record TrackedNumber(int originalIndex, long value) {
}