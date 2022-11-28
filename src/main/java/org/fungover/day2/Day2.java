package org.fungover.day2;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day2 {
    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day2/day2.txt"));
        System.out.println(calculatePosition(s));
        System.out.println(calculatePositionWithAim(s));
    }


    public static SubPos calculatePosition(String input) {
        var values = input.lines().toList();
        SubPos pos = new SubPos(0, 0);

        for (var command : values) {
            if (command.startsWith("f"))
                pos = pos.forward(Integer.parseInt(command.split(" ")[1]));
            else if (command.startsWith("u"))
                pos = pos.up(Integer.parseInt(command.split(" ")[1]));
            else if (command.startsWith("d"))
                pos = pos.down(Integer.parseInt(command.split(" ")[1]));
        }
        return pos;
    }

    public static SubPosAim calculatePositionWithAim(String input) {
        var values = input.lines().toList();
        SubPosAim pos = new SubPosAim(0, 0, 0);

        for (var command : values) {
            if (command.startsWith("f"))
                pos = pos.forward(Integer.parseInt(command.split(" ")[1]));
            else if (command.startsWith("u"))
                pos = pos.up(Integer.parseInt(command.split(" ")[1]));
            else if (command.startsWith("d"))
                pos = pos.down(Integer.parseInt(command.split(" ")[1]));
        }
        return pos;
    }
}
