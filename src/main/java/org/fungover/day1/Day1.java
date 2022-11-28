package org.fungover.day1;

import org.fungover.util.FileReader;

import javax.swing.*;

import java.util.List;

import static org.fungover.util.FileReader.*;

public class Day1 {
    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day1/day1.txt"));
        System.out.println(countSlidingWindow(s));
    }

    public static long count(String input) {
        int count = -1;
        int previous = 0;

        for (int i : input.lines().mapToInt(Integer::parseInt).toArray()) {

            if (i > previous)
                count++;
            previous = i;
        }
        return count;
    }

    public static long countSlidingWindow(String input) {

        var values = input.lines().mapToInt(Integer::parseInt).toArray();
        var slidingValues = new int[values.length - 2];

        for (int i = 0; i < values.length - 2; i++) {
            slidingValues[i] = values[i] + values[i + 1] + values[i + 2];
            System.out.println(slidingValues[i]);
        }

        int count = -1;
        int previous = 0;

        for (int i : slidingValues) {
            if (i > previous)
                count++;
            previous = i;
        }
        return count;
    }
}
