package org.fungover.day15;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day15 {

//    public static final int ROW = 10;
    public static final int ROW = 2000000;
//    public static final int BOUND = 20;
    public static final int BOUND = 4_000_000;


    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day15/day15.txt"));
//        String s = """
//                Sensor at x=2, y=18: closest beacon is at x=-2, y=15
//                Sensor at x=9, y=16: closest beacon is at x=10, y=16
//                Sensor at x=13, y=2: closest beacon is at x=15, y=3
//                Sensor at x=12, y=14: closest beacon is at x=10, y=16
//                Sensor at x=10, y=20: closest beacon is at x=10, y=16
//                Sensor at x=14, y=17: closest beacon is at x=10, y=16
//                Sensor at x=8, y=7: closest beacon is at x=2, y=10
//                Sensor at x=2, y=0: closest beacon is at x=2, y=10
//                Sensor at x=0, y=11: closest beacon is at x=2, y=10
//                Sensor at x=20, y=14: closest beacon is at x=25, y=17
//                Sensor at x=17, y=20: closest beacon is at x=21, y=22
//                Sensor at x=16, y=7: closest beacon is at x=15, y=3
//                Sensor at x=14, y=3: closest beacon is at x=15, y=3
//                Sensor at x=20, y=1: closest beacon is at x=15, y=3
//                """;

        var data = s.lines().map(i -> i.split("[=,:]"))
                .map(k -> new int[]{Integer.parseInt(k[1]), Integer.parseInt(k[3]), Integer.parseInt(k[5]), Integer.parseInt(k[7])})
                .map(a -> new Sensor(a[0], a[1], Math.abs(a[0] - a[2]) + Math.abs(a[1] - a[3]), new Beacon(a[2], a[3]))).toList();

        Set<Integer> noBeacons = new HashSet<>();
        for (var sensor : data) {
            //If ROW isn't inside sensor radius for sensor look at next
            if (ROW > sensor.manhattanDistance() + sensor.y() &&
                    ROW < sensor.y() - sensor.manhattanDistance())
                continue;
            //Calculate distance between ROW and sensor radius
            var rowDistance = ROW > sensor.y() ?
                    (sensor.y() + sensor.manhattanDistance()) - ROW :
                    ROW - (sensor.y() - sensor.manhattanDistance());
            // Loop through every x coordinate for point that is inside the signal area on ROW
            for (var x = sensor.x() - rowDistance; x < sensor.x() + rowDistance; x++)
                noBeacons.add(x);
        }
        System.out.println(noBeacons.size());


        //x and y coordinates each no lower than 0 and no larger than 4000000.
        //One missing value, has to be outside the boundaries of at least two scanners, Except if it's on the bounds of the 0-4_000_000 square.
        Set<Integer> a = new HashSet<>();
        Set<Integer> b = new HashSet<>();

        for (var sensor : data) {
            a.add(sensor.y() - sensor.x() + sensor.manhattanDistance() + 1);
            a.add(sensor.y() - sensor.x() - sensor.manhattanDistance() - 1);
            b.add(sensor.y() + sensor.x() + sensor.manhattanDistance() + 1);
            b.add(sensor.y() + sensor.x() - sensor.manhattanDistance() - 1);
        }

        for (var as : a)
            for (var bs : b) {
                if ((bs - as) % 2 == 1 || as >= bs)  //Will not intersect if odd
                    continue;
                var p = new Point((bs - as) / 2, (as + bs) / 2);  //Intersection points
                //Check if intersection point is outside all sensor radius
                if (p.p1() > 0 && p.p1() <= BOUND && p.p2() > 0 && p.p2() <= BOUND)
                    if (data.stream().allMatch(t -> distance(p, t) > t.manhattanDistance()))
                        System.out.println("Found " + p.p1() + "," + p.p2() + ":" + BigInteger.valueOf(4_000_000).multiply(BigInteger.valueOf(p.p1())).add(BigInteger.valueOf(p.p2())));
            }
    }

    private static int distance(Point p, Sensor s) {
        return Math.abs(p.p1() - s.x()) + Math.abs(p.p2() - s.y());
    }
}

record Point(int p1, int p2) {
}

record Sensor(int x, int y, int manhattanDistance, Beacon b) {
}

record Beacon(int x, int y) {
}