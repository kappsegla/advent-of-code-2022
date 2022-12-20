package org.fungover.day17;

import java.io.IOException;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day17 {
    final static int[][][] rocks = {
            {
                    {1, 0, 0, 0},
                    {1, 0, 0, 0},
                    {1, 0, 0, 0},
                    {1, 0, 0, 0}},

            {
                    {0, 1, 0, 0},
                    {1, 1, 1, 0},
                    {0, 1, 0, 0},
                    {0, 0, 0, 0}},

            {
                    {0, 0, 1, 0},
                    {0, 0, 1, 0},
                    {1, 1, 1, 0},
                    {0, 0, 0, 0}},

            {
                    {1, 1, 1, 1},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}},

            {
                    {1, 1, 0, 0},
                    {1, 1, 0, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}}
    };

    static int[] freeSpace = {3, 1, 1, 0, 2};

    static int[][] grid = new int[7][10000];

    static int currentRockId = 0;
    static int px = 2;
    static int py = 3;


    public static void main(String[] args) throws IOException {

        String s = stringFromFile(resourceStringToPath("/day17/day17.txt"));
//        String s = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>";

        int rockCount = 0;

        while (rockCount < 2022) {
            for (int c : s.chars().toArray()) {
                if (c == '<')
                    moveRock(-1, 0);
                else if (c == '>')
                    moveRock(1, 0);
                if (!moveDown()) {
                    //Rock is stuck, add new rock
                    currentRockId = (currentRockId + 1) % rocks.length;
                    px = 2;
                    py = findMaxY() + 3 + 4 - freeSpace[currentRockId];

                    System.out.println("New Rock");
                    rockCount++;
                    if (rockCount >= 2022)
                        break;
                }
            }
        }
        System.out.println(findMaxY() + 1);
    }

    private static int findMaxY() {
        int maxy = 0;
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 7; j++) {
                if (grid[j][i] == 1) {
                    maxy = Math.max(maxy, i);
                    break;
                }
            }
            if (i > maxy)
                break;
        }
        return maxy;
    }

    private static boolean moveDown() {
        if (!moveRock(0, -1)) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (rocks[currentRockId][i][j] == 0)
                        continue;
                    int rpx = px + i;
                    int rpy = py - j;
                    assert grid[rpx][rpy] == 0;
                    grid[rpx][rpy] = 1;
                }
            }
            return false;
        }
        return true;
    }

    private static boolean moveRock(int dx, int dy) {
        int nextX = px + dx;
        int nextY = py + dy;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (rocks[currentRockId][i][j] == 0)
                    continue;
                int rpx = nextX + i;
                int rpy = nextY - j;
                if (rpx < 0 || rpx > 6)
                    return false;
                if (rpy < 0)
                    return false;
                if (grid[rpx][rpy] != 0)
                    return false;
            }
        }
        //Rock can move
        px += dx;
        py += dy;
        return true;
    }
}
