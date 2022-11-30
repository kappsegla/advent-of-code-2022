package org.fungover.day9;

import java.util.*;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day9 {

    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day9/day9.txt"));
//        String s = """
//                2199943210
//                3987894921
//                9856789892
//                8767896789
//                9899965678""";


        var rows = (int) s.lines().count();
        var cols = s.lines().findAny().get().length();
        byte[][] array = new byte[rows][cols];
        int count = 0;
        //Fill array
        for (var line : s.lines().toList()) {
            for (var height : line.chars().toArray()) {
                array[count / cols][count % cols] = (byte) Character.getNumericValue(height);
                count++;
            }
        }

        //Find low points
        List<Byte> lowpoints = new ArrayList<>();
        List<Integer> basinSize = new ArrayList<>();
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                if (isLowPoint(array, r, c, cols, rows)) {
                    lowpoints.add(array[r][c]);
                    Set<Point> points = new HashSet<>();
                    calculateBasinSize(array, points, r, c, cols, rows, array[r][c]);
                    points.add(new Point(r,c,array[r][c]));
                    basinSize.add(points.size());
                    System.out.println(points);
                }
            }

        //Calculate risk level of lowpoints as height + 1
        var sum = lowpoints.stream().mapToInt(Integer::valueOf).map(i -> ((char) i) + 1).sum();
        System.out.println("LowPoint sum: " + sum);
        //Calculate multiplication of all basinSizes
        var multiply = basinSize.stream()
                .sorted(Collections.reverseOrder())
                .mapToInt(Integer::valueOf).limit(3).reduce((left, right) -> left * right).getAsInt();
        System.out.println("Lowpoint sizes multiplied: " + multiply);
    }

    private static void calculateBasinSize(byte[][] array, Set<Point> points, int r, int c, int cols, int rows, int previousValue) {
        //Are r and c invalid coords
        if (r < 0 || c < 0 || r >= rows || c >= cols)
            return;
        //Height 9 doesn't belong to any basins
        if(  array[r][c] == 9 )
            return;
        //Are we already in the set?
        if( points.contains(new Point(r,c,array[r][c])))
            return;

        //Check if we are higher than previous value and add to set
        if (array[r][c] >=  previousValue)
            points.add(new Point(r, c, array[r][c]));

        //Go left
        calculateBasinSize(array, points, r, c - 1, cols, rows, array[r][c]);
        //Go right
        calculateBasinSize(array, points, r, c + 1, cols, rows, array[r][c]);
        //Go up
        calculateBasinSize(array, points, r - 1, c, cols, rows, array[r][c]);
        //Go down
        calculateBasinSize(array, points, r + 1, c, cols, rows, array[r][c]);

    }

    private static boolean isLowPoint(byte[][] array, int row, int col, int cols, int rows) {
        byte pos = array[row][col];
        byte up = Byte.MAX_VALUE;
        byte down = Byte.MAX_VALUE;
        byte left = Byte.MAX_VALUE;
        byte right = Byte.MAX_VALUE;

        if (col - 1 >= 0)
            left = array[row][col - 1];
        if (col + 1 < cols)
            right = array[row][col + 1];
        if (row - 1 >= 0)
            up = array[row - 1][col];
        if (row + 1 < rows)
            down = array[row + 1][col];

        return pos < up && pos < down && pos < left && pos < right;
    }

}
