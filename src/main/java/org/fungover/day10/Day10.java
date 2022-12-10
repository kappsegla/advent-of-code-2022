package org.fungover.day10;

import java.util.List;
import java.util.stream.Stream;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day10 {

    static int rX = 1;
    static int pc = 0;
    static int signalStrengthSum = 0;

    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day10/day10.txt"));
        //String s = stringFromFile(resourceStringToPath("/day10/test.txt"));

        for (var instruction : s.lines().toList()) {
            if (instruction.startsWith("noop"))
                cycle();
            else if (instruction.startsWith("addx")) cycleX(instruction);
        }
        System.out.println(signalStrengthSum);
    }

    public static void cycleX(String instruction){
        cycle();
        cycle();
        rX += Integer.parseInt(instruction.split(" ")[1]);
    }

    public static void cycle() {
        pc++;
        if (pc == 20 || pc == 60 || pc == 100 || pc == 140 || pc == 180 || pc == 220) {
            signalStrengthSum += pc * rX;
            System.out.println(pc + ":" + rX);
        }
    }
}
