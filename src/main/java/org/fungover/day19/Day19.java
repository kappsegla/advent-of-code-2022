package org.fungover.day19;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.fungover.day19.Day19.ROBOT.*;
import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day19 {

    public static void main(String[] args) {
        String s = stringFromFile(resourceStringToPath("/day19/day19.txt"));
        var bluePrints = s.lines().map(BluePrint::of).toList();

        //Step1
//        System.out.println(measurePerf(Day19::executeWithStream,bluePrints));
//        System.out.println(measurePerf(Day19::executeWithParallelStream,bluePrints));
        System.out.println("Step1: " + bluePrints.parallelStream().mapToInt(Day19::getQualityLevel).sum());

        //Step2
        System.out.println("Step2: " + bluePrints.parallelStream().limit(3).mapToInt(b -> getMaxGeodes(b, 32)).reduce(1, (a, b) -> a * b));
    }

    static void executeWithStream(List<BluePrint> bluePrints) {
        System.out.println("Step1: " + bluePrints.stream().mapToInt(Day19::getQualityLevel).sum());
    }

    static void executeWithParallelStream(List<BluePrint> bluePrints) {
        System.out.println("Step1: " + bluePrints.parallelStream().mapToInt(Day19::getQualityLevel).sum());
    }


    private static int getQualityLevel(final BluePrint b) {
        return getMaxGeodes(b, 24) * b.id();
    }

    private static int getMaxGeodes(BluePrint b, int minutes) {
        Map<State, Integer> cache = new ConcurrentHashMap<>();
        int result = 0;
        result = getMaxGeodesForType(cache, b, minutes - 1, ORE, 1, 0, 0, 0, 1, 0, 0, 0);
        result = Math.max(result, getMaxGeodesForType(cache, b, minutes - 1, CLAY, 1, 0, 0, 0, 1, 0, 0, 0));
        result = Math.max(result, getMaxGeodesForType(cache, b, minutes - 1, OB, 1, 0, 0, 0, 1, 0, 0, 0));
        result = Math.max(result, getMaxGeodesForType(cache, b, minutes - 1, GEO, 1, 0, 0, 0, 1, 0, 0, 0));
//        try(ExecutorService pool = Executors.newFixedThreadPool(3)){
//            var task1 = pool.submit(()->getMaxGeodesForType(cache, b, minutes - 1, ORE, 1, 0, 0, 0, 1, 0, 0, 0));
//            var task2 = pool.submit(()->getMaxGeodesForType(cache, b, minutes - 1, CLAY, 1, 0, 0, 0, 1, 0, 0, 0));
//            var task3 = pool.submit(()->getMaxGeodesForType(cache, b, minutes - 1, OB, 1, 0, 0, 0, 1, 0, 0, 0));
//            result = getMaxGeodesForType(cache, b, minutes - 1, GEO, 1, 0, 0, 0, 1, 0, 0, 0);
//
//            result = Math.max(result, task1.get());
//            result = Math.max(result, task2.get());
//            result = Math.max(result, task3.get());
//        } catch (ExecutionException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        return result;
    }

    private static int getMaxGeodesForType(Map<State, Integer> cache, BluePrint b, int minutesLeft, final ROBOT goal, int nrOre, int nrClay, int nrOb,
                                           int nrGeo,
                                           final int nrOreRobots, final int nrClayRobots, final int nrObRobots, final int nrGeoRobots) {
        if (minutesLeft == 0) {
            return nrGeo;
        }

        // Stop building a robot if we have more of the resource it builds than we need.
        final int maxOre = Math.max(b.oreCost(), Math.max(b.clayOreCost(), Math.max(b.obOreCost(), b.geoObCost())));
        if (goal == ORE && nrOre >= maxOre || goal == CLAY && nrClay >= b.obClayCost()
                || goal == OB && (nrOb >= b.geoObCost() || nrClay == 0) || goal == GEO && nrOb == 0) {
            return 0;
        }

        final State state = new State(nrOre, nrClay, nrOb, nrGeo, nrOreRobots, nrClayRobots, nrObRobots, nrGeoRobots, minutesLeft, goal);

        if (cache.containsKey(state)) {
            return cache.get(state);
        }
        int max = 0;

        while (minutesLeft > 0) {
            if (canBuildOreRobot(b, goal, nrOre)) { // Build ore robot
                int tmpMax = 0;
                for (int newGoal = 0; newGoal < 4; newGoal++) {
                    tmpMax = Math.max(tmpMax,
                            getMaxGeodesForType(cache, b, minutesLeft - 1, ROBOT.values()[newGoal], nrOre - b.oreCost() + nrOreRobots,
                                    nrClay + nrClayRobots, nrOb + nrObRobots, nrGeo + nrGeoRobots, nrOreRobots+1,
                                    nrClayRobots, nrObRobots, nrGeoRobots));
                }
                max = Math.max(max, tmpMax);
                cache.put(state, max);
                return max;
            } else if (canBuildClayRobot(b, goal, nrOre)) { // Build clay robot
                int tmpMax = 0;
                for (int newGoal = 0; newGoal < 4; newGoal++) {
                    tmpMax = Math.max(tmpMax,
                            getMaxGeodesForType(cache, b, minutesLeft - 1, ROBOT.values()[newGoal], nrOre - b.clayOreCost() + nrOreRobots,
                                    nrClay + nrClayRobots, nrOb + nrObRobots, nrGeo + nrGeoRobots, nrOreRobots,
                                    nrClayRobots + 1, nrObRobots, nrGeoRobots));
                }
                max = Math.max(max, tmpMax);
                cache.put(state, max);
                return max;
            } else if (canBuildObRobot(b, goal, nrOre, nrClay)) { // Build ob robot
                int tmpMax = 0;
                for (int newGoal = 0; newGoal < 4; newGoal++) {
                    tmpMax = Math.max(tmpMax,
                            getMaxGeodesForType(cache, b, minutesLeft - 1, ROBOT.values()[newGoal], nrOre - b.obOreCost() + nrOreRobots,
                                    nrClay - b.obClayCost() + nrClayRobots, nrOb + nrObRobots, nrGeo + nrGeoRobots,
                                    nrOreRobots, nrClayRobots, nrObRobots + 1, nrGeoRobots));
                }
                max = Math.max(max, tmpMax);
                cache.put(state, max);
                return max;
            } else if (canBuildGeoRobot(b, goal, nrOre, nrOb)) { // Build geo robot
                int tmpMax = 0;
                for (int newGoal = 0; newGoal < 4; newGoal++) {
                    tmpMax = Math.max(tmpMax,
                            getMaxGeodesForType(cache, b, minutesLeft - 1, ROBOT.values()[newGoal], nrOre - b.geoOreCost() + nrOreRobots,
                                    nrClay + nrClayRobots, nrOb - b.geoObCost() + nrObRobots, nrGeo + nrGeoRobots,
                                    nrOreRobots, nrClayRobots, nrObRobots, nrGeoRobots + 1));
                }
                max = Math.max(max, tmpMax);
                cache.put(state, max);
                return max;
            }
            // Can not build a robot, so continue gathering resources.
            minutesLeft--;
            nrOre += nrOreRobots;
            nrClay += nrClayRobots;
            nrOb += nrObRobots;
            nrGeo += nrGeoRobots;
            max = Math.max(max, nrGeo);
        }
        cache.put(state, max);
        return max;
    }

    private static boolean canBuildGeoRobot(BluePrint b, ROBOT goal, int nrOre, int nrOb) {
        return goal == GEO && nrOre >= b.geoOreCost() && nrOb >= b.geoObCost();
    }

    private static boolean canBuildObRobot(BluePrint b, ROBOT goal, int nrOre, int nrClay) {
        return goal == OB && nrOre >= b.obOreCost() && nrClay >= b.obClayCost();
    }

    private static boolean canBuildClayRobot(BluePrint b, ROBOT goal, int nrOre) {
        return goal == CLAY && nrOre >= b.clayOreCost();
    }

    private static boolean canBuildOreRobot(BluePrint b, ROBOT goal, int nrOre) {
        return goal == ORE && nrOre >= b.oreCost();
    }

    /*
     * Applies the function parameter func, passing n as parameter.
     * Returns the average time (ms.) to execute the function 100 times.
     */

    public static <T> double measurePerf(Consumer<T> consumer, T value) {
        int numOfExecutions = 10;
        double totTime = 0.0;
        for (int i = 0; i < numOfExecutions; i++) {
            double start = System.nanoTime();
            consumer.accept(value);
            double duration = (System.nanoTime() - start) / 1_000_000;
            totTime += duration;
        }
        return totTime / numOfExecutions;
    }


    private record BluePrint(int id, int oreCost, int clayOreCost, int obOreCost, int obClayCost, int geoOreCost,
                             int geoObCost) {

        public static BluePrint of(String line) {
            final Pattern p = Pattern.compile(
                    "Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.");
            final Matcher m = p.matcher(line);
            if (m.find()) {
                return new BluePrint(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)),
                        Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)),
                        Integer.parseInt(m.group(6)), Integer.parseInt(m.group(7)));
            }
            throw new IllegalArgumentException();
        }
    }

    private record State(int nrOre, int nrClay, int nrOb, int nrGeo, int nrOreRobot, int nrClayRobot, int nrObRobot,
                         int nrGeoRobot, int minutesLeft, ROBOT goal) {
    }

    enum ROBOT {
        ORE, CLAY, OB, GEO
    }
}

