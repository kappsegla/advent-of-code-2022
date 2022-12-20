package org.fungover.day16;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.fungover.util.FileReader.resourceStringToPath;
import static org.fungover.util.FileReader.stringFromFile;

public class Day16 {

    static Map<String, Valve> valves = new HashMap<>();
    static List<Valve> valvesById = new ArrayList<>();
    static List<Integer> usefullValves = new ArrayList<>();
    static List<Map<String, Integer>> memo = new ArrayList<>();

    public static void main(String[] args) {

        String s = stringFromFile(resourceStringToPath("/day16/day16.txt"));
//        String s = """
//                Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
//                Valve BB has flow rate=13; tunnels lead to valves CC, AA
//                Valve CC has flow rate=2; tunnels lead to valves DD, BB
//                Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
//                Valve EE has flow rate=3; tunnels lead to valves FF, DD
//                Valve FF has flow rate=0; tunnels lead to valves EE, GG
//                Valve GG has flow rate=0; tunnels lead to valves FF, HH
//                Valve HH has flow rate=22; tunnel leads to valve GG
//                Valve II has flow rate=0; tunnels lead to valves AA, JJ
//                Valve JJ has flow rate=21; tunnel leads to valve II
//                """;

        int start = 0;
        int valveId = 0;

        for (var line : s.lines().toList()) {
            var vInfo = line.split("[\\s=;]|(,\\s)");
            var valveName = vInfo[1];
            var flowRate = Integer.parseInt(vInfo[5]);

            //Check if valve already exists
            var valve = valves.computeIfAbsent(valveName, Valve::new);
            valve.flowRate = flowRate;
            valvesById.add(valve);

            if (valve.flowRate > 0) {
                usefullValves.add(valveId);
            }
            for (int i = 11; i < vInfo.length; i++) {
                valve.addTunnel(valves.computeIfAbsent(vInfo[i], Valve::new));
            }
            if (valve.name.equals("AA")) {
                start = valveId;
                usefullValves.add(valveId);
            }
            valveId++;
        }
        var dist_table = calcDist(valvesById);
        //Init memo
        for (int i = 0; i < 30; i++) {
            memo.add(new HashMap<>());
        }

        //Step1
        var init = "0".repeat(usefullValves.size());

        int max_pres = find_max_pressure(0, start, init, dist_table);
        System.out.println(max_pres);
    }

    public static int find_max_pressure(int time, int pos, String opened, int[][] dist) {

        // check if solution to this subproblem has been previously computed
        if (memo.get(time).containsKey(opened)) {
            return memo.get(time).get(opened);
        }
        int max_pressure = 0;
        // iterate over all useful valves
        for (int next = 0; next < usefullValves.size(); next++) {
            // skip valves that are already open
            if (opened.charAt(next) == '1') {
                continue;
            }
            // for every unopened useful valve, ...
            // check if we can open it without exceeding the time limit
            int next_id = usefullValves.get(next);
            int next_time = time + dist[pos][next_id] + 1;
            if (next_time >= 30) {
                continue;
            }
            // if we can open it without exceeding the time limit, ...

            // compute total pressure released by opening that valve next
            var next_valve = valvesById.get(next_id);
            int next_pressure = (30 - next_time) * next_valve.flowRate;

            // update the bitstring since this valve is now open
            char[] openArray = opened.toCharArray();
            openArray[next] = '1';
            var next_opened = String.valueOf(openArray);

            // recursively compute max pressure
            max_pressure = Math.max(max_pressure, next_pressure + find_max_pressure(next_time, next_id, next_opened, dist));

        }
        // memoize result and return
        memo.get(time).put(opened, max_pressure);
        return max_pressure;
    }


    public static int[][] calcDist(List<Valve> valves) {
        int nvalves = valves.size();
        int[][] dist = new int[nvalves][nvalves];

        // init distance array with a large value,
        // and set initial distances between adjacent valves to 1
        for (int row = 0; row < nvalves; row++) {
            for (int col = 0; col < nvalves; col++) {
                dist[row][col] = nvalves + 100;
            }
            for (var v : valves.get(row).tunnelsTo) {
                int vid = valves.indexOf(v);
                dist[row][vid] = 1;
                dist[vid][row] = 1;
            }
        }

        // use Floyd-Warshall to find all-pairs shortest path between all valves
        for (int k = 0; k < nvalves; k++) {
            for (int i = 0; i < nvalves; i++) {
                for (int j = 0; j < nvalves; j++) {
                    dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                }
            }
        }
        return dist;
    }
}

record State(List<Valve> open, Valve pos, long totalFlow) {
}

class Valve {
    String name;
    int flowRate;
    Set<Valve> tunnelsTo = new HashSet<>();

    public Valve(String name) {
        this.name = name;
    }

    public int getFlowRate() {
        return flowRate;
    }

    public void setFlowRate(int flowRate) {
        this.flowRate = flowRate;
    }

    public Set<Valve> getTunnelsTo() {
        return tunnelsTo;
    }

    public String getName() {
        return name;
    }

    public void addTunnel(Valve valve) {
        tunnelsTo.add(valve);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Valve valve = (Valve) o;

        return Objects.equals(name, valve.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
