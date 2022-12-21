package org.fungover.day18;

import java.util.Arrays;
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

    }

    private static long countExposedSides(Point3D cube, Set<Point3D> cubes) {
        int count = 6;
        //Walk in all 6 directions and see if a cube exists at that direction
        if( cubes.contains(Point3D.of(cube.x+1, cube.y, cube.z))) count--;
        if( cubes.contains(Point3D.of(cube.x-1, cube.y, cube.z))) count--;
        if( cubes.contains(Point3D.of(cube.x, cube.y+1, cube.z))) count--;
        if( cubes.contains(Point3D.of(cube.x, cube.y-1, cube.z))) count--;
        if( cubes.contains(Point3D.of(cube.x, cube.y, cube.z+1))) count--;
        if( cubes.contains(Point3D.of(cube.x, cube.y, cube.z-1))) count--;

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
        return new Point3D(x,y,z);
    }

    public static Point3D of(String values) {
        var coords = Arrays.stream(values.split(",")).mapToLong(Long::parseLong).toArray();
        return new Point3D(coords[0], coords[1], coords[2]);
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
