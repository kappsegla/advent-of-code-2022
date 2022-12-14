package org.fungover.day14;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day14Step2 {
    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day14/day14.txt"));

//        String s = """
//                498,4 -> 498,6 -> 496,6
//                503,4 -> 502,4 -> 502,9 -> 494,9
//                """;//24 //93

        var lines = s.lines().toList();
        var pairs = lines.stream().map(v -> v.split("\\s->\\s"))
                .map(a -> Arrays.stream(a)
                        .map(i -> Arrays.stream(i.split(","))
                                .mapToInt(Integer::parseInt).toArray())
                        .toList())
                .toList();

        //Find max values
        var maxHeight = pairs.stream()
                .flatMap(Collection::stream)
                .mapToInt(k -> k[1])
                .max().getAsInt()+2;

        var maxWidth = pairs.stream()
                .flatMap(Collection::stream)
                .mapToInt(k -> k[0])
                .max().getAsInt();

        maxWidth = maxWidth  - (maxWidth-500) + maxHeight;

        var grid = new Grid(new int[maxHeight + 1][maxWidth + 1]);

        pairs.stream().forEach(l -> fillGrid(l, grid));
        for (int i = 0; i < grid.area()[maxHeight].length; i++) {
            grid.area()[maxHeight][i] = 1;
        }

        int sandUnits = 0;
        boolean overflow = false;
        int sandX = 500;
        int sandY = 0;
        while ( sandX >= 0 && sandX < maxWidth && sandY >= 0 && sandY<maxHeight) {
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
//                for (int j = 0; j < grid.area().length; j++) {
//                    for (int i = 493; i < grid.area()[0].length; i++) {
//                        System.out.print(grid.area()[j][i]);
//                    }
//                    System.out.println();
//                }
//                System.out.println();
            }
            if( grid.area()[sandY][sandX] == 2)
                break;

        }

        for (int j = 0; j < grid.area().length; j++) {
            for (int i = 470; i < grid.area()[0].length; i++) {
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

