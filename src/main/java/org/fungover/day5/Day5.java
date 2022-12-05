package org.fungover.day5;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day5 {

    public static List<Deque<String>> stacks = new ArrayList<>();

    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day5/day5.txt"));
//        String s = """
//                    [D]
//                [N] [C]
//                [Z] [M] [P]
//                 1   2   3
//
//                move 1 from 2 to 1
//                move 3 from 1 to 3
//                move 2 from 2 to 1
//                move 1 from 1 to 2
//                """;
        var parts = s.split("\\r?\\n\\r?\\n");

        //Step1 ZSQVCCJLL
        fillStacks(parts[0]);
        parts[1].lines().forEach(Day5::crateMover9000);
        stacks.forEach(stack -> System.out.print(stack.peek()));
        stacks.clear();
        System.out.println();
        //Step2  QZFJRWHGS
        fillStacks(parts[0]);
        parts[1].lines().forEach(Day5::crateMover9001);
        stacks.forEach(stack -> System.out.print(stack.peek()));
    }

    private static void fillStacks(String part) {
        for (var line : part.lines().toList()) {
            if (!line.contains("["))
                break;
            int pos = 0;
            while (pos * 4 < line.length()) {
                if (stacks.size() < pos + 1)
                    stacks.add(new ArrayDeque<>());
                var crateName = String.valueOf(line.charAt(pos * 4 + 1));
                if (crateName.charAt(0) != ' ')
                    stacks.get(pos).addLast(crateName);
                pos++;
            }
        }
    }

    public static void crateMover9000(String instruction) {
        var i = instruction.split(" ");
        var count = Integer.parseInt(i[1]);
        var from = Integer.parseInt(i[3]) - 1;
        var to = Integer.parseInt(i[5]) - 1;

        for (int j = 0; j < count; j++) {
            stacks.get(to).push(stacks.get(from).pop());
        }
    }

    public static void crateMover9001(String instruction) {
        var i = instruction.split(" ");
        var count = Integer.parseInt(i[1]);
        var from = Integer.parseInt(i[3]) - 1;
        var to = Integer.parseInt(i[5]) - 1;

        Deque<String> tempStorage = new ArrayDeque<>();
        for (int j = 0; j < count; j++) {
            tempStorage.push(stacks.get(from).pop());
        }
        tempStorage.forEach(stacks.get(to)::push);
    }
}
