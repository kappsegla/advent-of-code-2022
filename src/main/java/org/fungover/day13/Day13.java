package org.fungover.day13;

import java.util.Arrays;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day13 {
    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day13/day13.txt"));
//        String s = """
//                6,10
//                0,14
//                9,10
//                0,3
//                10,4
//                4,11
//                6,0
//                6,12
//                4,1
//                0,13
//                10,12
//                3,4
//                3,0
//                8,4
//                1,10
//                2,14
//                8,10
//                9,0
//                """;
//        String fold = """
//                fold along y=7
//                fold along x=5
//                """;
        String fold = """
                fold along x=655
                fold along y=447
                fold along x=327
                fold along y=223
                fold along x=163
                fold along y=111
                fold along x=81
                fold along y=55
                fold along x=40
                fold along y=27
                fold along y=13
                fold along y=6""";

        var maxX = s.lines().map(m -> m.split(",")[0]).mapToInt(Integer::parseInt).max().getAsInt();
        var maxY = s.lines().map(m -> m.split(",")[1]).mapToInt(Integer::parseInt).max().getAsInt();

        int[][] paper = new int[maxY + 1][maxX + 1];

        for (String v : s.lines().toList()) {
            if (v.isEmpty())
                break;
            var coords = v.split(",");
            var x = Integer.parseInt(coords[0]);
            var y = Integer.parseInt(coords[1]);
            paper[y][x] = 1;
        }
        for (String command : fold.lines().toList()) {
            var c = command.split("=");
            var foldPoint = Integer.parseInt(c[1]);
            if (c[0].charAt(c[0].length() - 1) == 'y')
                paper = foldAlongY(paper, foldPoint);
            else
                paper = foldAlongX(paper, foldPoint);
        }

        printResult(paper);

        var result = Arrays.stream(paper).flatMapToInt(Arrays::stream).filter(i -> i > 0).count();
        System.out.println(result);
    }

    private static void printResult(int[][] paper) {
        for (int[] i : paper) {
            for (int j : i) {
                if (j == 0)
                    System.out.print(" ");
                else
                    System.out.print("#");
            }
            System.out.println();
        }
    }

    private static int[][] foldAlongX(int[][] paper, int fold) {
        var newY = paper.length;
        var folded = new int[newY][paper[0].length / 2];
        copyArray(paper, folded);

        for (int y = 0; y < folded.length; y++) {
            for (int x = 0; x < folded[y].length; x++) {
                if ((x - (x - fold) * 2) < paper[y].length)
                    folded[y][x] += paper[y][x - ((x - fold) * 2)];
            }
        }
        return folded;
    }

    private static int[][] foldAlongY(int[][] paper, int fold) {
        var newX = paper[0].length;
        var folded = new int[paper.length / 2][newX];
        copyArray(paper, folded);
        for (int y = 0; y < folded.length; y++) {
            for (int x = 0; x < folded[y].length; x++) {
                if ((y - (y - fold) * 2) < paper.length)
                    folded[y][x] += paper[y - ((y - fold) * 2)][x];
            }
        }

        return folded;
    }

    private static void copyArray(int[][] from, int[][] to) {
        for (int i = 0; i < to.length; i++) {
            System.arraycopy(from[i], 0, to[i], 0, to[i].length);
        }
    }

}
