package com.gdblab.algorithms;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.gdblab.automata.RegexMatcher;
import com.gdblab.database.Database;
import com.gdblab.main.Main;
import com.gdblab.schema.Edge;
import com.gdblab.schema.GraphObject;
import com.gdblab.schema.Node;
import com.gdblab.schema.Path;

public class DFSAutomata {
    private HashMap<String, Node> nodes;
    private HashMap<String, Edge> edges;
    private List<List<Edge>> paths;
    private RegexMatcher matcher;
    private long totalTime = 0;

    public DFSAutomata(Database db, String semantic, String regex) {
        long startTime = 0, endTime = 0;
        this.matcher = new RegexMatcher(regex);
        paths = new ArrayList<>();
        nodes = db.graph.getNodes();
        edges = db.graph.getEdges();
        switch (semantic) {
            case "Simple Path":
                startTime = System.currentTimeMillis();
                DFSSimplePath();
                endTime = System.currentTimeMillis();
                this.totalTime = endTime - startTime;
                break;
            case "Trail":
                startTime = System.currentTimeMillis();
                DFSTrail();
                endTime = System.currentTimeMillis();
                this.totalTime = endTime - startTime;
                break;
            case "Arbitrary":
                startTime = System.currentTimeMillis();
                DFSArbitrary(1);
                endTime = System.currentTimeMillis();
                this.totalTime = endTime - startTime;
                break;
        }
        checkZeroPaths();
    }

    private void DFSSimplePath() {
        List<List<Edge>> allPaths = new ArrayList<>();
        List<Edge> currentPath = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        for (Node node : nodes.values()) {
            DFSUtilSimplePath(node, visited, currentPath, allPaths);
        }
    }
    
    private void DFSUtilSimplePath(Node node, Set<String> visited, List<Edge> currentPath, List<List<Edge>> allPaths) {
        visited.add(node.getId());
        for (Edge edge : edges.values()) {
            if (edge.getSource().equals(node) && !visited.contains(edge.getTarget().getId())) {
                currentPath.add(edge);
                
                if(matcher.match(getPaths(currentPath)) == "Accepted") {
                    allPaths.add(new ArrayList<>(currentPath));
                    DFSUtilSimplePath(edge.getTarget(), visited, currentPath, allPaths);
                }
                else if(matcher.match(getPaths(currentPath)) == "Substring") {
                    DFSUtilSimplePath(edge.getTarget(), visited, currentPath, allPaths);
                }
                currentPath.remove(currentPath.size() - 1);
            }
        }
        visited.remove(node.getId());

        this.paths = allPaths;
    }
    
    private void DFSTrail() {
        List<List<Edge>> allPaths = new ArrayList<>();

        Set<String> visitedEdges = new HashSet<>();
        List<Edge> pathList = new ArrayList<>();

        for (Node node : nodes.values()) {
            DFSUtilTrail(node, visitedEdges, pathList, allPaths);
        }
        this.paths = allPaths;
    }

    private void DFSUtilTrail(Node node, Set<String> visitedEdges, List<Edge> localPathList, List<List<Edge>> allPaths) {
        for (Map.Entry<String, Edge> entry : edges.entrySet()) {
            Edge edge = entry.getValue();

            if (edge.getSource().getId().equals(node.getId()) && !visitedEdges.contains(edge.getId())) {
                visitedEdges.add(edge.getId());
                localPathList.add(edge);

                if(matcher.match(getPaths(localPathList)) == "Accepted") {
                    allPaths.add(new ArrayList<>(localPathList));
                    DFSUtilTrail(edge.getTarget(), visitedEdges, localPathList, allPaths);
                }
                else if (matcher.match(getPaths(localPathList)) == "Substring") {
                    DFSUtilTrail(edge.getTarget(), visitedEdges, localPathList, allPaths);
                }

                localPathList.remove(edge);
                visitedEdges.remove(edge.getId());
            }
        }
    }

    private void DFSArbitrary(int MaxLength) {
        List<List<Edge>> allPaths = new ArrayList<>();

        Map<String, Integer> visitCount = new HashMap<>();
        List<Edge> pathList = new ArrayList<>();

        for (Node node : nodes.values()) {
            DFSUtilArbitrary(node, visitCount, pathList, allPaths, MaxLength);
        }

        checkPath(allPaths);

        this.paths = allPaths;
    }

    private void DFSUtilArbitrary(Node node, Map<String, Integer> visitCount, List<Edge> localPathList,
        List<List<Edge>> allPaths, int maxLength) {
        visitCount.put(node.getId(), visitCount.getOrDefault(node.getId(), 0) + 1);

        for (Map.Entry<String, Edge> entry : edges.entrySet()) {
            Edge edge = entry.getValue();

            if (edge.getSource().getId().equals(node.getId())
                    && visitCount.getOrDefault(edge.getTarget().getId(), 0) <= 3) {
                localPathList.add(edge);

                if (matcher.match(getPaths(localPathList)) == "Accepted") {
                    allPaths.add(new ArrayList<>(localPathList));
                    DFSUtilArbitrary(edge.getTarget(), visitCount, localPathList, allPaths, maxLength);
                }
                else if (matcher.match(getPaths(localPathList)) == "Substring") {
                    DFSUtilArbitrary(edge.getTarget(), visitCount, localPathList, allPaths, maxLength);
                }
                
                localPathList.remove(edge);
            }
        }

        visitCount.put(node.getId(), visitCount.get(node.getId()) - 1);
    }

    private void checkZeroPaths() {
        if (matcher.match("") == "Accepted") {
            for (Node node : nodes.values()) {
                Edge e = new Edge("", "", node, new Node(""));
                List<Edge> path = new ArrayList<>();
                path.add(e);
                this.paths.add(path);
            }
        }
    }

    private String getPaths(List<Edge> path) {
        String pathString = "";
        for (Edge edge : path) {
            pathString += edge.getLabel();
        }

        return pathString;
    }

    public void printPath(String output) {

		String outputFile = Main.output;

        try (FileWriter fstream = new FileWriter(outputFile);
            BufferedWriter writer = new BufferedWriter(fstream)) {
            writer.write("--Configuration--"); writer.newLine();
			writer.write("Algorithm: " + Main.algorithm); writer.newLine();
			writer.write("Database: " + Main.dburl); writer.newLine();
			writer.write("Semantic: " + Main.semantic); writer.newLine();
			writer.write("RPQ: " + Main.rpq); writer.newLine();
			writer.write("Time: " + this.totalTime + " ms"); writer.newLine();
            writer.write("--Configuration--"); writer.newLine();
            writer.write("--Paths--"); writer.newLine();
            for (List<Edge> list : paths) {
                int i = 0;
                for (Edge edge : list) {
                    if(i == 0){
                        writer.write(edge.getSource().getId() + " " + edge.getLabel() + " " + edge.getTarget().getId());
                    }
                    else{
                        writer.write(" " + edge.getLabel() + " " + edge.getTarget().getId());
                    }
                    i++;
                }
                writer.newLine();
            }
            writer.write("--Paths--"); writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_ITALIC = "\u001B[3m";
    
    private static void checkPath(List<List<Edge>> paths) {
        List<List<Edge>> pathsToRemove = new ArrayList<>();

        for (List<Edge> p : paths) {
            Map<String, Integer> c = new HashMap<>();

            for (Edge e : p) {
                if (c.containsKey(e.getId())) {
                    c.put(e.getId(), c.get(e.getId()) + 1);
                } else {
                    c.put(e.getId(), 1);
                }
            }

            for (Map.Entry<String, Integer> entry : c.entrySet()) {
                if (entry.getValue() >= 3) {
                    pathsToRemove.add(p);
                    break; // Agregar break para evitar eliminar el mismo camino más de una vez
                }
            }
        }

        paths.removeAll(pathsToRemove);
    }
}
