package org.fungover.day3;

import org.fungover.day2.SubPos;
import org.fungover.day2.SubPosAim;

import java.util.ArrayList;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day3 {
    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day3/day3.txt"));

        String input = """
                00100
                11110
                10110
                10111
                10101
                01111
                00111
                11100
                10000
                11001
                00010
                01010""";

        int gamma = calcGamma(s);
        int epsilon = calcEpsilon(s);
        System.out.println(gamma);
        System.out.println(epsilon);
        System.out.println(gamma * epsilon);

        int o2 = calcO2(s);  //23
        int co2 = calcCo2(s);  //10

        System.out.println(o2);
        System.out.println(co2);
        System.out.println(co2 * o2);

    }

    private static int calcCo2(String input) {
        var lines = new ArrayList<>(input.lines().toList());

        for (int i = 0; i < lines.get(0).length(); i++) {
            if (lines.size() == 1)
                break;
            var mostCommon = 0;
            for (String s : lines) {
                if (s.charAt(i) == '1')
                    mostCommon++;
                else
                    mostCommon--;
            }
            char c = mostCommon >= 0 ? '1' : '0';
            int pos = i;
            lines.removeIf(v -> v.charAt(pos) == c);
        }

        return Integer.parseInt(lines.get(0), 2);

    }

    private static int calcO2(String input) {
        var lines = new ArrayList<>(input.lines().toList());

        for (int i = 0; i < lines.get(0).length(); i++) {
            if (lines.size() == 1)
                break;
            var mostCommon = 0;
            for (String s : lines) {
                if (s.charAt(i) == '1')
                    mostCommon++;
                else
                    mostCommon--;
            }
            char c = mostCommon >= 0 ? '1' : '0';
            int pos = i;
            lines.removeIf(v -> v.charAt(pos) != c);
        }

        return Integer.parseInt(lines.get(0), 2);
    }


    private static int calcEpsilon(String input) {
        var lines = input.lines().toList();
        String result = "";
        for (int i = 0; i < lines.get(0).length(); i++) {
            var mostCommon = 0;
            for (String s : lines) {
                if (s.charAt(i) == '1')
                    mostCommon++;
                else
                    mostCommon--;
            }
            result += mostCommon < 0 ? "1" : "0";
        }
        return Integer.parseInt(result, 2);
    }


    private static int calcGamma(String input) {
        var lines = input.lines().toList();
        String result = "";
        for (int i = 0; i < lines.get(0).length(); i++) {
            var mostCommon = 0;
            for (String s : lines) {
                if (s.charAt(i) == '1')
                    mostCommon++;
                else
                    mostCommon--;
            }
            result += mostCommon > 0 ? "1" : "0";
        }
        return Integer.parseInt(result, 2);
    }
}
