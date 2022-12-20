package org.fungover.day17;

import java.io.IOException;
import java.util.*;

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

    static int[][] grid = new int[7][30000];

    static int currentRockId = 0;
    static int px = 2;
    static int py = 3;


    public static void main(String[] args) throws IOException {

        String s = stringFromFile(resourceStringToPath("/day17/day17.txt"));
//        String s = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>";

        long rockCount = 0;
        int jetId = -1;

//        while (rockCount < 2022) {
//            for (int c : s.chars().toArray()) {
//                jetId++;
//                if (c == '<')
//                    moveRock(-1, 0);
//                else if (c == '>')
//                    moveRock(1, 0);
//                if (!moveDown()) {
//                    //Rock is stuck, add new rock
//                    currentRockId = (currentRockId + 1) % rocks.length;
//                    px = 2;
//                    py = findMaxY() + 3 + 4 - freeSpace[currentRockId];
//
//                    System.out.println("New Rock");
//                    rockCount++;
//                    if (rockCount >= 2022)
//                        break;
//                }
//            }
//        }
//        System.out.println(findMaxY() + 1);

        //Step 2
        Map<Earlier, Value> seen = new HashMap<>();
        Value firstRepeat = null;
        var times = 0L;

        while (rockCount < 1000000000000L) {
            for (int c : s.chars().toArray()) {
                jetId++;
                if (c == '<')
                    moveRock(-1, 0);
                else if (c == '>')
                    moveRock(1, 0);
                if (!moveDown()) {
                    var topView = topView();
                    var latest = new Earlier(topView, currentRockId, jetId);
                    if (seen.containsKey(latest)) {
                        if( firstRepeat == null) {
                            firstRepeat = seen.get(latest);
                            System.out.println(seen.get(latest));
                            times = (1000000000000L-rockCount) / rockCount;
                            rockCount+=rockCount*times;
                        }
                    }
                    seen.put(latest, new Value(findMaxY(), rockCount));

                    //Rock is stuck, add new rock
                    currentRockId = (currentRockId + 1) % rocks.length;
                    px = 2;
                    py = findMaxY() + 3 + 4 - freeSpace[currentRockId];

                    //System.out.println("New Rock");
                    rockCount++;
                    if (rockCount >= 1000000000000L)
                        break;
                }
            }
            jetId = 0;
        }

        long height = times * firstRepeat.height() + findMaxY();
        System.out.println(height);
    }

    private static int[] topView() {
        int maxy = findMaxY();
        int[] topView = new int[7];
        //For each column walk down until we reach bottom or a brick
        //Store that value for that column
        for (int j = 0; j < 7; j++) {
            for (int i = maxy; i >= maxy - 17; i--) {
                if (i < 0)
                    break;
                if (grid[j][i] == 1)
                    break;
                topView[j]++;
            }
        }
        return topView;
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

final class Earlier {
    private final int[] topView;
    private final int rockId;
    private final int jetId;

    Earlier(int[] topView, int rockId, int jetId) {
        this.topView = topView;
        this.rockId = rockId;
        this.jetId = jetId;
    }

    public int[] topView() {
        return topView;
    }

    public int rockId() {
        return rockId;
    }

    public int jetId() {
        return jetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Earlier earlier = (Earlier) o;

        if (rockId != earlier.rockId) return false;
        if (jetId != earlier.jetId) return false;
        return Arrays.equals(topView, earlier.topView);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(topView);
        result = 31 * result + rockId;
        result = 31 * result + jetId;
        return result;
    }

    @Override
    public String toString() {
        return "Earlier[" +
                "topView=" + topView + ", " +
                "rockId=" + rockId + ", " +
                "jetId=" + jetId + ']';
    }

}

record Value(int height, long rockCount) {
}
