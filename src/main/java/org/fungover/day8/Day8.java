package org.fungover.day8;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day8 {
    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day8/day8.txt"));
//        String s = """
//                be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
//                edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
//                fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
//                fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
//                aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
//                fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
//                dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
//                bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
//                egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
//                gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce""";

        var result = s.lines()
                .map(s1 -> s1.split("\\|")[1])
                .map(String::trim)
                .flatMap(s2 -> Arrays.stream(s2.split(" ")))
                .map(Day8::stringToNumber)
                .filter(v -> v != 0)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        System.out.println(result.values().stream().mapToLong(value -> value).sum());

        //Find all uniqe numbers, 1,4,7,8
        var mappingList = s.lines();

        //How many numbers uses each segment
        //        a: 8     8888
        //        b: 6    6    8
        //        c: 8    6    8
        //        d: 7     7777
        //        e: 4    4    9
        //        f: 9    4    9
        //        g: 7     7777

        //Sum of segmentValues for each number 0-9
        //int[] segmentScores = {42, 17, 34, 39, 30, 37, 41, 25, 49, 38};

        Map<Integer, Integer> scoreToNumber = new HashMap<>();
        scoreToNumber.put(42,0);
        scoreToNumber.put(17,1);
        scoreToNumber.put(34,2);
        scoreToNumber.put(39,3);
        scoreToNumber.put(30,4);
        scoreToNumber.put(37,5);
        scoreToNumber.put(41,6);
        scoreToNumber.put(25,7);
        scoreToNumber.put(49,8);
        scoreToNumber.put(45,9);

        int totalSum = 0;

        for(String line : s.lines().toList()) {
            var values = line.split("\\|");
            //Calculate occurande for different chars
            var charCount =  values[0]
                    .chars()
                    .filter(Character::isLetter)
                    .mapToObj(c -> (char) c)
                    .collect( Collectors.groupingBy(Function.identity(),Collectors.counting()));

            System.out.println(charCount);

            //For all outputs find the sum for characters by getting it from charCount
            //Take the sum and look up the number from scoreToNumber
            var fourDigits = values[1].trim().split("\s");

            String number = "";
            for( var d : fourDigits) {
                //Calculate segmentSum for each digit
                var segmentScore = d.chars()
                        .mapToObj(c->(char)c)
                        .map(charCount::get)
                        .mapToInt(Long::intValue)
                        .sum();
                //Lookup digit from scoreToNumber
                number += scoreToNumber.get(segmentScore);
            }
            System.out.println(number);
            totalSum += Integer.parseInt(number);
        }
        System.out.println(totalSum);


    }


    private static int stringToNumber(String s) {
        var length = s.length();
        return switch (length) {
            case 2 -> 1;
            case 3 -> 7;
            case 4 -> 4;
            case 7 -> 8;
            default -> 0;
        };


    }
}
