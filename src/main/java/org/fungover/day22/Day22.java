package org.fungover.day22;

import java.util.Arrays;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day22 {
    public static void main(String[] args) {

        String s = stringFromFile(resourceStringToPath("/day22/day22.txt"));

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
        String path = s.split("\\r\\n\\r\\n")[1];
        var map = s.split("\\r\\n\\r\\n")[0];

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
        //Find startpos on first line
        int posX = 0;
        int posY = 0;
        for (int i = 0; i < grid[0].length; i++) {
            if (grid[0][i] == 1) {
                posY = i;
                break;
            }
        }
        int direction = 0;
        Point2D currentPos = new Point2D(posX, posY);

        //Parse instructions
        var instructions = Arrays.stream(path.split("((?=\\D))|((?<=\\D))")).map(Day22::toCommand).toList();

        for (var command : instructions) {
            if (command instanceof StepCommand stepCommand)
                for (int i = 0; i < stepCommand.steps; i++) {
                    // Get nextPos
                    Point2D next = nextPos(currentPos, direction, grid);
                    // Check if Wall
                    if (isWall(next, grid))
                        break;
                    currentPos = next;
                }
            if (command instanceof TurnCommand turnCommand){
                direction += turnCommand.direction;
            if (direction > 3)
                direction = 0;
            if (direction < 0)
                direction = 3;
        }}

        //Sum, +1 because of 0 index for our array
        System.out.println(1000 * (currentPos.x() + 1) + 4 * (currentPos.y() + 1) + direction);
    }

    private static Command toCommand(String s) {
        if (s.matches("\\d+"))
            return new StepCommand(Integer.parseInt(s));
        else
            return new TurnCommand(s);

    }

    private static boolean isWall(Point2D pos, int[][] grid) {
        return grid[pos.x()][pos.y()] == 2;
    }

    private static Point2D nextPos(Point2D currentPos, int direction, int[][] grid) {
        var delta = directionToPoint(direction);
        var tryPoint = currentPos;
        while(true) {
            tryPoint = new Point2D(tryPoint.x() + delta.x(), tryPoint.y() + delta.y());
            //Check if tryPoint is outside grid to the right, then wrap to the left
            if( tryPoint.x() > grid.length-1)
                tryPoint = new Point2D(0, tryPoint.y());
            if( tryPoint.x() < 0)
                tryPoint = new Point2D(grid.length - 1, tryPoint.y());
            if( tryPoint.y() > grid[0].length-1)
                tryPoint = new Point2D(tryPoint.x(), 0);
            if( tryPoint.y() < 0 )
                tryPoint = new Point2D(tryPoint.x(), grid[0].length-1);
            if( grid[tryPoint.x()][tryPoint.y()] == 1) //Valid pos
                return tryPoint;
            if( grid[tryPoint.x()][tryPoint.y()] == 2) //Wall, return for now
                return tryPoint;
            if( grid[tryPoint.x()][tryPoint.y()] == 0) //Don't go here, keep moving
                continue;
        }
    }

    private static Point2D directionToPoint(int direction) {
        return switch (direction) {
            case 0 -> new Point2D(0, 1);
            case 1 -> new Point2D(1, 0);
            case 2 -> new Point2D(0, -1);
            case 3 -> new Point2D(-1, 0);
            default -> throw new IllegalStateException();
        };
    }
}

class Command {
}

class StepCommand extends Command {
    int steps;

    public StepCommand(int steps) {
        this.steps = steps;
    }
}

class TurnCommand extends Command {
    int direction;

    public TurnCommand(String direction) {
        if (direction.equals("R"))
            this.direction = 1;
        else
            this.direction = -1;
    }
}

record Point2D(int x, int y) {
}
