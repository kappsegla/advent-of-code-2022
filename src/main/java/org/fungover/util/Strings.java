package org.fungover.util;

import java.util.List;

public class Strings {

    public static String removeAllSpaces(String input) {
        return input.replaceAll(" ", "");
    }

    public static String doubleSpaceToSpace(String input) {
        return input.replaceAll("\\s{2}", " ");
    }

}
