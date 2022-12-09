package org.fungover.day9;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day9 {

    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day9/day9.txt"));
        //6067
        //2471

//        String s = """
//                R 4
//                U 4
//                L 3
//                D 1
//                R 4
//                D 1
//                L 5
//                R 2
//                """; //13
//        String s = """
//                R 5
//                U 8
//                L 8
//                D 3
//                R 17
//                D 10
//                L 25
//                U 20
//                """;

        step1(s);
        step2(s);
    }

    private static void step2(String s) {
        Set<Position> visitedPositions = new HashSet<>();
        Position[] rope = new Position[10];
        Arrays.fill(rope, new Position(0, 0));
        visitedPositions.add(rope[9]);

        for (String l : s.lines().toList()) {
            var direction = l.split("\\s")[0];
            var steps = Integer.parseInt(l.split("\\s")[1]);

            for (int i = 0; i < steps; i++) {
                rope[0] = move(rope[0], direction);

                //Follow head
                for (int j = 0; j < 9; j++) {
                    rope[j + 1] = rope[j + 1].followHead(rope[j]);
                }
                visitedPositions.add(rope[9]);
            }
        }
        System.out.println(visitedPositions.size());
    }

    private static void step1(String s) {
        Set<Position> visitedPositions = new HashSet<>();
        var head = new Position(0, 0);
        var tail = new Position(0, 0);
        for (String l : s.lines().toList()) {

            var dir = l.split("\\s")[0];
            var count = Integer.parseInt(l.split("\\s")[1]);

            for (int i = 0; i < count; i++) {
                head = move(head, dir);

                //Follow head
                tail = tail.followHead(head);

                visitedPositions.add(tail);
            }
        }
        System.out.println(visitedPositions.size());
    }

    private static Position move(Position head, String dir) {
        switch (dir) {
            case "R" -> head = new Position(head.x(), head.y() + 1);
            case "U" -> head = new Position(head.x() - 1, head.y());
            case "L" -> head = new Position(head.x(), head.y() - 1);
            case "D" -> head = new Position(head.x() + 1, head.y());
        }
        return head;
    }
}

record Position(int x, int y) {

    public Position followHead(Position head) {
        var shouldMove = Math.abs(x - head.x) > 1 || Math.abs(y - head.y) > 1;

        if (!shouldMove) return this;

        var dx = 0;
        var dy = 0;

        if (x > head.x) {
            dx--;
        } else if (x < head.x) {
            dx++;
        }
        if (y > head.y) {
            dy--;
        } else if (y < head.y) {
            dy++;
        }

        return new Position(x + dx, y + dy);
    }
}
