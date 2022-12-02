package org.fungover.day2;

import java.util.HashMap;
import java.util.Map;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day2 {
    public static final int ROCK = 1;
    public static final int PAPER = 2;
    public static final int SCISSORS = 3;


    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day2/day2.txt"));
//        String s = """
//                A Y
//                B X
//                C Z
//                """;
        var sum = s.lines().mapToInt(Day2::calculateScore).sum();
        System.out.println(sum);  //13565
        sum = s.lines().mapToInt(Day2::calculateScoreToWin).sum();
        System.out.println(sum);  //12424

        //Using lookup map
        Map<String, Integer> codeToPoints = new HashMap<>() {{
            put("A X", 4);
            put("A Y", 8);
            put("A Z", 3);
            put("B X", 1);
            put("B Y", 5);
            put("B Z", 9);
            put("C X", 7);
            put("C Y", 2);
            put("C Z", 6);
        }};
        sum = s.lines().mapToInt(codeToPoints::get).sum();
        System.out.println(sum);

        //Using lookup map
        Map<String, Integer> codeToPointsStep2 = new HashMap<>() {{
            put("A X", 3); //Loose
            put("A Y", 4); //Draw
            put("A Z", 8); //Win
            put("B X", 1);
            put("B Y", 5);
            put("B Z", 9);
            put("C X", 2);
            put("C Y", 6);
            put("C Z", 7);
        }};
        sum = s.lines().mapToInt(codeToPointsStep2::get).sum();
        System.out.println(sum);
    }

    private static int calculateScoreToWin(String choices) {
        String o = choices.split(" ")[0];
        String y = choices.split(" ")[1];
        int op = convertToNumber(o);
        int yp = convertToNumberBestChoice(op, y);
        return yp + yourpoints(op, yp);
    }

    private static int convertToNumberBestChoice(int op, String yc) {
        if (yc.equals("X")) { //We should loose
            if (op == ROCK)
                return SCISSORS;
            if (op == PAPER)
                return ROCK;
            if (op == SCISSORS)
                return PAPER;
        }
        if (yc.equals("Y")) { //We should draw
            return op;
        }
        if (yc.equals("Z")) { //We should win
            if (op == ROCK)
                return PAPER;
            if (op == PAPER)
                return SCISSORS;
            if (op == SCISSORS)
                return ROCK;
        }
        throw new RuntimeException();
    }

    static int calculateScore(String choices) {
        String o = choices.split(" ")[0];
        String y = choices.split(" ")[1];
        int op = convertToNumber(o);
        int yp = convertToNumber(y);
        return yp + yourpoints(op, yp);
    }

    private static int yourpoints(int op, int yp) {
        if (op == yp) //Draw
            return 3;
        if (op == ROCK && yp == PAPER) //Rock vs Paper
            return 6;
        if (op == PAPER && yp == SCISSORS) //Paper vs Scissors
            return 6;
        if (op == SCISSORS && yp == ROCK) //Scissors vs Rock
            return 6;
        return 0;
    }

    private static int convertToNumber(String o) {
        return switch (o) {
            case "A", "X" -> ROCK;
            case "B", "Y" -> PAPER;
            case "C", "Z" -> SCISSORS;
            default -> throw new RuntimeException();
        };
    }
}
