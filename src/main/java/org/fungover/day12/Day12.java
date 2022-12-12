package org.fungover.day12;

import org.fungover.util.Strings;

import java.util.*;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day12 {

    static char[][] array;

    public static void main(String[] args) {
            String s = stringFromFile(resourceStringToPath("/day12/day12.txt")); //528 //522
//        String s = """
//                Sabqponm
//                abcryxxl
//                accszExk
//                acctuvwj
//                abdefghi
//                """; //31 //29

        array = Strings.stringValuesTo2DCharArray(s);

        Point start = null;
        Point end = null;

        //Find Start and End
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (array[i][j] == 'S') {
                    start = new Point(i, j);
                    array[i][j] = 'a';
                }
                if (array[i][j] == 'E') {
                    end = new Point(i, j);
                    array[i][j] = 'z';
                }
            }
        }

        Map<Point, Integer> shortestPath = new HashMap<>();
        shortestPath.put(start, 0);
        List<Point> queue = new ArrayList<>();
        queue.add(start);
        while (queue.size() > 0) {
            Point p = queue.remove(0);

            if (p.x() != 0)
                checkPoint(p, new Point(p.x() - 1, p.y()), shortestPath, queue);
            if (p.x() != array.length - 1)
                checkPoint(p, new Point(p.x() + 1, p.y()), shortestPath, queue);
            if (p.y() != 0)
                checkPoint(p, new Point(p.x(), p.y() - 1), shortestPath, queue);
            if (p.y() != array[0].length - 1)
                checkPoint(p, new Point(p.x(), p.y() + 1), shortestPath, queue);
        }
        System.out.println(shortestPath.get(end));
    }

    public static void checkPoint(Point current, Point next, Map<Point, Integer> shortestPath, List<Point> queue) {
        var part1 = true;
        int height = getHeight(next.x(),next.y());
        if (height - getHeight(current.x(),current.y()) <= 1) {
            int pathLength = shortestPath.get(current) + 1;
            if (shortestPath.getOrDefault(next, Integer.MAX_VALUE) > pathLength) {
                queue.add(next);
                shortestPath.put(next, !part1 && height == 0 ? 0 : pathLength);
            }
        }
    }

    private static int getHeight(int x, int y) {
        char value = array[x][y];
        if( value == 'S')
            value = 'a';
        if( value == 'E')
            value = 'z';
        return value - 'a';
    }
}

record Point(int x, int y) {
}
