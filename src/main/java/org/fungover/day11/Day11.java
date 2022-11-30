package org.fungover.day11;

import org.fungover.util.ArrayUtils;
import org.fungover.util.Strings;

import java.util.Arrays;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day11 {

    private static int STEPS = 100;

    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day11/day11.txt"));
//        String s = """
//                5483143223
//                2745854711
//                5264556173
//                6141336146
//                6357385478
//                4167524645
//                2176841721
//                6882881134
//                4846848554
//                5283751526
//                """;
//        String s = """
//                11111
//                19991
//                19191
//                19991
//                11111""";

        var area = Strings.stringValuesTo2DByteArray(s);

        int totalFlashCount = 0;

        for (int i = 0; i < STEPS; i++) {
            ArrayUtils.addValueToAllPlaces(area, 1);

            int stepFlashCount = 0;
            for (int r = 0; r < area.length; r++) {
                for (int c = 0; c < area[r].length; c++) {
                    if (area[r][c] > 9) {
                        stepFlashCount++;
                        area[r][c] = 0;
                        //Increase all surrounding places and count new flashes
                        stepFlashCount += flash(area, r, c);
                    }
                }
            }
            System.out.println("Step "+ (i+1) +" flashes: " + stepFlashCount);
            totalFlashCount += stepFlashCount;
//Step two, run until all flashes at the same time. Increase STEPS to go longer.
//            if( stepFlashCount == area.length * area[0].length)
//                return;
        }
        System.out.println(totalFlashCount);
    }

    private static int flash(byte[][] area, int r, int c) {
        int flashCount = 0;
        for (int i = r-1; i < r+2 ; i++) {
            for (int j = c-1; j < c+2; j++) {
                if( ArrayUtils.getValue(area,i,j,-1) == -1)
                    continue;
                if( area[i][j] != 0)
                    area[i][j]++;
                if (area[i][j] > 9){
                    area[i][j] = 0;
                    flashCount++;
                    flashCount += flash(area,i,j);
                }
            }
        }
        return flashCount;
    }


}
