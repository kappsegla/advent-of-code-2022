package org.fungover.day10;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day10 {
    public static void main(String[] args) {

        String s = stringFromFile(resourceStringToPath("/day10/day10.txt"));
//        String s = """
//                [({(<(())[]>[[{[]{<()<>>
//                [(()[<>])]({[<{<<[]>>(
//                {([(<{}[<>[]}>{[]{[(<()>
//                (((({<>}<{<{<>}{[]{[]{}
//                [[<[([]))<([[{}[[()]]]
//                [{[{({}]{}}([{[{{{}}([]
//                {<[[]]>}<{[{[{[]{()[[[]
//                [<(<(<(<{}))><([]([]()
//                <{([([[(<>()){}]>(<<{{
//                <{([{{}}[<[[[<>{}]]]>[]]""";

        var sum = s.lines().map(Day10::findFirstCorruptedSymbol).mapToInt(Day10::syntaxPoints).sum();

        System.out.println(sum);

        var values  = s.lines()
                .map(Day10::incompleteLines)
                .mapToLong(Day10::toScore)
                .filter( v-> v > 0)
                .sorted().toArray();

        System.out.println(values[values.length/2]);
    }

    private static long toScore(Deque<String> strings) {
        return strings.stream()
                .map(Day10::opposite)
                .map(Day10::syntaxPointsAutoComplete)
                .reduce(0L, (first, second) -> first * 5L + second);
    }

    private static Deque<String> incompleteLines(String s) {
        Deque<String> queue = new ArrayDeque<>();

        for (String c : s.split("")) {
            if (c.matches("[({\\[<]"))
                queue.push(c);
            else if (queue.size() != 0 && opposite(queue.peek()).equals(c))
                queue.pop();
            else {
                queue.clear();
                return queue;
            }
        }
        if( queue.size() > 0)
            return queue;
        throw new RuntimeException();
    }

    private static String findFirstCorruptedSymbol(String s) {
        Deque<String> queue = new ArrayDeque<>();

        for (String c : s.split("")) {
            if (c.matches("[({\\[<]"))
                queue.push(c);
            else if (queue.size() != 0 && opposite(queue.peek()).equals(c))
                queue.pop();
            else
                return c;
        }
        return "";
    }

    private static String opposite(String value) {
        return switch (value) {
            case "(" -> ")";
            case "[" -> "]";
            case "{" -> "}";
            case "<" -> ">";
            default -> throw new RuntimeException();
        };
    }

    private static int syntaxPoints(String value) {
        return switch (value) {
            case ")" -> 3;
            case "]" -> 57;
            case "}" -> 1197;
            case ">" -> 25137;
            default -> 0;
        };
    }
    private static long syntaxPointsAutoComplete(String value) {
        return switch (value) {
            case ")" -> 1L;
            case "]" -> 2L;
            case "}" -> 3L;
            case ">" -> 4L;
            default -> 0L;
        };
    }
}

