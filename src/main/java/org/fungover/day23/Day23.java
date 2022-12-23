package org.fungover.day23;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day23 {
    public static void main(String[] args) {

        String s = stringFromFile(resourceStringToPath("/day23/day23.txt"));

//        String s = """
//                ....#..
//                ..###.#
//                #...#.#
//                .#...##
//                #.###..
//                ##.#.##
//                .#..#..
//                """;

//        String s = """
//                .....
//                ..##.
//                ..#..
//                .....
//                ..##.
//                .....
//                """;

        List<Elf> elfs = new ArrayList<>();

        //Parse elfs
        int lines = 0;
        for (var line : s.lines().toList()) {
            var chars = line.split("");
            for (int i = 0; i < chars.length; i++) {
                if (chars[i].equals("#"))
                    elfs.add(new Elf(lines, i));
            }
            lines++;
        }

        List<Pos> whichList = new ArrayList<>();
        //Run for 10 rounds
        for (int i = 0; ; i++) {
           // printElfs(elfs);
            whichList.clear();
            for (var elf : elfs) {
                //Check if elf wants to move
                Pos p = elf.proposeMovement(elfs);
                if (p != null)
                    whichList.add(p);
            }
            if( whichList.isEmpty()) {
                System.out.println(i);
                break;
            }

            //Check movements that can be done. Only one elf wants to move there
            var canMove = whichList.stream().collect(Collectors.groupingBy(Function.identity(),
                            Collectors.counting()))
                    .entrySet().stream()
                    .filter(e -> e.getValue() == 1).toList();

            //Move allowed elfs
            canMove.forEach(c -> {
                var entry = c.getKey();
                entry.elf().move(entry.x(), entry.y());
            });

            //Rotate firstMoveDirection
            elfs.forEach(Elf::rotate);
        }

        //Find min, max values to calculate surrounding box.
        var minx = elfs.stream().mapToInt(Elf::getX).min().getAsInt();
        var maxx = elfs.stream().mapToInt(Elf::getX).max().getAsInt();
        var miny = elfs.stream().mapToInt(Elf::getY).min().getAsInt();
        var maxy = elfs.stream().mapToInt(Elf::getY).max().getAsInt();

        int boxSize = (maxx - minx+1) * (maxy - miny+1);

        System.out.println(boxSize - elfs.size()); //Expect 110
    }

    public static void printElfs(List<Elf> elfs) {
        var minx = elfs.stream().mapToInt(Elf::getX).min().getAsInt();
        var maxx = elfs.stream().mapToInt(Elf::getX).max().getAsInt();
        var miny = elfs.stream().mapToInt(Elf::getY).min().getAsInt();
        var maxy = elfs.stream().mapToInt(Elf::getY).max().getAsInt();

        for (int i = minx; i <= maxx; i++) {
            for (int j = miny; j <= maxy; j++) {
                if (elfs.contains(new Elf(i, j)))
                    System.out.print("#");
                else
                    System.out.print(".");
            }
            System.out.println();
        }
        System.out.println();
    }
}

enum MOVE {
    NORTH, SOUTH, WEST, EAST
}

final class Pos {
    private final int x;
    private final int y;
    private final Elf elf;

    Pos(int x, int y, Elf elf) {
        this.x = x;
        this.y = y;
        this.elf = elf;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public Elf elf() {
        return elf;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Pos) obj;
        return this.x == that.x &&
                this.y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Pos[" +
                "x=" + x + ", " +
                "y=" + y + ", " +
                "elf=" + elf + ']';
    }


}

class Elf {
    int x;
    int y;
    List<MOVE> moves = new ArrayList<>();

    public Elf(int x, int y) {
        this.x = x;
        this.y = y;
        moves.add(MOVE.NORTH);
        moves.add(MOVE.SOUTH);
        moves.add(MOVE.WEST);
        moves.add(MOVE.EAST);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public List<MOVE> getMoves() {
        return moves;
    }

    public void setMoves(List<MOVE> moves) {
        this.moves = moves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Elf elf = (Elf) o;

        if (x != elf.x) return false;
        return y == elf.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    public Pos proposeMovement(List<Elf> elfs) {
        //Check for neighbours, if zero stay put,
        if (neighbourCount(elfs) == 0)
            return null;
        for (var dir : moves) {
            if (neighbourCount(elfs, dir) == 0) {
                //Time to move
                if (dir == MOVE.NORTH)
                    return new Pos(x - 1, y, this);
                if (dir == MOVE.SOUTH)
                    return new Pos(x + 1, y, this);
                if (dir == MOVE.WEST)
                    return new Pos(x, y - 1, this);
                if (dir == MOVE.EAST)
                    return new Pos(x, y + 1, this);
            }
        }
        return null;
    }

    private int neighbourCount(List<Elf> elfs, MOVE direction) {
        int count = 0;
        var elfNorth = new Elf(x - 1, y);
        var elfNorthWest = new Elf(x - 1, y - 1);
        var elfNorthEast = new Elf(x - 1, y + 1);
        var elfWest = new Elf(x, y - 1);
        var elfEast = new Elf(x, y + 1);
        var elfSouth = new Elf(x + 1, y);
        var elfSouthWest = new Elf(x + 1, y - 1);
        var elfSouthEast = new Elf(x + 1, y + 1);

        if (direction == MOVE.NORTH) {
            if (elfs.contains(elfNorth)) count++;
            if (elfs.contains(elfNorthWest)) count++;
            if (elfs.contains(elfNorthEast)) count++;
            return count;
        }
        if (direction == MOVE.SOUTH) {
            if (elfs.contains(elfSouth)) count++;
            if (elfs.contains(elfSouthWest)) count++;
            if (elfs.contains(elfSouthEast)) count++;
            return count;
        }
        if (direction == MOVE.WEST) {
            if (elfs.contains(elfWest)) count++;
            if (elfs.contains(elfSouthWest)) count++;
            if (elfs.contains(elfNorthWest)) count++;
            return count;
        }
        if (direction == MOVE.EAST) {
            if (elfs.contains(elfEast)) count++;
            if (elfs.contains(elfSouthEast)) count++;
            if (elfs.contains(elfNorthEast)) count++;
            return count;
        }
        throw new IllegalStateException();
    }

    private int neighbourCount(List<Elf> elfs) {
        int count = 0;
        var elfNorth = new Elf(x - 1, y);
        var elfNorthWest = new Elf(x - 1, y - 1);
        var elfNorthEast = new Elf(x - 1, y + 1);
        var elfWest = new Elf(x, y - 1);
        var elfEast = new Elf(x, y + 1);
        var elfSouth = new Elf(x + 1, y);
        var elfSouthWest = new Elf(x + 1, y - 1);
        var elfSouthEast = new Elf(x + 1, y + 1);

        if (elfs.contains(elfNorth)) count++;
        if (elfs.contains(elfNorthWest)) count++;
        if (elfs.contains(elfNorthEast)) count++;
        if (elfs.contains(elfWest)) count++;
        if (elfs.contains(elfEast)) count++;
        if (elfs.contains(elfSouth)) count++;
        if (elfs.contains(elfSouthWest)) count++;
        if (elfs.contains(elfSouthEast)) count++;

        return count;
    }


    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void rotate() {
        var temp = moves.remove(0);
        moves.add(temp);
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}