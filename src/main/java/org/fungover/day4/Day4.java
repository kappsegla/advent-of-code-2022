package org.fungover.day4;

import org.fungover.util.FileReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.function.Predicate.not;
import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;


public class Day4 {

    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day3/day3.txt"));
        var numbers = Arrays.stream(FileReader.listFromFile(resourceStringToPath("/day4/day4.txt")).get(0).split(",")).mapToInt(Integer::parseInt).toArray();
        List<String> boardsData = FileReader.streamFromFile(resourceStringToPath("/day4/day4.txt")).skip(1).filter(not(String::isEmpty)).toList();

//        var numbers = Arrays.stream("7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1".split(",")).mapToInt(Integer::parseInt).toArray();
//
//        List<String> boardsData = """
//                22 13 17 11  0
//                 8  2 23  4 24
//                21  9 14 16  7
//                 6 10  3 18  5
//                 1 12 20 15 19
//
//                 3 15  0  2 22
//                 9 18 13 17  5
//                19  8  7 25 23
//                20 11 10 24  4
//                14 21 16 12  6
//
//                14 21 17 24  4
//                10 16 15  9 19
//                18  8 23 26 20
//                22 11 13  6  5
//                 2  0 12  3  7
//                """.lines().filter(not(String::isEmpty)).toList();

        List<Board> boards = new ArrayList<>();

        for (int i = 0; i < boardsData.size(); i += 5) {
            var board = new Board();
            board.board[0] = Arrays.stream(boardsData.get(i).replaceAll("\\s{2}", " ").replaceAll("^\s", "").split(" ")).mapToInt(Integer::parseInt).toArray();
            board.board[1] = Arrays.stream(boardsData.get(i + 1).replaceAll("\\s{2}", " ").replaceAll("^\s", "").split(" ")).mapToInt(Integer::parseInt).toArray();
            board.board[2] = Arrays.stream(boardsData.get(i + 2).replaceAll("\\s{2}", " ").replaceAll("^\s", "").split(" ")).mapToInt(Integer::parseInt).toArray();
            board.board[3] = Arrays.stream(boardsData.get(i + 3).replaceAll("\\s{2}", " ").replaceAll("^\s", "").split(" ")).mapToInt(Integer::parseInt).toArray();
            board.board[4] = Arrays.stream(boardsData.get(i + 4).replaceAll("\\s{2}", " ").replaceAll("^\s", "").split(" ")).mapToInt(Integer::parseInt).toArray();
            boards.add(board);
        }

        boolean[] finishedBoards = new boolean[boards.size()];
        for (var number : numbers) {
            //Mark all boards
            for (var b : boards) {
                b.mark(number);
            }
            //Check for winner
            for (int i = 0; i < boards.size(); i++) {
                if ( !finishedBoards[i] &&  boards.get(i).checkForBingo()) {
                    finishedBoards[i] = true;
                    System.out.println("We have a winner in board number: " + (i + 1));
                    var sum = boards.get(i).calculateSum();
                    System.out.println("Board sum: " + sum);
                    System.out.println("Latest number: " + number);
                    System.out.println("Code: " + number * sum);
                }
            }
        }
    }
}

class Board {
    int[][] board = new int[5][];

    public void mark(int number) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == number)
                    board[i][j] = -1;
            }
        }
    }

    public boolean checkForBingo() {

        //Check if whole row has bingo
        for (int[] ints : board) {
            var bingo = 5;
            for (int anInt : ints) {
                if( anInt == -1)
                    bingo -= 1;
            }
            if (bingo == 0)
                return true;
        }
        //Check if whole column has bingo
        for (int i = 0; i < board.length; i++) {
            var bingo = 5;
            for (int j = 0; j < board[i].length; j++) {
                if( board[j][i] == -1)
                    bingo -= 1;
            }
            if (bingo == 0)
                return true;
        }
        return false;
    }

    public int calculateSum() {
        int sum = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != -1)
                    sum += board[i][j];
            }
        }
        return sum;
    }

}