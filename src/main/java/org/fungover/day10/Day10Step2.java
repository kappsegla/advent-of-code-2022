package org.fungover.day10;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

import java.util.Arrays;

public class Day10Step2 {

    static int rX = 1;
    static int pc = 0;
    static int signalStrengthSum = 0;
    static char[] crt = new char[240];

    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day10/day10.txt"));
        //String s = stringFromFile(resourceStringToPath("/day10/test.txt"));

        Arrays.fill(crt, '.');

        for (var instruction : s.lines().toList()) {
            if (instruction.startsWith("noop"))
                cycle();
            else if (instruction.startsWith("addx")) cycleX(instruction);
        }

        for (int j = 0; j < crt.length; j++) {
            if (j % 40 == 0)
                System.out.println();
            System.out.print(crt[j]);
        }
    }


    public static void cycleX(String instruction) {
        cycle();
        cycle();
        rX += Integer.parseInt(instruction.split(" ")[1]);
    }

    public static void cycle() {
        //crt position
        if (rX - 1 == pc % 40 || rX == pc % 40 || rX + 1 == pc % 40)
            crt[pc] = '#';
        pc++;
        if (pc == 20 || pc == 60 || pc == 100 || pc == 140 || pc == 180 || pc == 220) {
            signalStrengthSum += pc * rX;
            System.out.println(pc + ":" + rX);
        }
    }
}
