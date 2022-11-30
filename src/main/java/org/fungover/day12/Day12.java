package org.fungover.day12;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day12 {
    public static void main(String[] args) {
            String s = stringFromFile(resourceStringToPath("/day12/day12.txt"));
//        String s = """
//                start-A
//                start-b
//                A-c
//                A-b
//                b-d
//                A-end
//                b-end
//                """; //10 paths

//        String s = """
//                dc-end
//                HN-start
//                start-kj
//                dc-start
//                dc-HN
//                LN-dc
//                HN-end
//                kj-sa
//                kj-HN
//                kj-dc"""; //19 paths

//        String s = """
//                fs-end
//                he-DX
//                fs-he
//                start-DX
//                pj-DX
//                end-zg
//                zg-sl
//                zg-pj
//                pj-he
//                RW-he
//                fs-DX
//                pj-RW
//                zg-RW
//                start-pj
//                he-WI
//                zg-he
//                pj-fs
//                start-RW""";//226 paths

        //Build graph
        Map<String, Node> allNodes = new HashMap<>();
        Node start = new Node("start");
        Node end = null;
        allNodes.put("start", start);

        for (String path : s.lines().toList()) {
            var nodeNames = path.split("-");
            var node1 = allNodes.computeIfAbsent(nodeNames[0], n -> new Node(nodeNames[0]));
            var node2 = allNodes.computeIfAbsent(nodeNames[1], n -> new Node(nodeNames[1]));
            node1.addNode(node2);
            node2.addNode(node1);

            if (node2.name.equals("end"))
                end = node2;
        }
        int nodeCount = allNodes.size();

        //Calculate paths in graph
        List<Node> pathList = new ArrayList<>();
        int[] count = {0};
        //printAllPathsUtil(start, end, pathList, count);
        printAllPathsUtilOneSmallCaveCanBeVisitedTwice(start,end,pathList,count);
        System.out.println("Total paths: " + count[0]);

    }

    private static void printAllPathsUtil(Node u, Node d, List<Node> pathList, int[] count) {
        //Have we reached our end node?
        if (u.equals(d)) {
            pathList.add(u);
            System.out.println(pathList);
            count[0]++;
            return;
        }

        //Check if Node has lowercase name and already in pathList
        if (u.name.equals(u.name.toLowerCase()) && pathList.contains(u))
            return;

        pathList.add(u);

        for (Node i : u.nodes) {
            List<Node> path = new ArrayList<>(pathList);
            printAllPathsUtil(i, d, path, count);
        }
    }

    private static void printAllPathsUtilOneSmallCaveCanBeVisitedTwice(Node u, Node d, List<Node> pathList, int[] count) {
        //Have we reached our end node?
        if (u.equals(d)) {
            pathList.add(u);
            System.out.println(pathList);
            count[0]++;
            return;
        }

        var isLowerCase = u.name.equals(u.name.toLowerCase());

        //Check if Node has lowercase name and already in pathList
        var haveDouble = pathList.stream()
                .map(n->n.name)
                .filter(v-> v.equals(v.toLowerCase()))
                .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()))
                .entrySet()
                .stream()
                .filter(o-> o.getValue() > 1).count() == 1;

        //Check if Node has lowercase name and already in pathList
        if( isLowerCase && haveDouble && pathList.contains(u))
            return;
        if( u.name.equals("start") && pathList.contains(u))
            return;

        pathList.add(u);

        for (Node i : u.nodes) {
            List<Node> path = new ArrayList<>(pathList);
            printAllPathsUtilOneSmallCaveCanBeVisitedTwice(i, d, path, count);
        }
    }
}


class Node {
    String name;
    Set<Node> nodes = new HashSet<>();

    public Node(String name) {
        this.name = name;
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(name);
//        output.append("(");
//        for (Node n : nodes) {
//            output.append(n.name);
//        }
//        output.append(")");
        return output.toString();
    }
}
