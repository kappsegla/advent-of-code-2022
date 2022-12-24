package org.fungover.day24;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day24 {

    static int TOP_WALL = 0;
    static int LEFT_WALL = 0;
    static int RIGHT_WALL = 0;
    static int BOTTOM_WALL = 0;

    static Point startpos = new Point(0, 1);
    static Point endpos = new Point(0, 1);
    static List<Wind> winds = new ArrayList<>();

    public static void main(String[] args) {
       String s = stringFromFile(resourceStringToPath("/day24/day24.txt"));
//        String s = """
//                #.######
//                #>>.<^<#
//                #.<..<<#
//                #>v.><>#
//                #<^v^^>#
//                ######.#
//                """;


        //Possible directions
        Point down = new Point(1, 0);
        Point up = new Point(-1, 0);
        Point right = new Point(0, 1);
        Point left = new Point(0, -1);

        int x = 0;
        int y = 0;
        for (var line : s.lines().toList()) {
            for (var t : line.split("")) {
                switch (t) {
                    case ">" -> winds.add(new Wind(new Point(x, y), right));
                    case "<" -> winds.add(new Wind(new Point(x, y), left));
                    case "v" -> winds.add(new Wind(new Point(x, y), down));
                    case "^" -> winds.add(new Wind(new Point(x, y), up));
                }
                y++;
            }
            endpos = new Point(x, y - 2);
            BOTTOM_WALL = x;
            RIGHT_WALL = y - 1;
            x++;
            y = 0;
        }
        //Simulate
        int totalTime = 0;
        int time = move(startpos, endpos);
        System.out.println(time);
        totalTime += time;
        time = move(endpos, startpos);
        System.out.println(time);
        totalTime += time;
        time = move(startpos, endpos);
        System.out.println(time);
        totalTime += time;
        System.out.println(totalTime);
    }

    private static int move(Point startpos, Point endpos) {
        Set<Point> currentPossible = new HashSet<>();
        currentPossible.add(startpos);
        int currentMinute = 0;
        while (!currentPossible.isEmpty()) {
            if (currentPossible.contains(endpos)) {
                break;
            }
            List<Wind> nextWinds = nextWindMap(winds);
            var nextMap = mapWindToPoint(nextWinds);
            Set<Point> newPossible = new HashSet<>();
            for (var position : currentPossible) {
                var neighbors = getNeighbours(position, nextMap);
                newPossible.addAll(neighbors);
                if (!nextMap.contains(position)) {
                    newPossible.add(position);
                }
            }
            currentPossible = newPossible;
            winds = nextWinds;
            currentMinute++;
        }
        return currentMinute;
    }

    private static List<Point> mapWindToPoint(List<Wind> winds) {
        var next = new ArrayList<Point>();
        for (var wind : winds) {
            next.add(wind.p());
        }
        //printGrid(next);
        return next;
    }

    private static List<Point> getNeighbours(Point position, List<Point> points) {
        return Stream.of(new Point(position.x() + 1, position.y()),
                        new Point(position.x() - 1, position.y()),
                        new Point(position.x(), position.y() + 1),
                        new Point(position.x(), position.y() - 1))
                .filter(p ->
                        validPoint(p) && !points.contains(p)
                ).toList();

    }

    static boolean validPoint(Point point) {
        if (point.equals(endpos)|| point.equals(startpos))
            return true;
        return point.x() > TOP_WALL && point.x() < BOTTOM_WALL && point.y() > LEFT_WALL && point.y() < RIGHT_WALL;
    }

    static List<Wind> nextWindMap(List<Wind> currentWinds) {
        List<Wind> nextWinds = new ArrayList<>();
        for (var wind : currentWinds) {
            Wind wind1 = wind.move();
            if (wind1.p().x() == TOP_WALL)
                wind1 = new Wind(new Point(BOTTOM_WALL - 1, wind1.p().y()), wind1.dir());
            else if (wind1.p().x() == BOTTOM_WALL)
                wind1 = new Wind(new Point(1, wind1.p().y()), wind1.dir());
            else if (wind1.p().y() == LEFT_WALL)
                wind1 = new Wind(new Point(wind1.p().x(), RIGHT_WALL - 1), wind1.dir());
            else if (wind1.p().y() == RIGHT_WALL)
                wind1 = new Wind(new Point(wind1.p().x(), 1), wind1.dir());

            nextWinds.add(wind1);
        }
        return nextWinds;
    }

    static void printGrid(List<Point> windMap) {
        for (int i = 0; i <= BOTTOM_WALL; i++) {
            for (int j = 0; j <= RIGHT_WALL; j++) {
                if (i == 0 || j == 0 || i == BOTTOM_WALL || j == RIGHT_WALL)
                    System.out.print("#");
                else if (windMap.contains(new Point(i, j)))
                    System.out.print("w");
                else
                    System.out.print(".");
            }
            System.out.println();
        }
        System.out.println();
    }
}

record Point(int x, int y) {
}

record Wind(Point p, Point dir) {

    public Wind move() {
        return new Wind(new Point(p.x() + dir().x(), p.y() + dir.y()), dir);
    }
}