package org.fungover.day5;

import org.fungover.util.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day5 {
    public static void main(String[] args) {
          String s = stringFromFile(resourceStringToPath("/day5/day5.txt"));
//        String s = """
//                0,9 -> 5,9
//                8,0 -> 0,8
//                9,4 -> 3,4
//                2,2 -> 2,1
//                7,0 -> 7,4
//                6,4 -> 2,0
//                0,9 -> 2,9
//                3,4 -> 1,4
//                0,0 -> 8,8
//                5,5 -> 8,2""";
//        String s = """
//                1,1 -> 3,3
//                9,7 -> 7,9
//                """;
        //Create Lines
        List<Line> lines = s.lines().map(Strings::removeAllSpaces).map(Day5::toLine).toList();

        //Keep only horizontal and vertical lines
        lines = lines.stream().filter(l -> hasSameX(l) || hasSameY(l) || isDiagonal(l)).toList();

        List<Pos> posList = new ArrayList<>();

        for (var line : lines) {
            if (hasSameX(line)) {
                for (int i = line.start().y(); i <= line.end().y(); i++) {
                    var pos = new Pos(line.start().x(), i, 1);
                    var index = posList.indexOf(pos);
                    if (index == -1)
                        posList.add(pos);
                    else {
                        var oldPos = posList.get(index);
                        posList.set(index, new Pos(pos.x(), pos.y(), oldPos.value() + 1));
                    }
                }
            } else if (hasSameY(line)) {
                for (int i = line.start().x(); i <= line.end().x(); i++) {
                    var pos = new Pos(i, line.start().y(), 1);
                    var index = posList.indexOf(pos);
                    if (index == -1)
                        posList.add(pos);
                    else {
                        var oldPos = posList.get(index);
                        posList.set(index, new Pos(pos.x(), pos.y(), oldPos.value() + 1));
                    }
                }
            } else if (isDiagonal(line)) {
                var dx = line.end().x() - line.start().x();
                var startX = line.start().x();
                var stopX = line.end().x();
                var startY = line.start().y();
                var stopY = line.end().y();


                if (Integer.signum(dx) < 0) {
                    var temp = startX;
                    startX = stopX;
                    stopX = temp;
                    temp = startY;
                    startY = stopY;
                    stopY = temp;
                }
                var signumY = Math.signum(stopY - startY);

                for (int x = startX; x <= stopX; x++) {
                    var pos = new Pos(x, startY, 1);
                    var index = posList.indexOf(pos);
                    if (index == -1)
                        posList.add(pos);
                    else {
                        var oldPos = posList.get(index);
                        posList.set(index, new Pos(pos.x(), pos.y(), oldPos.value() + 1));
                    }
                    //Calculate y coord.
                    startY += signumY;
                }

            }
        }

        System.out.println(posList.toString());

        //Count lines with more than 2 overlaps
        System.out.println(posList.stream().filter(p -> p.value() > 1).count());


    }

    private static boolean isDiagonal(Line l) {
        return Math.abs(l.start().x() - l.end().x()) == Math.abs(l.start().y() - l.end().y());
    }

    private static boolean hasSameY(Line l) {
        return l.start().y() == l.end().y();
    }

    private static boolean hasSameX(Line l) {
        return l.start().x() == l.end().x();
    }

    private static Line toLine(String s) {
        var parts = Arrays.stream(s.split("->|,")).mapToInt(Integer::parseInt).toArray();
        if (parts[0] == parts[2] && parts[1] < parts[3])
            return new Line(new Pos(parts[0], parts[1], 0), new Pos(parts[2], parts[3], 0));
        else if (parts[0] == parts[2] && parts[1] > parts[3])
            return new Line(new Pos(parts[2], parts[3], 0), new Pos(parts[0], parts[1], 0));
        else if (parts[1] == parts[3] && parts[0] < parts[2])
            return new Line(new Pos(parts[0], parts[1], 0), new Pos(parts[2], parts[3], 0));
        else
            return new Line(new Pos(parts[2], parts[3], 0), new Pos(parts[0], parts[1], 0));
    }
}

record Line(Pos start, Pos end) {
}

record Pos(int x, int y, int value) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pos pos = (Pos) o;

        if (x != pos.x) return false;
        return y == pos.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
