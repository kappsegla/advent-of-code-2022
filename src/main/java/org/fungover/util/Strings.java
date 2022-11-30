package org.fungover.util;

import java.util.List;

public class Strings {

    public static String removeAllSpaces(String input) {
        return input.replaceAll(" ", "");
    }

    public static String doubleSpaceToSpace(String input) {
        return input.replaceAll("\\s{2}", " ");
    }

    public static byte[][] stringValuesTo2DByteArray(String s){
        var rows = (int) s.lines().count();
        var cols = s.lines().findAny().get().length();
        byte[][] array = new byte[rows][cols];
        int count = 0;
        //Fill array
        for (var line : s.lines().toList()) {
            for (var height : line.chars().toArray()) {
                array[count / cols][count % cols] = (byte) Character.getNumericValue(height);
                count++;
            }
        }
        return array;
    }
}
