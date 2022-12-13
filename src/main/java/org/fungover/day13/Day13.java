package org.fungover.day13;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;
import static org.fungover.util.Strings.stringToListOfListOfStrings;

public class Day13 {
    public static void main(String[] args) {
        //String s = stringFromFile(resourceStringToPath("/day13/day13.txt"));
        String s = """
                [1,1,3,1,1]
                [1,1,5,1,1]
                                
                [[1],[2,3,4]]
                [[1],4]
                                
                [9]
                [[8,7,6]]
                                
                [[4,4],4,4]
                [[4,4],4,4,4]
                                
                [7,7,7,7]
                [7,7,7]
                                
                []
                [3]
                                
                [[[]]]
                [[]]
                                
                [1,[2,[3,[4,[5,6,7]]]],8,9]
                [1,[2,[3,[4,[5,6,0]]]],8,9]
                """;

        var lines = stringToListOfListOfStrings(s);

        Set<Integer> rightOrderPairs = new HashSet<>();
        for (int i = 0; i < lines.size(); i++) {
            List<String> pair = lines.get(i);
            if (isPairInRightOrder(pair.get(0), pair.get(1)))
                rightOrderPairs.add(i + 1);
        }
        System.out.println(rightOrderPairs.stream().mapToInt(i -> i).sum());

    }

    private static boolean isPairInRightOrder(String a, String b) {
        var left = a.replaceAll("[\\[\\]]", "").split(",");
        var right = b.replaceAll("[\\[\\]]", "").split(",");

        int c = 0;
        while (c < left.length && c < right.length) {
            if( left[0].isEmpty())
                return true;
            int v1 = Integer.parseInt(left[c]);
            int v2 = Integer.parseInt(right[c]);

            if (v1 > v2)
                return false;
            if (v1 < v2)
                return true;
            c++;
        }
        if( left.length < right.length)
            return true;
        return false;
    }
}
