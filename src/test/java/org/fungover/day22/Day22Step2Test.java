package org.fungover.day22;

import static org.fungover.day22.Day22Step2.DIR.*;
import static org.fungover.day22.Day22Step2.DIR.RIGHT;
import static org.junit.jupiter.api.Assertions.*;

class Day22Step2Test {
    @org.junit.jupiter.api.Test
    void verifyNextCubePos() {
        // ---------------------- A - F BORDER
        assertEquals(new Day22Step2.PointAndDir(150, 0, RIGHT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(0, 50, UP)));
        assertEquals(new Day22Step2.PointAndDir(151, 0, RIGHT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(0, 51, UP)));
        assertEquals(new Day22Step2.PointAndDir(199, 0, RIGHT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(0, 99, UP)));

        assertEquals(new Day22Step2.PointAndDir(0, 50, DOWN), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(150, 0, LEFT)));
        assertEquals(new Day22Step2.PointAndDir(0, 51, DOWN), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(151, 0, LEFT)));
        assertEquals(new Day22Step2.PointAndDir(0, 99, DOWN), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(199, 0, LEFT)));


        // ---------------------- A - E BORDER
        assertEquals(new Day22Step2.PointAndDir(149, 0, RIGHT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(0, 50, LEFT)));
        assertEquals(new Day22Step2.PointAndDir(148, 0, RIGHT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(1, 50, LEFT)));
        assertEquals(new Day22Step2.PointAndDir(100, 0, RIGHT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(49, 50, LEFT)));

        assertEquals(new Day22Step2.PointAndDir(0, 50, RIGHT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(149, 0, LEFT)));
        assertEquals(new Day22Step2.PointAndDir(1, 50, RIGHT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(148, 0, LEFT)));
        assertEquals(new Day22Step2.PointAndDir(49, 50, RIGHT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(100, 0, LEFT)));


        // ---------------------- B - F BORDER
        assertEquals(new Day22Step2.PointAndDir(199, 0, UP), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(0, 100, UP)));
        assertEquals(new Day22Step2.PointAndDir(199, 1, UP), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(0, 101, UP)));
        assertEquals(new Day22Step2.PointAndDir(199, 49, UP), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(0, 149, UP)));

        assertEquals(new Day22Step2.PointAndDir(0, 100, DOWN), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(199, 0, DOWN)));
        assertEquals(new Day22Step2.PointAndDir(0, 101, DOWN), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(199, 1, DOWN)));
        assertEquals(new Day22Step2.PointAndDir(0, 149, DOWN), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(199, 49, DOWN)));

        // ---------------------- B - D BORDER
        assertEquals(new Day22Step2.PointAndDir(149, 99, LEFT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(0, 149, RIGHT)));
        assertEquals(new Day22Step2.PointAndDir(148, 99, LEFT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(1, 149, RIGHT)));
        assertEquals(new Day22Step2.PointAndDir(100, 99, LEFT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(49, 149, RIGHT)));

        assertEquals(new Day22Step2.PointAndDir(49, 149, LEFT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(100, 99, RIGHT)));
        assertEquals(new Day22Step2.PointAndDir(48, 149, LEFT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(101, 99, RIGHT)));
        assertEquals(new Day22Step2.PointAndDir(0, 149, LEFT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(149, 99, RIGHT)));

        // ---------------------- B - C BORDER
        assertEquals(new Day22Step2.PointAndDir(50, 99, LEFT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(49, 100, DOWN)));
        assertEquals(new Day22Step2.PointAndDir(51, 99, LEFT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(49, 101, DOWN)));
        assertEquals(new Day22Step2.PointAndDir(99, 99, LEFT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(49, 149, DOWN)));

        assertEquals(new Day22Step2.PointAndDir(49, 100, UP), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(50, 99, RIGHT)));
        assertEquals(new Day22Step2.PointAndDir(49, 101, UP), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(51, 99, RIGHT)));
        assertEquals(new Day22Step2.PointAndDir(49, 149, UP), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(99, 99, RIGHT)));

        // ---------------------- E - C BORDER
        assertEquals(new Day22Step2.PointAndDir(100, 0, DOWN), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(50, 50, LEFT)));
        assertEquals(new Day22Step2.PointAndDir(100, 1, DOWN), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(51, 50, LEFT)));
        assertEquals(new Day22Step2.PointAndDir(100, 49, DOWN), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(99, 50, LEFT)));

        assertEquals(new Day22Step2.PointAndDir(50, 50, RIGHT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(100, 0, UP)));
        assertEquals(new Day22Step2.PointAndDir(51, 50, RIGHT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(100, 1, UP)));
        assertEquals(new Day22Step2.PointAndDir(99, 50, RIGHT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(100, 49, UP)));

        // ---------------------- F - D BORDER
        assertEquals(new Day22Step2.PointAndDir(150, 49, LEFT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(149, 50, DOWN)));
        assertEquals(new Day22Step2.PointAndDir(151, 49, LEFT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(149, 51, DOWN)));
        assertEquals(new Day22Step2.PointAndDir(199, 49, LEFT), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(149, 99, DOWN)));

        assertEquals(new Day22Step2.PointAndDir(149, 50, UP), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(150, 49, RIGHT)));
        assertEquals(new Day22Step2.PointAndDir(149, 51, UP), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(151, 49, RIGHT)));
        assertEquals(new Day22Step2.PointAndDir(149, 99, UP), Day22Step2.nextCubePos(new Day22Step2.PointAndDir(199, 49, RIGHT)));
    }

}