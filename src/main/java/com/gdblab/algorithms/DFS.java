package com.gdblab.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gdblab.automata.RegexMatcher;
import com.gdblab.database.Database;
import com.gdblab.schema.Edge;
import com.gdblab.schema.Node;

public class DFS {
    private HashMap<String, Node> nodes;
    private HashMap<String, Edge> edges;
    private List<List<Edge>> paths;
    private RegexMatcher matcher;

    public DFS(Database db, String semantic, String regex) {
        this.matcher = new RegexMatcher(regex);
        paths = new ArrayList<>();
        nodes = db.graph.getNodes();
        edges = db.graph.getEdges();
        switch (semantic) {
            case "Simple Path":
                DFSSimplePath();
                break;
            case "Trail":
                DFSTrail();
                break;
            case "Arbitrary":
                DFSArbitrary(3);
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

                System.out.println("Current Path: " + getPaths(currentPath) + " // Matcher Status: " + matcher.match(getPaths(currentPath))); // <---- COMMENT THIS OUT TO REMOVE PRINTING OF PATHS
                
                if(matcher.match(getPaths(currentPath)) == "Accepted") {
                    allPaths.add(new ArrayList<>(currentPath));
                }

                DFSUtilSimplePath(edge.getTarget(), visited, currentPath, allPaths);
                currentPath.remove(currentPath.size() - 1);
            }
        }
        visited.remove(node.getId());

        this.paths.addAll(allPaths);
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
                }

                DFSUtilTrail(edge.getTarget(), visitedEdges, localPathList, allPaths);

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

        this.paths = allPaths;
    }

    private void DFSUtilArbitrary(Node node, Map<String, Integer> visitCount, List<Edge> localPathList, List<List<Edge>> allPaths, int maxLength) {
        visitCount.put(node.getId(), visitCount.getOrDefault(node.getId(), 0) + 1);

        for (Map.Entry<String, Edge> entry : edges.entrySet()) {
            Edge edge = entry.getValue();

            if (edge.getSource().getId().equals(node.getId()) && visitCount.getOrDefault(edge.getTarget().getId(), 0) < maxLength) {
                localPathList.add(edge);
                
                if(matcher.match(getPaths(localPathList)) == "Accepted") {
                    allPaths.add(new ArrayList<>(localPathList));
                }

                DFSUtilArbitrary(edge.getTarget(), visitCount, localPathList, allPaths, maxLength);
                localPathList.remove(edge);
            }
        }

        visitCount.put(node.getId(), visitCount.get(node.getId()) - 1);
    }

    public void printCompletePaths() {
        System.out.println("Paths: ");
        int i = 0;
        if (this.paths.isEmpty()) {
            System.out.println("No paths found");
        }
        else {
            for (List<Edge> path : this.paths) {
                if (path.isEmpty()) {
                    continue;
                }
                System.out.print("Path #" + i + ": " + path.get(0).getSource().getId());
                for (Edge edge : path) {
                    System.out.print(" " + edge.getLabel() + " " + edge.getTarget().getId());
                }
                System.out.println();
                i++;
            }
        }
    }

    public void printMinPaths() {
        System.out.println("Paths: ");

        if (this.paths.isEmpty()) {
            System.out.println("No paths found");
        }
        else {
            for (List<Edge> path : this.paths) {
            if (path.isEmpty()) {
                continue;
            }

            System.out.print("Path: ");
            
            for (Edge edge : path) {
                System.out.print(edge.getLabel());
            }
            System.out.println();
        }
        }
    }

    private ArrayList<String> getAllPaths() {
        ArrayList<String> paths = new ArrayList<>();
        for (List<Edge> path : this.paths) {
            if (path.isEmpty()) {
                continue;
            }

            String pathString = "";
            for (Edge edge : path) {
                pathString += edge.getLabel();
            }
            paths.add(pathString);
        }
        return paths;
    }

    private String getPaths(List<Edge> path) {
        String pathString = "";
        for (Edge edge : path) {
            pathString += edge.getLabel();
        }

        return pathString;
    }

    private void checkZeroPaths() {
        if(matcher.match("") == "Accepted") {
            for (Node node : nodes.values()) {
                Edge e = new Edge("", "_", node, node);
                List<Edge> path = new ArrayList<>();
                path.add(e);
                this.paths.add(path);
            }
        }
    }

}
