package org.fungover.day12;

import org.fungover.util.Strings;

import java.util.Arrays;

public class Day12 {

    public static void main(String[] args) {
    String s = """
            Sabqponm
            abcryxxl
            accszExk
            acctuvwj
            abdefghi
            """;

    var array = Strings.stringValuesTo2DCharArray(s);

    Point start = null;
    Point end = null;

    //Find Start Square
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if( array[i][j] == 'S')
                    start = new Point(i,j);
                if( array[i][j] == 'E')
                    end = new Point(i,j);
            }
        }
        System.out.println(start);
        System.out.println(end);


    }

}

record Point(int x, int y){}
