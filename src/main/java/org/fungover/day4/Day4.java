package org.fungover.day4;

import java.util.Arrays;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day4 {
    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day4/day4.txt"));
//        String s = """
//                2-4,6-8
//                2-3,4-5
//                5-7,7-9
//                2-8,3-7
//                6-6,4-6
//                2-6,4-8
//                """;
        var sectors = s.lines()
                .map(l -> Arrays.stream(l.split("[-,]")).mapToInt(Integer::parseInt).toArray())
                .toList();
        //Step1
        int count = 0;
        for (var v : sectors) {
            if (overlapp(v[0], v[1], v[2], v[3])) {
                var overlapp = overlappedRange(v);
                if( overlapp[1]- overlapp[0] == v[1]-v[0] || overlapp[1]- overlapp[0] == v[3]-v[2])
                    count++;
            }
        }
        System.out.println(count);
        //Step2
        count = 0;
        for (var v : sectors) {
            if (overlapp(v[0], v[1], v[2], v[3])) {
                count++;
            }
        }
        System.out.println(count);
    }

    private static int[] overlappedRange(int[] ranges) {
        int e = Math.max(ranges[0],ranges[2]);
        int f = Math.min(ranges[1],ranges[3]);
        //if(  e <= f ) //Overlapping
        return new int[]{e,f};
    }

    private static boolean overlapp(int a, int c, int b, int d) {
        return a <= d && c >= b;
    }
}

