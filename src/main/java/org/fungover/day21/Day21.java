package org.fungover.day21;

import java.util.HashMap;
import java.util.Map;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day21 {

    public static void main(String[] args) {

        //   String s = stringFromFile(resourceStringToPath("/day21/day21.txt"));

        String s = """
                root: pppw + sjmn
                dbpl: 5
                cczh: sllz + lgvd
                zczc: 2
                ptdq: humn - dvpt
                dvpt: 3
                lfqf: 4
                humn: 5
                ljgn: 2
                sjmn: drzm * dbpl
                sllz: 4
                pppw: cczh / lfqf
                lgvd: ljgn * ptdq
                drzm: hmdt - zczc
                hmdt: 32
                """;


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

                    orders.put(name, new Num(f.calc(n1, n2)));
                }
            }
            if (orders.get("root") instanceof Num num) {
                System.out.println(num.number);
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
        //Step1
        while (true) {
            for (var name : orders.keySet()) {
                if( name.equals("root")){
                    System.out.println("human");
                }
                else if( name.equals("humn")){
                    System.out.println("human");
                }
                else if (orders.get(name) instanceof Formula f && orders.get(f.name1) instanceof Num n1 && orders.get(f.name2) instanceof Num n2) {
                        orders.put(name, new Num(f.calc(n1, n2)));
                }
            }
            if (orders.get("root") instanceof Num num) {
                System.out.println(num.number);
                break;
            }
        }

    }
}

sealed class Yelling permits Num, Formula {

}

final class Num extends Yelling {
    long number;

    public Num(long number) {
        this.number = number;
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

    public long calc(Num n1, Num n2) {
        return switch (calc) {
            case MUL -> n1.number * n2.number;
            case DIV -> n1.number / n2.number;
            case ADD -> n1.number + n2.number;
            case SUB -> n1.number - n2.number;
        };
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
