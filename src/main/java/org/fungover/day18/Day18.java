package org.fungover.day18;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day18 {
    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day18/day18.txt"));
//        String s = """
//                2,2,2
//                1,2,2
//                3,2,2
//                2,1,2
//                2,3,2
//                2,2,1
//                2,2,3
//                2,2,4
//                2,2,6
//                1,2,5
//                3,2,5
//                2,1,5
//                2,3,5
//                """;

        var cubes = s.lines().map(Point3D::of).collect(Collectors.toSet());

        long count = 0;
        for (var cube : cubes) {
            count += countExposedSides(cube, cubes);
        }
        System.out.println(count);

        //Step2
        //Find min, max values for each dimension and add empty space around
        var minx = cubes.stream().mapToLong(Point3D::getX).min().getAsLong() - 1;
        var maxx = cubes.stream().mapToLong(Point3D::getX).max().getAsLong() + 1;
        var miny = cubes.stream().mapToLong(Point3D::getY).min().getAsLong() - 1;
        var maxy = cubes.stream().mapToLong(Point3D::getY).max().getAsLong() + 1;
        var minz = cubes.stream().mapToLong(Point3D::getZ).min().getAsLong() - 1;
        var maxz = cubes.stream().mapToLong(Point3D::getZ).max().getAsLong() + 1;

        //Total number of lava cubes
        var lavaCubes = cubes.size();

        //Total size of bounding box with + 1 in each direction +/-
        var boundingBox = (maxx - minx + 1) * (maxy - miny + 1) * (maxz - minz + 1);

        //Fill empty cubes starting at empty position
        Point3D start = Point3D.of(minx, miny, minz);

        Set<Point3D> air = new HashSet<>();
        floodFill(start, air, cubes, minx, maxx, miny, maxy, minz, maxz);

        Set<Point3D> allCubes = new HashSet<>();
        //Fill
        for (long x = minx; x <= maxx; x++)
            for (long y = 0; y <= maxy; y++)
                for (long z = 0; z <= maxz; z++)
                    allCubes.add(Point3D.of(x, y, z));

        allCubes.removeAll(air);
        allCubes.removeAll(cubes);

        var holeCount = 0;
        for (var cube : allCubes) {
            holeCount += countExposedSides(cube, allCubes);
        }
        System.out.println(holeCount);
        System.out.println(count - holeCount);
    }

    private static void floodFill(Point3D cube, Set<Point3D> air, Set<Point3D> lava, long minx, long maxx, long miny, long maxy, long minz, long maxz) {
        if (cube.x < minx || cube.x > maxx ||
                cube.y < miny || cube.y > maxy ||
                cube.z < minz || cube.z > maxz)
            return;

        if (air.contains(cube) || lava.contains(cube))
            return;

        air.add(cube);

        floodFill(Point3D.of(cube.x - 1, cube.y, cube.z), air, lava, minx, maxx, miny, maxy, minz, maxz);
        floodFill(Point3D.of(cube.x + 1, cube.y, cube.z), air, lava, minx, maxx, miny, maxy, minz, maxz);
        floodFill(Point3D.of(cube.x, cube.y - 1, cube.z), air, lava, minx, maxx, miny, maxy, minz, maxz);
        floodFill(Point3D.of(cube.x, cube.y + 1, cube.z), air, lava, minx, maxx, miny, maxy, minz, maxz);
        floodFill(Point3D.of(cube.x, cube.y, cube.z - 1), air, lava, minx, maxx, miny, maxy, minz, maxz);
        floodFill(Point3D.of(cube.x, cube.y, cube.z + 1), air, lava, minx, maxx, miny, maxy, minz, maxz);
    }


    private static long countExposedSides(Point3D cube, Set<Point3D> cubes) {
        int count = 6;
        //Walk in all 6 directions and see if a cube exists at that direction
        if (cubes.contains(Point3D.of(cube.x + 1, cube.y, cube.z))) count--;
        if (cubes.contains(Point3D.of(cube.x - 1, cube.y, cube.z))) count--;
        if (cubes.contains(Point3D.of(cube.x, cube.y + 1, cube.z))) count--;
        if (cubes.contains(Point3D.of(cube.x, cube.y - 1, cube.z))) count--;
        if (cubes.contains(Point3D.of(cube.x, cube.y, cube.z + 1))) count--;
        if (cubes.contains(Point3D.of(cube.x, cube.y, cube.z - 1))) count--;

        return count;
    }
}

class Point3D {
    long x;
    long y;
    long z;

    private Point3D(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Point3D of(long x, long y, long z) {
        return new Point3D(x, y, z);
    }

    public static Point3D of(String values) {
        var coords = Arrays.stream(values.split(",")).mapToLong(Long::parseLong).toArray();
        return new Point3D(coords[0], coords[1], coords[2]);
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public long getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point3D point3D = (Point3D) o;

        if (x != point3D.x) return false;
        if (y != point3D.y) return false;
        return z == point3D.z;
    }

    @Override
    public int hashCode() {
        int result = (int) (x ^ (x >>> 32));
        result = 31 * result + (int) (y ^ (y >>> 32));
        result = 31 * result + (int) (z ^ (z >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Point3D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
