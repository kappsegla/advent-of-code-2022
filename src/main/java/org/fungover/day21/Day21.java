package org.fungover.day21;

import java.util.HashMap;
import java.util.Map;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day21 {

    public static void main(String[] args) {

        String s = stringFromFile(resourceStringToPath("/day21/day21.txt"));

//        String s = """
//                root: pppw + sjmn
//                dbpl: 5
//                cczh: sllz + lgvd
//                zczc: 2
//                ptdq: humn - dvpt
//                dvpt: 3
//                lfqf: 4
//                humn: 5
//                ljgn: 2
//                sjmn: drzm * dbpl
//                sllz: 4
//                pppw: cczh / lfqf
//                lgvd: ljgn * ptdq
//                drzm: hmdt - zczc
//                hmdt: 32
//                """;

        var lines = s.lines().map(l -> l.split("[:\\s]")).toList();
        Map<String, Yelling> orders = new HashMap<>();
        for (var m : lines) {
            var name = m[0];
            Yelling yelling;
            if (m.length == 3)
                yelling = new Num(Long.parseLong(m[2]));
            else
                yelling = new Formula(m[2], m[4], Calc.of(m[3]));
            orders.put(name, yelling);
        }
        //Step1
        while (true) {
            for (var name : orders.keySet()) {
                if (orders.get(name) instanceof Formula f && orders.get(f.name1) instanceof Num n1 && orders.get(f.name2) instanceof Num n2) {

                    orders.put(name, new Num(f.calc(n1.number, n2.number)));
                }
            }
            if (orders.get("root") instanceof Num num) {
                System.out.println("Step1 answer: " + num.number);
                break;
            }
        }
        //Step2
        lines = s.lines().map(l -> l.split("[:\\s]")).toList();
        orders = new HashMap<>();
        for (var m : lines) {
            var name = m[0];
            Yelling yelling;
            if (m.length == 3)
                yelling = new Num(Long.parseLong(m[2]));
            else
                yelling = new Formula(m[2], m[4], Calc.of(m[3]));
            orders.put(name, yelling);
        }

        Monkey root = Monkey.of("root", orders);
        System.out.println("Step1 answer: " + root.calcNumber());

        //Find which leg has humn, left or right
        Monkey nonHumanSide = checkForHuman(root.left) ? root.right : root.left;
        Monkey humanSide = checkForHuman(root.left) ? root.left : root.right;

        //Calculate value for the leg that is false
        var value = nonHumanSide.calcNumber();
        System.out.println("Human side should be: " + value);

        long answer = findValue(value, humanSide);
        System.out.println("Answer:" + answer);
    }

    private static long findValue(long value, Monkey monkey) {
        if (monkey.name.equals("humn")) {
            return value;
        }
        //Find human side.
        boolean left = checkForHuman(monkey.left);
        var otherSide = left ? monkey.right : monkey.left;
        var humanSide = left ? monkey.left : monkey.right;

        //Calculate other sides value
        long otherValue = otherSide.calcNumber();

        long lookingForValue = 0;
        if (left)
            lookingForValue = ((Formula) monkey.yelling).calcInvertedLeft(otherValue, value);
        else
            lookingForValue = ((Formula) monkey.yelling).calcInvertedRight(otherValue, value);
        return findValue(lookingForValue, humanSide);
    }

    private static boolean checkForHuman(Monkey monkey) {
        //Are we human?
        if (monkey.name.equals("humn"))
            return true;

        //We have reach the bottom
        if (monkey.left == null || monkey.right == null)
            return false;

        return checkForHuman(monkey.left) || checkForHuman(monkey.right);
    }
}

class Monkey {
    String name;
    Monkey left;
    Monkey right;

    Yelling yelling;

    public Monkey(String name, Monkey left, Monkey right, Yelling yelling) {
        this.name = name;
        this.left = left;
        this.right = right;
        this.yelling = yelling;
    }

    public long calcNumber() {
        if (yelling instanceof Formula f)
            return f.calc(left.calcNumber(), right.calcNumber());
        else
            return yelling.getValue();
    }

    public long calcNumber(long value) {
        if (yelling instanceof Formula f) {
            return f.calc(left.calcNumber(value), right.calcNumber(value));
        } else if (name.equals("humn")) {
//            System.out.println("In human returning: " + value);
            return value;
        } else
            return yelling.getValue();
    }


    public static Monkey of(String name, Map<String, Yelling> orders) {
        var yell = orders.get(name);

        if (yell instanceof Num n)
            return new Monkey(name, null, null, yell);
        if (yell instanceof Formula f) {
            return new Monkey(name, Monkey.of(f.name1, orders), Monkey.of(f.name2, orders), yell);
        }
        throw new IllegalStateException();
    }
}


sealed abstract class Yelling permits Num, Formula {

    abstract long getValue();

}

final class Num extends Yelling {
    long number;

    public Num(long number) {
        this.number = number;
    }

    @Override
    long getValue() {
        return number;
    }
}

final class Formula extends Yelling {
    String name1;
    String name2;
    Calc calc;

    public Formula(String name1, String name2, Calc calc) {
        this.name1 = name1;
        this.name2 = name2;
        this.calc = calc;
    }

    public long calc(long left, long right) {
        return switch (calc) {
            case MUL -> left * right;
            case DIV -> left / right;
            case ADD -> left + right;
            case SUB -> left - right;
        };
    }

    public long calcInvertedLeft(long right, long result) {
        return switch (calc) {
            case MUL -> result / right;
            case DIV -> result * right;
            case ADD -> result - right;
            case SUB -> result + right;
        };
    }

    public long calcInvertedRight(long left, long result) {
        return switch (calc) {
            case MUL -> result / left;
            case DIV -> left / result;
            case ADD -> result - left;
            case SUB -> left - result;
        };
    }

    @Override
    long getValue() {
        return 0;
    }
}

enum Calc {
    MUL, DIV, ADD, SUB;

    public static Calc of(String s) {
        return switch (s) {
            case "*" -> MUL;
            case "/" -> DIV;
            case "+" -> ADD;
            case "-" -> SUB;
            default -> throw new IllegalStateException("Unexpected value: " + s);
        };
    }
}
