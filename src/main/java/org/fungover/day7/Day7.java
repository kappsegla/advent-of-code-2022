package org.fungover.day7;

import java.util.*;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day7 {
    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day7/day7.txt"));

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
//        //Map<String, Node> nodes = new HashMap<>();
        System.out.println(s.lines().filter(y -> y.startsWith("dir")).distinct().count());


        Node root = new Node("/", 0);
        Node $ = root;
        Node dir = null;

        for (var t : s.lines().toList()) {
            var l = t;
            if (t.startsWith("$"))
                l = t.substring(2);
            if (l.startsWith("cd /")) {
                $ = root;
                continue;
            }
            if (l.startsWith("cd ..")) {
                if ($.getParent() != null)
                    $ = $.getParent();
                continue;
            }
            if (l.matches("cd\\s[^.]+")) {
                //Move to folder deeper in our tree
                var nodeName = l.split("\\s")[1];
                var nextNode = $.nodes().stream().filter(n -> n.name().equals(nodeName)).findFirst();
                if (nextNode.isPresent())
                    $ = nextNode.get();
                else {
                    $ = $.addNode(nodeName, 0L);
                }
                continue;
            }
            if (l.startsWith("dir")) {
                //Found a folder name
                var folderName = l.split("\\s")[1];
                $.addNode(folderName, 0L);
                continue;
            }
            if (l.startsWith("$ ls")) {
                //Ignore
                continue;
            }
            if (l.matches("\\d+\\s[\\D]+")) {
                //Found a file, add to current folder
                var parts = l.split("\\s");
                $.addNode(parts[1], Long.parseLong(parts[0]));
            }
        }



        printTree(root, 1);

        //Total size
        System.out.println("Total size: " + root.calculateSize()); //48381165 for testdata

        List<Node> allFolders = new ArrayList<>();

        getAllFolders(root, allFolders);

        System.out.println("Max 100000 dirs: " + allFolders.stream().mapToLong(Node::calculateSize)
                .filter(n -> n <= 100000L)
                .sum()); //95437 for testdata

        long diskSize = 70000000;

        long freeSpace = 70000000 - root.calculateSize();
        System.out.println(freeSpace);
        long neededSpace = 30000000 - freeSpace;
        System.out.println(neededSpace);

        allFolders.stream()
                .sorted(Comparator.comparingLong(Node::calculateSize))
                .filter(n -> n.calculateSize() >= neededSpace)
                .forEach(o-> System.out.println(o +  " : " + o.calculateSize()));

    }

    private static void printTree(Node node, int i) {
        //Print intendention
        System.out.printf("%"+ i +"s", "  ");
        //Print ourself
        System.out.println(node.toString());
        //Print our siblings
        for( Node n : node.nodes()) {
            printTree(n,i+1);
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
    private final long size;
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

    public Node addNode(String name, long size) {
        var n = new Node(name, size);
        n.parent = this;
        this.nodes.add(n);
        return n;
    }

    public long calculateSize() {
        //Call all our nodes and ask them to update and return there size if we are directory
        if (size == 0)
            return nodes.stream().mapToLong(Node::calculateSize).sum();
        return size;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
