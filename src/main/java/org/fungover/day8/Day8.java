package org.fungover.day8;

import java.util.ArrayList;
import java.util.List;

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

        var grid = stringValuesTo2DByteArray(s);

        step1(grid);
        step2(grid);
    }

    private static void step1(byte[][] grid) {
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                //Check all directions
                if (left(i, j, grid) ||
                        right(i, j, grid) ||
                        up(i, j, grid) ||
                        down(i, j, grid))
                    count++;
            }
        }
        System.out.println(count);
    }

    private static void step2(byte[][] grid) {
        List<Integer> scenicScore = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                //Check all directions
                count = leftCount(i, j, grid) *
                        rightCount(i, j, grid) *
                        upCount(i, j, grid) *
                        downCount(i, j, grid);
                scenicScore.add(count);
            }
        }
        scenicScore.stream().mapToInt(Integer::intValue).max().ifPresent(System.out::println);
    }

    private static boolean left(int i, int j, byte[][] grid) {
        int start = grid[i][j];
        while (j > 0) {
            if (grid[i][--j] >= start)
                return false;
        }
        return true;
    }

    private static boolean right(int i, int j, byte[][] grid) {
        int start = grid[i][j];
        while (j < grid[i].length - 1) {
            if (grid[i][++j] >= start)
                return false;
        }
        return true;
    }

    private static boolean up(int i, int j, byte[][] grid) {
        int start = grid[i][j];
        while (i > 0) {
            if (grid[--i][j] >= start)
                return false;
        }
        return true;
    }

    private static boolean down(int i, int j, byte[][] grid) {
        int start = grid[i][j];
        while (i < grid.length - 1) {
            if (grid[++i][j] >= start)
                return false;
        }
        return true;
    }

    private static int leftCount(int i, int j, byte[][] grid) {
        int count = 0;
        int start = grid[i][j];
        while (j > 0) {
            if (grid[i][--j] >= start)
                return count + 1;
            count++;
        }
        return count;
    }

    private static int rightCount(int i, int j, byte[][] grid) {
        int count = 0;
        int start = grid[i][j];
        while (j < grid[i].length - 1) {
            if (grid[i][++j] >= start)
                return count + 1;
            count++;
        }
        return count;
    }

    private static int upCount(int i, int j, byte[][] grid) {
        int count = 0;
        int start = grid[i][j];
        while (i > 0) {
            if (grid[--i][j] >= start)
                return count + 1;
            count++;
        }
        return count;
    }

    private static int downCount(int i, int j, byte[][] grid) {
        int count = 0;
        int start = grid[i][j];
        while (i < grid.length - 1) {
            if (grid[++i][j] >= start)
                return count + 1;
            count++;
        }
        return count;
    }
}