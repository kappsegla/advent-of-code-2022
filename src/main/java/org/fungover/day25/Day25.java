package org.fungover.day25;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day25 {
    public static void main(String[] args) {

        String s = stringFromFile(resourceStringToPath("/day25/day25.txt"));
//        String s = """
//                1=-0-2
//                12111
//                2=0=
//                21
//                2=01
//                111
//                20012
//                112
//                1=-1=
//                1-12
//                12
//                1=
//                122
//                """;

        //SNAFU
        System.out.println(snafuToDecimal("1"));
        System.out.println(snafuToDecimal("2"));
        System.out.println(snafuToDecimal("1="));
        System.out.println(snafuToDecimal("1-"));
        System.out.println(snafuToDecimal("10"));
        System.out.println(snafuToDecimal("11"));
        System.out.println(snafuToDecimal("12"));
        System.out.println(snafuToDecimal("2="));
        System.out.println(snafuToDecimal("2-"));
        System.out.println(snafuToDecimal("20"));
        System.out.println(snafuToDecimal("1=0"));
        System.out.println(snafuToDecimal("1-0"));
        System.out.println(snafuToDecimal("1121-1110-1=0")); //314159265

        System.out.println(decimalToSnafu(1));
        System.out.println(decimalToSnafu(2));
        System.out.println(decimalToSnafu(3));
        System.out.println(decimalToSnafu(4));
        System.out.println(decimalToSnafu(5));
        System.out.println(decimalToSnafu(6));
        System.out.println(decimalToSnafu(7));
        System.out.println(decimalToSnafu(8));
        System.out.println(decimalToSnafu(9));
        System.out.println(decimalToSnafu(10));
        System.out.println(decimalToSnafu(15));
        System.out.println(decimalToSnafu(25));
        System.out.println(decimalToSnafu(2022));
        System.out.println(decimalToSnafu(4890));

        var sum = s.lines().mapToLong(Day25::snafuToDecimal).sum();
        System.out.println(sum);
        System.out.println(decimalToSnafu(sum));

    }

    public static long snafuToDecimal(String snafu) {
        long position = 1;
        long total = 0;
        for (int i = snafu.length() - 1; i >= 0; i--) {
            var c = snafu.charAt(i);
            switch (c) {
                case '2' -> total += position * 2L;
                case '1' -> total += position;
                case '-' -> total -= position;
                case '=' -> total -= position * 2L;
            }
            position *= 5;
        }
        return total;
    }

    //Recursive version
    public static String numBuilder(long num) {
        if (num == 0L)
            return "";
        return switch ((int) (num % 5)) {
            case 0 -> numBuilder(num / 5L) + "0";
            case 1 -> numBuilder(num / 5L) + "1";
            case 2 -> numBuilder(num / 5L) + "2";
            case 3 -> numBuilder((num + 2) / 5L) + "=";
            case 4 -> numBuilder((num + 1) / 5L) + "-";
            default -> null;
        };
    }

    //Using StringBuilder.reverse
    public static String decimalToSnafu(long decimal) {
        StringBuilder builder = new StringBuilder();
        while (decimal > 0) {
            switch ((int) (decimal % 5)) {
                case 0 -> builder.append("0");
                case 1 -> builder.append("1");
                case 2 -> builder.append("2");
                case 3 -> {
                    builder.append("=");
                    decimal += 5;
                }
                case 4 -> {
                    builder.append("-");
                    decimal += 5;
                }
                default -> throw new IllegalStateException();
            }
            decimal /= 5L;
        }
        return builder.reverse().toString();
    }
}
