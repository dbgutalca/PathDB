package com.gdblab.algorithms;

import java.util.*;

import com.gdblab.converter.RPQtoER;

class LabeledEdgeGraph {
    private Map<Integer, List<Pair<Integer, String>>> graph;

    public LabeledEdgeGraph(String regex) {
        this.graph = new HashMap<>();
        // this.regexMatcher = new RegexMatcher(regex);
    }

    public void addEdge(int from, int to, String label) {
        this.graph.putIfAbsent(from, new ArrayList<>());
        this.graph.get(from).add(new Pair<>(to, label));
    }

    public void dfs(int start) {
        Set<String> visitedEdges = new HashSet<>();
        List<String> path = new ArrayList<>();
        dfsUtil(start, visitedEdges, path);
    }

    private void dfsUtil(int current, Set<String> visitedEdges, List<String> path) {
        String p = "";
        for (String string : path) {
            p += string;
        }

        // if (regexMatcher.match(p)) {
        //     System.out.println("Path: " + p + "  Match: " + regexMatcher.match(p));
        // }

        for (Pair<Integer, String> neighbor : graph.getOrDefault(current, new ArrayList<>())) {
            String edge = current + "-" + neighbor.getKey() + "-" + neighbor.getValue();
            if (!visitedEdges.contains(edge)) {
                visitedEdges.add(edge);
                
                path.add(neighbor.getValue());
                dfsUtil(neighbor.getKey(), visitedEdges, path);
                
                path.remove(path.size() - 1);
                visitedEdges.remove(edge);
            }
        }
    }

    static class Pair<K, V> {
        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    public Set<Integer> getNodes() {
        return graph.keySet();
    }
}

public class DFSTest {
    public static void main(String[] args) {
        System.out.println("Method: DFS\n");

        String er = "A*";

        // System.out.println(RPQtoER.Convert(rpq));
        
        LabeledEdgeGraph graph = new LabeledEdgeGraph(er);
        graph.addEdge(1, 1, "C");
        graph.addEdge(1, 2, "B");
        graph.addEdge(2, 1, "C");
        graph.addEdge(2, 3, "A");
        graph.addEdge(2, 5, "A");
        graph.addEdge(2, 5, "B");
        graph.addEdge(3, 3, "B");
        graph.addEdge(4, 2, "C");
        graph.addEdge(3, 4, "D");
        graph.addEdge(5, 4, "B");

        
        Set<Integer> nodes = graph.getNodes();

        for (Integer source : nodes) {
            graph.dfs(source);   
        }

    }
}
