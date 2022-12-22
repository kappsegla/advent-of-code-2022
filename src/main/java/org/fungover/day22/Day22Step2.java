package org.fungover.day22;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.fungover.day22.Day22Step2.DIR.*;
import static org.fungover.day22.Day22Step2.SIDE.*;
import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;


public class Day22Step2 {

    public static void main(String[] args) {

        String s = stringFromFile(resourceStringToPath("/day22/day22.txt"));
//
//        String s = """
//                        ...#
//                        .#..
//                        #...
//                        ....
//                ...#.......#
//                ........#...
//                ..#....#....
//                ..........#.
//                        ...#....
//                        .....#..
//                        .#......
//                        ......#.
//
//                10R5L5R10L4R5L5""";
        String path = s.split("\\n\\n")[1];
        var map = s.split("\\n\\n")[0];

        int height = (int) map.lines().count();
        int width = map.lines().mapToInt(String::length).max().getAsInt();

        int[][] grid = new int[height][width];

        var lines = map.split("\\n");
        for (int i = 0; i < height; i++) {
            var cols = lines[i].split("");
            for (int j = 0; j < cols.length; j++) {
                if (cols[j].equals(" "))
                    grid[i][j] = 0;
                if (cols[j].equals("."))
                    grid[i][j] = 1;
                if (cols[j].equals("#"))
                    grid[i][j] = 2;
            }
        }
        Grid gridObj = new Grid(grid);

        //Find startpos on first line
        int posX = 0;
        int posY = 0;
        for (int i = 0; i < grid[0].length; i++) {
            if (grid[0][i] == 1) {
                posY = i;
                break;
            }
        }
        var currentPos = new PointAndDir(posX, posY, RIGHT);

        //BiFunction<PointAndDir, Grid, PointAndDir> nextPos = Day22Step2::nextPos;
        BiFunction<PointAndDir, Grid, PointAndDir> nextPos = Day22Step2::nextCubePos;

        //Parse instructions
        var instructions = Arrays.stream(path.split("((?=\\D))|((?<=\\D))")).map(Day22Step2::toCommand).toList();

        var next = currentPos;
        for (var command : instructions) {
            if (command instanceof StepCommand stepCommand)
                for (int i = 0; i < stepCommand.steps; i++) {
                    // Get nextPos
                    if (sideOf(new PointAndDir(currentPos.x + currentPos.dir.point2D.x, currentPos.y + currentPos.dir.point2D.y, currentPos.dir)) != OUTSIDE)
                        next = new PointAndDir(currentPos.x + currentPos.dir.point2D.x, currentPos.y + currentPos.dir.point2D.y, currentPos.dir);
                    else //Wrap around
                        next = nextPos.apply(currentPos, gridObj);
                    //var next = nextPos.apply(currentPos, gridObj);
                    if (isWall(next, grid)) break;
                    currentPos = next;
                }
            if (command instanceof TurnCommand turnCommand) {
                var newDirection = turnCommand.turn(currentPos.dir);
                currentPos = new PointAndDir(currentPos.x, currentPos.y, newDirection);
            }
        }
        //Sum, +1 because of 0 index for our array
        System.out.println(1000 * (currentPos.x() + 1) + 4 * (currentPos.y() + 1) + currentPos.dir.ordinal());
    }

    private static Command toCommand(String s) {
        if (s.matches("\\d+"))
            return new StepCommand(Integer.parseInt(s));
        else
            return new TurnCommand(s);
    }

    private static boolean isWall(PointAndDir pos, int[][] grid) {
        return grid[pos.x()][pos.y()] == 2;
    }

    private static PointAndDir nextPos(PointAndDir currentPos, Grid gridObj) {
        var delta = currentPos.dir.point2D;
        var grid = gridObj.getGrid();
        var tryPoint = currentPos;
        while (true) {
            tryPoint = new PointAndDir(tryPoint.x() + delta.x(), tryPoint.y() + delta.y(), tryPoint.dir);
            //Check if tryPoint is outside grid to the right, then wrap to the left
            if (tryPoint.x() > grid.length - 1)
                tryPoint = new PointAndDir(0, tryPoint.y(), tryPoint.dir);
            if (tryPoint.x() < 0)
                tryPoint = new PointAndDir(grid.length - 1, tryPoint.y(), tryPoint.dir);
            if (tryPoint.y() > grid[0].length - 1)
                tryPoint = new PointAndDir(tryPoint.x(), 0, tryPoint.dir);
            if (tryPoint.y() < 0)
                tryPoint = new PointAndDir(tryPoint.x(), grid[0].length - 1, tryPoint.dir);
            if (grid[tryPoint.x()][tryPoint.y()] == 1) //Valid pos
                return tryPoint;
            if (grid[tryPoint.x()][tryPoint.y()] == 2) //Wall, return for now
                return tryPoint;
            if (grid[tryPoint.x()][tryPoint.y()] == 0) //Don't go here, keep moving
                continue;
        }
    }

    public static PointAndDir nextCubePos(PointAndDir curr, Grid gridObj) {
        var nextDir = curr.dir;
        var currDir = curr.dir;
        var currSide = sideOf(curr);
        var nextPos = new Point2D(curr.x, curr.y);
        if (currSide == A && currDir == UP) {
            nextDir = RIGHT;
            nextPos = new Point2D(150 + curr.y - 50, 0); // nextSide = F !
        } else if (currSide == A && currDir == LEFT) {
            nextDir = RIGHT;
            nextPos = new Point2D(100 + (50 - curr.x - 1), 0); // nextSide = E !
        } else if (currSide == B && currDir == UP) {
            nextDir = UP;
            nextPos = new Point2D(199, curr.y - 100);// nextSide = F
        } else if (currSide == B && currDir == RIGHT) {
            nextDir = LEFT;
            nextPos = new Point2D((50 - curr.x) + 2 * 50 - 1, 99); // nextSide = D  ??
        } else if (currSide == B && currDir == DOWN) {
            nextDir = LEFT;
            nextPos = new Point2D(50 + (curr.y - 2 * 50), 99); // nextSide = C !
        } else if (currSide == C && currDir == RIGHT) {
            nextDir = UP;
            nextPos = new Point2D(49, (curr.x - 50) + 2 * 50); // nextSide = B !
        } else if (currSide == C && currDir == LEFT) {
            nextDir = DOWN;
            nextPos = new Point2D(100, curr.x - 50); // nextSide = E !
        } else if (currSide == E && currDir == LEFT) {
            nextDir = RIGHT;
            nextPos = new Point2D(curr.x - 2 * 50, 50); // nextSide = A !!
        } else if (currSide == E && currDir == UP) {
            nextDir = RIGHT;
            nextPos = new Point2D(50 + curr.y, 50); // nextSide = C !
        } else if (currSide == D && currDir == DOWN) {
            nextDir = LEFT;
            nextPos = new Point2D(3 * 50 + (curr.y - 50), 49); // nextSide = F !
        } else if (currSide == D && currDir == RIGHT) {
            nextDir = LEFT;
            nextPos = new Point2D(50 - (curr.x - 50 * 2) - 1, 149); // nextSide = B !??
        } else if (currSide == F && currDir == RIGHT) {
            nextDir = UP;
            nextPos = new Point2D(149, (curr.x - 3 * 50) + 50); // nextSide = D !
        } else if (currSide == F && currDir == LEFT) {
            nextDir = DOWN;
            nextPos = new Point2D(0, 50 + (curr.x - 3 * 50)); // nextSide = A !
        } else if (currSide == F && currDir == DOWN) {
            nextDir = DOWN;
            nextPos = new Point2D(0, curr.y + 100); // nextSide = B !
        }
        return new PointAndDir(nextPos.x, nextPos.y, nextDir);
    }

    private static SIDE sideOf(PointAndDir pos) {
        if (pos.x >= 0 && pos.x <= 49 && pos.y >= 50 && pos.y <= 99) return A;
        if (pos.x >= 0 && pos.x <= 49 && pos.y >= 100 && pos.y <= 149) return B;
        if (pos.x >= 50 && pos.x <= 99 && pos.y >= 50 && pos.y <= 99) return C;
        if (pos.x >= 100 && pos.x <= 149 && pos.y >= 50 && pos.y <= 99) return D;
        if (pos.x >= 100 && pos.x <= 149 && pos.y >= 0 && pos.y <= 49) return E;
        if (pos.x >= 150 && pos.x <= 199 && pos.y >= 0 && pos.y <= 49) return F;
        return OUTSIDE;
    }

    enum SIDE {
        A, B, C, D, E, F, OUTSIDE
    }

    enum DIR {
        RIGHT(new Point2D(0, 1)),
        DOWN(new Point2D(1, 0)),
        LEFT(new Point2D(0, -1)),
        UP(new Point2D(-1, 0));

        final Point2D point2D;

        DIR(Point2D point2D) {
            this.point2D = point2D;
        }

        Point2D asPoint() {
            return point2D;
        }

        public DIR right() {
            return values()[modulus(this.ordinal() + 1, values().length)];
        }

        public DIR left() {
            return values()[modulus(this.ordinal() - 1, values().length)];
        }

        private int modulus(int x, int y) {
            return (Math.floorMod(x, y) + Math.abs(y)) % Math.abs(y);
        }
    }

    private static class Command {

    }

    private static class StepCommand extends Command {
        int steps;

        public StepCommand(int steps) {
            this.steps = steps;
        }
    }

    private static class TurnCommand extends Command {
        String leftOrRight;

        public TurnCommand(String direction) {
            leftOrRight = direction;
        }

        public DIR turn(DIR dir) {
            if (leftOrRight.equals("R"))
                return dir.right();

            return dir.left();
        }

    }

    private record Point2D(int x, int y) {
    }

    private record PointAndDir(int x, int y, DIR dir) {
    }
}

