package org.fungover.day11;

import org.fungover.util.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fungover.day11.Operation.*;
import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day11 {

    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day11/day11.txt"));
        //String s = stringFromFile(resourceStringToPath("/day11/test.txt"));

        var monkeys = Strings.stringToListOfListOfStrings(s);
        var monkeyList = monkeys.stream().map(Monkey::parse).toList();

//        for (int i = 0; i < 20; i++) {
//            monkeyList.stream().forEach(m -> m.turn1(monkeyList));
//        }

        var mod = 1;
        for( var monkey : monkeyList) {
            mod *= monkey.divisibleBy;
        }

        for (int j = 0; j < 10000; j++) {
            int k = mod;
            monkeyList.stream().forEach(m -> m.turn2(monkeyList,k));
        }
        monkeyList.stream().map(m -> m.inspections).forEach(System.out::println);


    }
}

class Monkey {
    int id;

    List<Item> worryItems = new ArrayList<>();

    Operation operation;

    int value;

    int divisibleBy;

    int whenTrue;
    int whenFalse;

    int inspections;


    public void turn1(List<Monkey> monkeys) {
        //Inspect
        for (Item item : worryItems) {
            if (operation == SELF)
                item.worryLevel = item.worryLevel * item.worryLevel;
            if (operation == PLUS)
                item.worryLevel = item.worryLevel + value;
            if (operation == MULTIPLICATION)
                item.worryLevel = item.worryLevel * value;
            inspections++;
        }

        //Divide by 3
        for (Item item : worryItems) {
            item.worryLevel /= 3;
        }

        //Test
        for (Item item : worryItems) {
            if (item.worryLevel % divisibleBy == 0) {
                monkeys.get(whenTrue).worryItems.add(item);
            } else {
                monkeys.get(whenFalse).worryItems.add(item);
            }
        }
        worryItems.clear();
    }

    public void turn2(List<Monkey> monkeys, int mod) {
        //Inspect
        for (Item item : worryItems) {
            if (operation == SELF)
                item.worryLevel = item.worryLevel * item.worryLevel;
            if (operation == PLUS)
                item.worryLevel = item.worryLevel + value;
            if (operation == MULTIPLICATION)
                item.worryLevel = item.worryLevel * value;
            inspections++;
        }
        int totalValue;
        for (Item item : worryItems) {
            item.worryLevel =  item.worryLevel % mod;
        }

        //Test
        for (Item item : worryItems) {
            if (item.worryLevel % divisibleBy == 0) {
                monkeys.get(whenTrue).worryItems.add(item);
            } else {
                monkeys.get(whenFalse).worryItems.add(item);
            }
        }
        worryItems.clear();
    }

    public static Monkey parse(List<String> strings) {
        Monkey monkey = new Monkey();
        monkey.id = Integer.parseInt(strings.get(0).split("[\\s:]")[1]);
        Arrays.stream(strings.get(1).replace(" ", "").split(":")[1].split(",")).map(Integer::parseInt).map(Item::new).forEach(monkey.worryItems::add);

        if (strings.get(2).trim().split(" ")[4].equals("*"))
            monkey.operation = MULTIPLICATION;
        if (strings.get(2).trim().split(" ")[4].equals("+"))
            monkey.operation = PLUS;

        var factor = strings.get(2).trim().split(" ")[5];
        if (factor.equals("old"))
            monkey.operation = SELF;
        else
            monkey.value = Integer.parseInt(factor);

        monkey.divisibleBy = Integer.parseInt(strings.get(3).trim().split(" ")[3]);
        monkey.whenTrue = Integer.parseInt(strings.get(4).trim().split(" ")[5]);
        monkey.whenFalse = Integer.parseInt(strings.get(5).trim().split(" ")[5]);

        return monkey;
    }
}

class Item {
    long worryLevel;

    public Item(int worryLevel) {
        this.worryLevel = worryLevel;
    }
}

enum Operation {
    PLUS, MULTIPLICATION, SELF;
}
