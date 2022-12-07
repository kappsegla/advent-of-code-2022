package org.fungover.day7;

import java.util.*;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day7 {
    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day7/day7.txt"));
        //Step1 answer = 1243729
        //Step2 answer = 4443914

//        String s = """
//                $ cd /
//                $ ls
//                dir a
//                14848514 b.txt
//                8504156 c.dat
//                dir d
//                $ cd a
//                $ ls
//                dir e
//                29116 f
//                2557 g
//                62596 h.lst
//                $ cd e
//                $ ls
//                584 i
//                $ cd ..
//                $ cd ..
//                $ cd d
//                $ ls
//                4060174 j
//                8033020 d.log
//                5626152 d.ext
//                7214296 k
//                """;
        //Step1 answer = 95437
        //Step2 answer = 24933642 (dir d)

        Node root = new Node("/", 0);
        Node $ = root;

        for (var line : s.lines().toList()) {
            var terminalOutput = line;
            if (line.startsWith("$"))
                terminalOutput = line.substring(2);

            if (terminalOutput.matches("cd /")) {
                $ = root;
            } else if (terminalOutput.matches("cd ..")) {
                //Move out one level
                $ = $.getParent();
            } else if (terminalOutput.matches("cd\\s[^.]+")) {
                //Move in one level
                var nodeName = terminalOutput.split("\\s")[1];
                var nextNode = $.nodes().stream().filter(n -> n.name().equals(nodeName)).findFirst();
                if (nextNode.isPresent())
                    $ = nextNode.get();
                else {
                    $ = $.addFile(nodeName, 0L);
                }
            } else if (terminalOutput.matches("dir .*")) {
                //Directory
                var folderName = terminalOutput.split("\\s")[1];
                $.addDir(folderName);
            }
            else if (terminalOutput.matches("\\d+\\s[\\D]+")) {
                //File
                var parts = terminalOutput.split("\\s");
                $.addFile(parts[1], Long.parseLong(parts[0]));
            }
        }
        //printTree(root, 1);
        List<Node> allFolders = step1(root);
        step2(root, allFolders);
    }

    private static List<Node> step1(Node root) {
        List<Node> allFolders = new ArrayList<>();

        getAllFolders(root, allFolders);

        System.out.println("Total size for dirs <= 100000: " + allFolders.stream().mapToLong(Node::calculateSize)
                .filter(n -> n <= 100000L)
                .sum());
        return allFolders;
    }

    private static void step2(Node root, List<Node> allFolders) {
        final long DISK_SIZE = 70000000;
        final long FREE_DISK_NEEDED = 30000000;

        long freeSpace = DISK_SIZE - root.calculateSize();
        long neededSpace = FREE_DISK_NEEDED - freeSpace;

        allFolders.stream()
                .sorted(Comparator.comparingLong(Node::calculateSize))
                .filter(n -> n.calculateSize() >= neededSpace)
                .findFirst()
                .ifPresent(n -> System.out.println("Remove dir " + n + ", size: " + n.calculateSize()));
    }

    private static void printTree(Node node, int i) {
        System.out.printf("%" + i + "s", "  ");
        System.out.println(node.toString());
        for (Node n : node.nodes()) {
            printTree(n, i + 1);
        }
    }

    private static void getAllFolders(Node node, List<Node> allFolders) {
        allFolders.add(node);
        for (Node n : node.nodes()) {
            if (n.size() == 0) {
                getAllFolders(n, allFolders);
            }
        }
    }
}

final class Node {
    private final String name;
    private final long size; //Zero for directories
    private final Set<Node> nodes;
    private Node parent;

    Node(String name, long size) {
        this.name = name;
        this.size = size;
        this.nodes = new HashSet<>();
    }

    public String name() {
        return name;
    }

    public long size() {
        return size;
    }

    public Set<Node> nodes() {
        return nodes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Node) obj;
        return Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        //Directory
        if (size == 0) return name;
        //File
        return name + " " + size;
    }

    public Node addDir(String name) {
        var n = new Node(name, 0L);
        n.parent = this;
        this.nodes.add(n);
        return n;
    }

    public Node addFile(String name, long size) {
        var n = new Node(name, size);
        n.parent = this;
        this.nodes.add(n);
        return n;
    }

    public long calculateSize() {
        if (size == 0)  //Directory
            return nodes.stream().mapToLong(Node::calculateSize).sum();
        return size;
    }

    public Node getParent() {
        if (parent == null)
            return this;
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
