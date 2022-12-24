package org.fungover.day19;

import java.util.*;
import java.util.stream.IntStream;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day19_Slow {
    static Map<State, Integer> cache = new HashMap<>();

    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day19/day19.txt"));
//        String s = """
//                Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
//                Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
//                """;

        var bluePrints = s.lines().map(l -> l.split("[\\s:]")).map(BluePrint::of).toList();



        System.out.println(bluePrints.stream().mapToInt(m -> m.id * calculateGeodesFound(m, 24)).sum());

//        System.out.println(calculateGeodesFound(bluePrints.get(0), 32));
//        System.out.println(calculateGeodesFound(bluePrints.get(1), 32));
        System.out.println(calculateGeodesFound(bluePrints.get(2), 32));

//        int sum = bluePrints.stream().limit(3).mapToInt(m->calculateGeodesFound(m, 32)).reduce(1, (a,b)-> a*b);
//        System.out.println(sum);
    }

    private static int calculateGeodesFound(BluePrint bluePrint, int maxTime) {
        var maxGeodes = 0;
        var queue = new PriorityQueue<State>();
        queue.add(new State(bluePrint));

        while (!queue.isEmpty()) {
            var state = queue.poll();
            if (state.canOutproduceBest(maxGeodes, maxTime)) {
                queue.addAll(state.nextStates(maxTime));
            }
            maxGeodes = Math.max(maxGeodes, state.geodes);
        }
        return maxGeodes;
    }
}

class State implements Comparable<State> {
    int oreRobots = 1;
    int clayRobots = 0;
    int obsidianRobots = 0;
    int geodeRobots = 0;

    int time = 1;
    int ore = 1;
    int clay = 0;
    int obsidian = 0;
    int geodes = 0;

    BluePrint bluePrint;

    public State(BluePrint bluePrint) {
        this.bluePrint = bluePrint;
    }

    public List<? extends State> nextStates(int maxTime) {
        List<State> nextStates = new ArrayList<>();
        if (time < maxTime) {
            if (bluePrint.maxOre > oreRobots && ore > 0) {
                nextStates.add(bluePrint.oreRobot.scheduleBuild(this));
            }
            if (bluePrint.maxClay > clayRobots && ore > 0) {
                nextStates.add(bluePrint.clayRobot.scheduleBuild(this));
            }
            if (bluePrint.maxObsidian > obsidianRobots && ore > 0 && clay > 0) {
                nextStates.add(bluePrint.obsidianRobot.scheduleBuild(this));
            }
            if (ore > 0 && obsidian > 0) {
                nextStates.add(bluePrint.geodeRobot.scheduleBuild(this));
            }
        }
        return nextStates.stream().filter(s -> s.time <= maxTime).toList();
    }

    public boolean canOutproduceBest(int bestSoFar, int timeBudget){
        var timeLeft = timeBudget - time;
        var potentialProduction = IntStream.range(0,timeLeft).map(i-> i  + geodeRobots).sum();
        return geodes + potentialProduction > bestSoFar;
    }

    @Override
    public int compareTo(State other) {
        return Integer.compare(geodes, other.geodes);
    }
}


class RobotBluePrint {
    public RobotBluePrint(int oreCost, int clayCost, int obsidianCost, int oreRobotsBuilt, int clayRobotsBuilt, int obsidianRobotsBuilt, int geodeRobotsBuilt) {
        this.oreCost = oreCost;
        this.clayCost = clayCost;
        this.obsidianCost = obsidianCost;
        this.oreRobotsBuilt = oreRobotsBuilt;
        this.clayRobotsBuilt = clayRobotsBuilt;
        this.obsidianRobotsBuilt = obsidianRobotsBuilt;
        this.geodeRobotsBuilt = geodeRobotsBuilt;
    }

    int oreCost;
    int clayCost;
    int obsidianCost;

    int oreRobotsBuilt;
    int clayRobotsBuilt;
    int obsidianRobotsBuilt;
    int geodeRobotsBuilt;

    public State scheduleBuild(State state) {
        var timeRequired = timeUntilBuild(state);

        var nextState = new State(state.bluePrint);

        nextState.time = state.time + timeRequired;
        nextState.ore = (state.ore - oreCost) + (timeRequired * state.oreRobots);
        nextState.oreRobots = state.oreRobots + oreRobotsBuilt;
        nextState.clay = (state.clay - clayCost) + (timeRequired * state.clayRobots);
        nextState.clayRobots = state.clayRobots + clayRobotsBuilt;
        nextState.obsidian = (state.obsidian - obsidianCost) + (timeRequired * state.obsidianRobots);
        nextState.obsidianRobots = state.obsidianRobots + obsidianRobotsBuilt;
        nextState.geodes = state.geodes + (timeRequired * state.geodeRobots);
        nextState.geodeRobots = state.geodeRobots + geodeRobotsBuilt;

        return nextState;
    }

    private int timeUntilBuild(State state) {
        return BluePrint.maxOf((oreCost <= state.ore) ? 0 : (int) Math.ceil((oreCost - state.ore) / (state.oreRobots * 1.0)),
                (clayCost <= state.clay) ? 0 : (int) Math.ceil((clayCost - state.clay) / (state.clayRobots * 1.0)),
                (obsidianCost <= state.obsidian) ? 0 : (int) Math.ceil((obsidianCost - state.obsidian) / (state.obsidianRobots * 1.0))) + 1;
    }
}

class BluePrint {
    int id;
    RobotBluePrint oreRobot;
    RobotBluePrint clayRobot;
    RobotBluePrint obsidianRobot;
    RobotBluePrint geodeRobot;

    int maxOre;
    int maxClay;
    int maxObsidian;

    public static BluePrint of(String[] input) {
        var bp = new BluePrint();
        bp.id = Integer.parseInt(input[1]);
        bp.oreRobot = new RobotBluePrint(Integer.parseInt(input[7]), 0, 0,1,0,0,0);
        bp.clayRobot = new RobotBluePrint(Integer.parseInt(input[13]), 0, 0,0,1,0,0);
        bp.obsidianRobot = new RobotBluePrint(Integer.parseInt(input[19]), Integer.parseInt(input[22]), 0,0,0,1,0);
        bp.geodeRobot = new RobotBluePrint(Integer.parseInt(input[28]), 0, Integer.parseInt(input[31]),0,0,0,1);

        bp.maxOre = maxOf(bp.oreRobot.oreCost, bp.clayRobot.oreCost, bp.obsidianRobot.oreCost, bp.geodeRobot.oreCost);

        bp.maxClay = maxOf(bp.oreRobot.clayCost, bp.clayRobot.clayCost, bp.obsidianRobot.clayCost, bp.geodeRobot.clayCost);

        bp.maxObsidian = maxOf(bp.oreRobot.obsidianCost, bp.clayRobot.obsidianCost, bp.obsidianRobot.obsidianCost, bp.geodeRobot.obsidianCost);

        return bp;
    }

    public static int maxOf(int first, int... rest) {
        int ret = first;
        for (int val : rest) {
            ret = Math.max(ret, val);
        }
        return ret;
    }

}
