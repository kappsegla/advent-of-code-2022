package org.fungover.util;

public class ArrayUtils {

    //Returns values at position. Returns -1 when outside array
    public static byte getValue(byte[][] array, int row, int col, int errorcode) {
        if (row < 0 || col < 0 || row >= array.length || col >= array[row].length)
            return (byte) errorcode;
        return array[row][col];
    }

    public static byte getLeft(byte[][] array, int row, int col, int errorcode) {
        col = col - 1;
        if (row < 0 || col < 0 || row >= array.length || col >= array[row].length)
            return (byte) errorcode;
        return array[row][col];
    }

    public static byte getRight(byte[][] array, int row, int col, int errorcode) {
        col = col + 1;
        if (row < 0 || col < 0 || row >= array.length || col >= array[row].length)
            return (byte) errorcode;
        return array[row][col];
    }

    public static byte getUp(byte[][] array, int row, int col, int errorcode) {
        row = row - 1;
        if (row < 0 || col < 0 || row >= array.length || col >= array[row].length)
            return (byte) errorcode;
        return array[row][col];
    }

    public static byte getDown(byte[][] array, int row, int col, int errorcode) {
        row = row + 1;
        if (row < 0 || col < 0 || row >= array.length || col >= array[row].length)
            return (byte) errorcode;
        return array[row][col];
    }

    public static byte getLeftUp(byte[][] array, int row, int col, int errorcode) {
        row = row - 1;
        col = col - 1;
        if (row < 0 || col < 0 || row >= array.length || col >= array[row].length)
            return (byte) errorcode;
        return array[row][col];
    }

    public static byte getRightUp(byte[][] array, int row, int col, int errorcode) {
        row = row - 1;
        col = col + 1;
        if (row < 0 || col < 0 || row >= array.length || col >= array[row].length)
            return (byte) errorcode;
        return array[row][col];
    }

    public static byte getLeftDown(byte[][] array, int row, int col, int errorcode) {
        row = row + 1;
        col = col - 1;
        if (row < 0 || col < 0 || row >= array.length || col >= array[row].length)
            return (byte) errorcode;
        return array[row][col];
    }

    public static byte getRightDown(byte[][] array, int row, int col, int errorcode) {
        row = row + 1;
        col = col + 1;
        if (row < 0 || col < 0 || row >= array.length || col >= array[row].length)
            return (byte) errorcode;
        return array[row][col];
    }

    public static void addValueToAllPlaces(byte[][] array, int value) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] += value;
            }
        }
    }
    public static void updateAllValues(byte[][] array, java.util.function.Function<Byte, Byte> formula) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = formula.apply(array[i][j]);
            }
        }
    }


}
