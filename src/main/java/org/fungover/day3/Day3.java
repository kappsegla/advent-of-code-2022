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

        step1(s);
        step2(s);
    }

    private static void step1(String s) {
        var result = s.lines().map(l -> {
            int middle = l.length() / 2;
            return new Tuple(l.substring(0, l.length() / 2), l.substring(l.length() / 2));
        }).toList();

        List<Integer> total = new ArrayList<>();
        for (var t : result) {
            for (int c : t.c1().chars().toArray()) {
                char k = (char) c;
                if (t.c2().contains(String.valueOf(k))) {
                    if (Character.isLowerCase(k)) {
                        System.out.println(k - 'a' + 1);
                        total.add(k - 'a' + 1);
                    }
                    if (Character.isUpperCase(k)) {
                        System.out.println(k - 'A' + 27);
                        total.add(k - 'A' + 27);
                    }
                    break;
                }
            }
        }
        System.out.println(total.stream().mapToInt(Integer::intValue).sum());
    }

    private static void step2(String s) {
        var result = s.lines().toList();

        var grouped = StreamUtils.createRows(result, 3);

        List<Integer> total = new ArrayList<>();
        for (var t : grouped) {
            for (int c : t.get(0).chars().toArray()) {
                char k = (char) c;
                if (t.get(1).contains(String.valueOf(k)) && t.get(2).contains(String.valueOf(k))) {
                    if (Character.isLowerCase(k)) {
                        System.out.println(k - 'a' + 1);
                        total.add(k - 'a' + 1);
                    }
                    if (Character.isUpperCase(k)) {
                        System.out.println(k - 'A' + 27);
                        total.add(k - 'A' + 27);
                    }
                    break;
                }
            }
        }
        System.out.println(total.stream().mapToInt(Integer::intValue).sum());
    }


}

record Tuple(String c1, String c2) {
}
