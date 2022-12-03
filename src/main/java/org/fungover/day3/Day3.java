package org.fungover.day3;

import org.fungover.util.StreamUtils;

import java.util.ArrayList;
import java.util.List;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day3 {
    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day3/day3.txt"));
//        String s = """
//                vJrwpWtwJgWrhcsFMMfFFhFp
//                jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
//                PmmdzqPrVvPwwTWBwg
//                wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
//                ttgJtRGJQctTZtZT
//                CrZsJsPPZsGzwwsLwLmpwMDw""";

        System.out.println((int)'a');
        System.out.println((int)'A');
        step1(s);
        step2(s);
    }

    private static void step1(String s) {
        var result = s.lines().map(l -> {
            int middle = l.length() / 2;
            return new Tuple(l.substring(0, l.length() / 2), l.substring(l.length() / 2));
        }).toList();

        var total = result.stream().mapToInt(Day3::commonCharToInt).sum();
        System.out.println(total);
    }

    private static int commonCharToInt(Tuple tuple) {
        for (int c : tuple.c1().chars().toArray()) {
            char k = (char) c;
            if (tuple.c2().contains(String.valueOf(k))) {
                if (Character.isLowerCase(k)) return k - 'a' + 1;
                if (Character.isUpperCase(k)) return k - 'A' + 27;
            }
        }
        throw new RuntimeException("No common character found");
    }

    private static void step2(String s) {
        var result = s.lines().toList();

        var grouped = StreamUtils.createRows(result, 3);

        System.out.println(grouped.stream().mapToInt(Day3::commonCharInStrings).sum());
    }

    private static int commonCharInStrings(List<String> strings) {
        for (int c : strings.get(0).chars().toArray()) {
            char k = (char) c;
            if (strings.get(1).contains(String.valueOf(k)) && strings.get(2).contains(String.valueOf(k))) {
                if (Character.isLowerCase(k)) return k - 'a' + 1;
                if (Character.isUpperCase(k)) return k - 'A' + 27;
            }
        }
        throw new RuntimeException("No common character found");
    }
}

record Tuple(String c1, String c2) {
}
