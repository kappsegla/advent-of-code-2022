package org.fungover.day13;

import com.google.gson.*;

import java.lang.reflect.Array;
import java.util.*;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;
import static org.fungover.util.Strings.stringToListOfListOfStrings;

public class Day13 {
    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day13/day13.txt"));
//        String s = """
//                [1,1,3,1,1]
//                [1,1,5,1,1]
//
//                [[1],[2,3,4]]
//                [[1],4]
//
//                [9]
//                [[8,7,6]]
//
//                [[4,4],4,4]
//                [[4,4],4,4,4]
//
//                [7,7,7,7]
//                [7,7,7]
//
//                []
//                [3]
//
//                [[[]]]
//                [[]]
//
//                [1,[2,[3,[4,[5,6,7]]]],8,9]
//                [1,[2,[3,[4,[5,6,0]]]],8,9]
//                """; //13 //140
        //Step1
        var lines = stringToListOfListOfStrings(s);
        Set<Integer> rightOrderPairs = new HashSet<>();
        for (int i = 0; i < lines.size(); i++) {
            List<String> pair = lines.get(i);
            if (compare(JsonParser.parseString(pair.get(0)), JsonParser.parseString(pair.get(1))) < 0)
                rightOrderPairs.add(i + 1);
        }
        System.out.println(rightOrderPairs.stream().mapToInt(i -> i).sum());
        //Step2
        s += "\n[[2]]\n[[6]]";
        lines = stringToListOfListOfStrings(s);
        var sorted = lines.stream().flatMap(Collection::stream).map(JsonParser::parseString).sorted(Day13::compare).toList();
        JsonElement divider1 = JsonParser.parseString("[[2]]");
        JsonElement divider2 = JsonParser.parseString("[[6]]");
        System.out.println( (sorted.indexOf(divider1)+1) * (sorted.indexOf(divider2)+1));

    }

    public static int compare(JsonElement first, JsonElement second) {
        if (first instanceof JsonPrimitive firstInteger && second instanceof JsonArray) {
            JsonArray array = new JsonArray();
            array.add(firstInteger);
            return compare(array, second);
        } else if (first instanceof JsonArray && second instanceof JsonPrimitive secondInteger) {
            JsonArray array = new JsonArray();
            array.add(secondInteger);
            return compare(first, array);
        } else if (first instanceof JsonPrimitive firstInteger && second instanceof JsonPrimitive secondInteger) {
            return Integer.compare(firstInteger.getAsInt(), secondInteger.getAsInt());
        } else if (first instanceof JsonArray firstArray && second instanceof JsonArray secondArray) {
            for (int i = 0; i < Math.min(firstArray.size(), secondArray.size()); i++) {
                int result = compare(firstArray.get(i), secondArray.get(i));
                if (result != 0) {
                    return result;
                }
            }
            return compare(new JsonPrimitive(firstArray.size()), new JsonPrimitive(secondArray.size()));
        }
        return 0;
    }
}
