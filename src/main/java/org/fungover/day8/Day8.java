package org.fungover.day8;

import java.util.Arrays;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;
import static org.fungover.util.Strings.stringValuesTo2DByteArray;

public class Day8 {

    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day8/day8.txt"));
//        String s = """
//                30373
//                25512
//                65332
//                33549
//                35390
//                """; //21

        //0 Shortest tree
        //9 tallest tree
        //Check left-right-up-down to see if a tree is visibile.
        //All trees should be lower for it to be visible
        //Border trees are always visible

        var grid = stringValuesTo2DByteArray(s);
        Boolean[][] visiblegrid = new Boolean[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[i].length; j++)
                visiblegrid[i][j] = Boolean.FALSE;


        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (checkLeft(i, j, grid) || checkUp(i, j, grid) ||
                        checkRight(i, j, grid) || checkDown(i, j, grid))
                    visiblegrid[i][j] = true;
            }
        }

        long count = Arrays.stream(visiblegrid)
                .flatMap(Arrays::stream).filter(i -> i)
                .count();
        System.out.println(count);
    }

    private static boolean checkDown(int i, int j, byte[][] grid) {
        int start = grid[i][j];
        while (i < grid.length-1) {
            if (grid[++i][j] >= start)
                return false;
        }
        return true;
    }

    private static boolean checkRight(int i, int j, byte[][] grid) {
        int start = grid[i][j];
        while (j < grid[i].length-1) {
            if (grid[i][++j] >= start)
                return false;
        }
        return true;
    }

    private static boolean checkUp(int i, int j, byte[][] grid) {
        int start = grid[i][j];
        while (i > 0) {
            if (grid[--i][j] >= start)
                return false;
        }
        return true;
    }

    private static boolean checkLeft(int i, int j, byte[][] grid) {
        int start = grid[i][j];
        while (j > 0) {
            if (grid[i][--j] >= start)
                return false;
        }
        return true;
    }

}
