package org.fungover.day6;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day6 {

    public static final int DAYS = 256;

    public static void main(String[] args) throws IOException {
        String s = stringFromFile(resourceStringToPath("/day6/day6.txt"));
//        String s = "3,4,3,1,2";
        var n = s.split(",");
        long[] fishList = new long[9];

        for (String value : n) {
            var place = Integer.parseInt(value);
            fishList[place]++;
        }
        System.out.println(Arrays.toString(fishList));

        for (int i = 0; i < DAYS; i++) {

            long newFish = fishList[0];
            for (int j = 1; j < fishList.length; j++) {
                fishList[j-1] = fishList[j];
            }
            fishList[6] += newFish;
            fishList[8] = newFish;

            long count = Arrays.stream(fishList).sum();
            System.out.println("After " + (i + 1) + "days: " + count);
        }
    }
}

