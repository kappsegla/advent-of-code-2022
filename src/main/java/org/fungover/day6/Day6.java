package org.fungover.day6;

import java.util.HashSet;
import java.util.Set;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day6 {

    public static final int MESSAGE_MARKER_SIZE = 14;
    public static final int PACKET_MARKER_SIZE = 4;

    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day6/day6.txt"));

        System.out.println(packetMarker(s));
        System.out.println(messageMarker(s));
    }

    public static int packetMarker(String input) {
        for (int i = 0; i <= input.length() - PACKET_MARKER_SIZE; i++) {
            if( isUnique(input.substring(i, i + PACKET_MARKER_SIZE)))
                return i + PACKET_MARKER_SIZE;
        }
        throw new RuntimeException("Invalid input");
    }

    public static int messageMarker(String input) {
        for (int i = 0; i <= input.length() - MESSAGE_MARKER_SIZE; i++) {
            var substring = input.substring(i, i+ MESSAGE_MARKER_SIZE);
            if( isUnique(substring))
                return i+ MESSAGE_MARKER_SIZE;
        }
        return 0;
    }

    public static boolean isUnique(String substring) {
        Set<Character> characterSet = new HashSet<>();

        for (int i = 0; i < substring.length(); i++) {
            characterSet.add(substring.charAt(i));
        }
        return characterSet.size() == substring.length();
    }
}
