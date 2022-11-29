package org.fungover.day7;

import java.util.Arrays;
import java.util.stream.IntStream;

import static java.util.Comparator.comparingInt;
import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day7 {
    //Part Two
        public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day7/day7.txt"));
//        String s = "16,1,2,0,4,2,7,1,2,14";
        var pos = Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).toArray();

        int maxPos = Arrays.stream(pos).max().getAsInt();

        int[] fuelCost = new int[maxPos];

        for (int i = 0; i < maxPos; i++) {
            int level = i;
            fuelCost[i] = Arrays.stream(pos).map(l-> Math.abs(l - level) ).map(Day7::sumOfSeries).sum();
            System.out.println("FuelCost for aligning on level " + i + ": " + fuelCost[i]);
        }


        int minIndex = IntStream.range(0,fuelCost.length).boxed()
                .min( comparingInt(p -> fuelCost[p]))
                .get();
        System.out.println("Fuelcost for level " + minIndex + ": " + Arrays.stream(fuelCost).min().getAsInt() );
    }

    private static int sumOfSeries(int n){
            return n * (n + 1) / 2;
    }

    //Part One
//    public static void main(String[] args) {
////        String s = stringFromFile(resourceStringToPath("/day7/day7.txt"));
//        String s = "16,1,2,0,4,2,7,1,2,14";
//        var pos = Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).toArray();
//
//        int maxPos = Arrays.stream(pos).max().getAsInt();
//
//        int[] fuelCost = new int[maxPos];
//
//        for (int i = 0; i < maxPos; i++) {
//            int level = i;
//            fuelCost[i] = Arrays.stream(pos).map(l-> Math.abs(l - level)).sum();
//            System.out.println("FuelCost for aligning on level " + i + ": " + fuelCost[i]);
//        }
//
//
//        int minIndex = IntStream.range(0,fuelCost.length).boxed()
//                .min( comparingInt(p -> fuelCost[p]))
//                .get();
//        System.out.println("Fuelcost for level " + minIndex + ": " + Arrays.stream(fuelCost).min().getAsInt() );
//    }
}
