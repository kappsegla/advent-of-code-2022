package org.fungover.util;

import java.util.ArrayList;
import java.util.List;

public class Strings {

    public static String removeAllSpaces(String input) {
        return input.replaceAll(" ", "");
    }

    public static String doubleSpaceToSpace(String input) {
        return input.replaceAll("\\s{2}", " ");
    }


    //line1
    //line2
    //line3
    //
    //line4
    //line5
    public static List<List<String>> stringToListOfListOfStrings(String s){
        List<List<String>> initial = new ArrayList<>();
        initial.add(new ArrayList<>());
        List<List<String>> result = s.lines().reduce(initial, (subtotal, element) -> {
            if (element.trim().isEmpty()) {
                subtotal.add(new ArrayList<>());
            } else {
                subtotal.get(subtotal.size() - 1).add(element);
            }
            return subtotal;
        }, (list1, list2) -> List.of());
        return result;
    }

    //2345
    //4566
    //4544
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
    //abcd
    //efgh
    //SEga
    public static char[][] stringValuesTo2DCharArray(String s){
        var rows = (int) s.lines().count();
        var cols = s.lines().findAny().get().length();
        char[][] array = new char[rows][cols];
        int count = 0;
        //Fill array
        for (var line : s.lines().toList()) {
            for (var height : line.chars().toArray()) {
                array[count / cols][count % cols] = (char) height;
                count++;
            }
        }
        return array;
    }

}
