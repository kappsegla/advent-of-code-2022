package org.fungover.day1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day1 {
    public static void main(String[] args) {
//        String s = stringFromFile(resourceStringToPath("/day1/day1.txt"));
        String s = """
                1000
                2000
                3000

                4000

                5000
                6000

                7000
                8000
                9000

                10000""";

        List<Integer> caloriesPerElf = new ArrayList<>();
        int total = 0;
        for (String line : s.lines().toList()) {
            if (line.isEmpty()) {
                caloriesPerElf.add(total);
                total = 0;
                continue;
            }
            total += Integer.parseInt(line);
        }
        if( total > 0)
            caloriesPerElf.add(total);

        int index = IntStream.range(0, caloriesPerElf.size()).boxed().max(Comparator.comparing(caloriesPerElf::get)).orElse(-1);

        System.out.println(caloriesPerElf);
        System.out.println(index + 1);
        System.out.println(caloriesPerElf.get(index));

        var topThreeIndex = IntStream.range(0, caloriesPerElf.size())
                .boxed()
                .sorted(Comparator.comparing(caloriesPerElf::get).reversed()).limit(3)
                .toList();

        System.out.println(topThreeIndex);
        System.out.println("Sum: "+topThreeIndex.stream().mapToInt(caloriesPerElf::get).sum());

        for( var i  : topThreeIndex) {
            System.out.println(caloriesPerElf.get(i));
        }
    }
}
