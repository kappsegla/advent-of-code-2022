package org.fungover.day13;

import java.util.*;

import static java.util.function.Predicate.not;
import static org.fungover.util.Strings.stringToListOfListOfStrings;

public class Day13 {
    public static void main(String[] args) {
//        String s = stringFromFile(resourceStringToPath("/day13/day13.txt"));
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
                """; //13 //140

        //Step1
        var elements = stringToListOfListOfStrings(s).stream()
                .map(i-> List.of( Element.of(i.get(0)),Element.of(i.get(1))))
                .toList();

        List<Integer> index = new ArrayList<>();
        for (int i = 0; i < elements.size() ; i++) {
            var element = elements.get(i);
            if (element.get(0).compareTo(element.get(1)) < 0)
                index.add(i+1);
        }

        System.out.println(index.stream().mapToInt(Integer::intValue).sum());
        //Step2
        s += "\n[[2]]\n[[6]]";
        elements = stringToListOfListOfStrings(s).stream()
                .map(i-> List.of( Element.of(i.get(0)),Element.of(i.get(1))))
                .toList();
        var divider1 = Element.of("[[2]]");
        var divider2 = Element.of("[[6]]");

        var sorted = elements.stream()
                .flatMap(Collection::stream)
                .sorted(Element::compareTo)
                .toList();
        System.out.println( (sorted.indexOf(divider1)+1) * (sorted.indexOf(divider2)+1));
    }
}


abstract sealed class Element implements Comparable<Element> permits IntElement, ListElement {

    public static Element of(String input) {
        return of(Arrays.stream(input.split("((?<=[\\[\\],])|(?=[\\[\\],]))"))
                .filter(not(String::isBlank))
                .filter(i -> !i.equals(","))
                .iterator());
    }

    private static Element of(Iterator<String> input) {
        var packets = new ArrayList<Element>();

        while (input.hasNext()) {
            var value = input.next();
            switch (value) {
                case "]" -> {
                    return new ListElement(packets);
                }
                case "[" -> packets.add(of(input));
                default -> packets.add(new IntElement(Integer.parseInt(value)));
            }
        }
        return new ListElement(packets);
    }
}

final class IntElement extends Element {

    public Integer value;

    public IntElement(Integer value) {
        this.value = value;
    }

    @Override
    public int compareTo(Element o) {
        return switch (o) {
            case IntElement other -> value.compareTo(other.value);
            case ListElement list -> asList().compareTo(list);
        };
    }

    public ListElement asList() {
        return new ListElement(List.of(this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntElement that = (IntElement) o;

        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}

final class ListElement extends Element {

    List<Element> subElements;

    public ListElement(List<Element> subElements) {
        this.subElements = subElements;
    }

    @Override
    public int compareTo(Element o) {
        return switch (o) {
            case IntElement other -> compareTo(other.asList());
            case ListElement other -> compareLists(other);
        };
    }

    private int compareLists(ListElement other) {
        var first = subElements.iterator();
        var second = other.subElements.iterator();
        while (first.hasNext() && second.hasNext()) {
            int compare = ((Element) first.next()).compareTo(second.next());
            if (compare != 0)
                return compare;
        }
        return Integer.compare(subElements.size(), other.subElements.size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListElement that = (ListElement) o;

        return Objects.equals(subElements, that.subElements);
    }

    @Override
    public int hashCode() {
        return subElements != null ? subElements.hashCode() : 0;
    }
}
