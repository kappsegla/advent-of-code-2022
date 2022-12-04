package org.fungover.day4;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day4 {
    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day4/day4.txt"));
//        String s = """
//                2-4,6-8
//                2-3,4-5
//                5-7,7-9
//                2-8,3-7
//                6-6,4-6
//                2-6,4-8
//                """;
        var sectors = s.lines()
                .map(l -> Arrays.stream(l.split("[-,]"))
                        .mapToInt(Integer::parseInt).toArray())
                .map(a -> List.of(new Range(a[0], a[1]), new Range(a[2], a[3])))
                .toList();

        //Step1  511
        int count = 0;
        for (var v : sectors) {
            var r1 = v.get(0);
            var r2 = v.get(1);
            if (r1.overlapping(r2)) {
                var overlapp = r1.overlappedRange(r2);
                if (overlapp.length() == r1.length() || overlapp.length() == r2.length())
                    count++;
            }
        }
        System.out.println(count);
        //Step2 821
        //Just do the overlapp check
        var result = sectors.stream().map(v-> v.get(0).overlapping(v.get(1))).filter(i->i).count();
        System.out.println(result);

    }
}

/**
 * Representing a range
 * @param start, start of range, inclusive
 * @param end, end of range, inclusive
 */
record Range(int start, int end) {
    Range(int start, int end) {
        if( start > end)
            throw new IllegalArgumentException(String.format("End %d must be >= than start %d",end,start));
        this.start = start;
        this.end = end;
    }

    /**
     * Calculates the length of the range end - start. For a range with start 1 and end 3 the length will be 3
     * since the Range is inclusive
     * @return The length of the range
     */
    public int length() {
        return end - start + 1;
    }

    /**
     * Checks if this range overlapps with the other range
     * @param other
     * @return True if overlapping
     */
    public boolean overlapping(Range other) {
        return this.start <= other.end && this.end >= other.start;
    }

    /**
     * If the ranges overlap this method will return a new range
     * consisting of the overlapping range
     * @param other
     * @return A new Range representing the overlapped range
     */
    public Range overlappedRange(Range other) {
        int oStart = Math.max(this.start, other.start);
        int oEnd = Math.min(this.end, other.end);
        if (oStart <= oEnd) //Overlapping
            return new Range(oStart, oEnd);
        throw new RuntimeException("No overlap");
    }
}
