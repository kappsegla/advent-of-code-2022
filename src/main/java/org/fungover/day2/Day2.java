package org.fungover.day2;

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
    }

    private static int calculateScoreToWin(String choices) {
        String o = choices.split(" ")[0];
        String y = choices.split(" ")[1];
        int op = convertToNumber(o);
        int yp = convertToNumberBestChoice(op, y);
        return yp + yourpoints(op, yp);
    }

    private static int convertToNumberBestChoice(int op, String yc) {
        if( yc.equals("X")  ) { //We should loose
            if( op == ROCK)
                return SCISSORS;
            if( op == PAPER)
                return ROCK;
            if( op == SCISSORS)
                return PAPER;
        }
        if( yc.equals("Y")  ) { //We should draw
            return op;
        }
        if( yc.equals("Z")  ) { //We should win
            if( op == ROCK)
                return PAPER;
            if( op == PAPER)
                return SCISSORS;
            if( op == SCISSORS)
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
        if( op == yp) //Draw
            return 3;
        if( op == ROCK && yp == PAPER) //Rock vs Paper
            return 6;
        if( op == PAPER && yp == SCISSORS) //Paper vs Scissors
            return 6;
        if( op == SCISSORS && yp == ROCK) //Scissors vs Rock
            return 6;
        return 0;
    }

    private static int convertToNumber(String o) {
        return switch (o){
            case "A","X"->ROCK;
            case "B","Y"->PAPER;
            case "C","Z"->SCISSORS;
            default -> throw new RuntimeException();
        };
    }
}
