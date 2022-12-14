package org.fungover.day14;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day14 {
    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day14/day14.txt"));

//        String s = """
//                498,4 -> 498,6 -> 496,6
//                503,4 -> 502,4 -> 502,9 -> 494,9
//                """;//24

        var lines = s.lines().toList();
        var pairs = lines.stream().map(v -> v.split("\\s->\\s"))
                .map(a -> Arrays.stream(a)
                        .map(i -> Arrays.stream(i.split(","))
                                .mapToInt(Integer::parseInt).toArray())
                        .toList())
                .toList();

        //Find max values
        var maxWidth = pairs.stream()
                .flatMap(Collection::stream)
                .mapToInt(k -> k[0])
                .max().getAsInt();

        var maxHeight = pairs.stream()
                .flatMap(Collection::stream)
                .mapToInt(k -> k[1])
                .max().getAsInt();

        var grid = new Grid(new int[maxHeight + 1][maxWidth + 1]);

        pairs.stream().forEach(l -> fillGrid(l, grid));

        int sandUnits = 0;
        boolean overflow = false;
        int sandX = 500;
        int sandY = 0;
        while (sandY + 1 <= grid.area().length - 1 && sandX - 1 >= 0  && sandX + 1 < grid.area()[0].length - 1) {
            //Try to fall down
            if (grid.down(sandX, sandY))
                sandY++;
                //Try to fall diagonally left
            else if (grid.downLeft(sandX, sandY)) {
                sandY++;
                sandX--;
            }
            //Try to fall diagonally right
            else if (grid.downRight(sandX, sandY)) {
                sandY++;
                sandX++;

            } else {
                //Come to rest
                grid.area()[sandY][sandX] = 2;
                sandY = 0;
                sandX = 500;
                sandUnits++;
            }
        }

        for (int j = 0; j < grid.area().length; j++) {
            for (int i = 0; i < grid.area()[0].length; i++) {
                System.out.print(grid.area()[j][i]);
            }
            System.out.println();
        }

        System.out.println(sandUnits);
    }

    private static void fillGrid(List<int[]> l, Grid grid) {
        for (int i = 0; i < l.size() - 1; i++) {
            grid.drawLine(l.get(i), l.get(i + 1));
        }
    }
}

record Grid(int[][] area) {

    public void drawLine(int[] start, int[] end) {
        var xDelta = Integer.signum(end[0] - start[0]);
        var yDelta = Integer.signum(end[1] - start[1]);
        var steps = Math.max(Math.abs(end[1] - start[1]), Math.abs(end[0] - start[0]));
        for (int i = 0; i <= steps; i++) {
            area[start[1] + i * yDelta][start[0] + i * xDelta] = 1;
        }
    }

    public boolean down(int x, int y) {
        if (y >= area.length - 1)
            return false;
        return area[y + 1][x] == 0;
    }

    public boolean downLeft(int x, int y) {
        if (y >= area.length - 1 || x <= 0)
            return false;
        return area[y + 1][x - 1] == 0;
    }

    public boolean downRight(int x, int y) {
        if (y >= area.length - 1 || x >= area[0].length - 1)
            return false;
        return area[y + 1][x + 1] == 0;
    }
}
