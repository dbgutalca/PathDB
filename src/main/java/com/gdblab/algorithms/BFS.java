package com.gdblab.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.gdblab.automata.RegexMatcher;
import com.gdblab.database.Database;
import com.gdblab.schema.Edge;
import com.gdblab.schema.Node;

public class BFS {
    private HashMap<String, Node> nodes;
    private HashMap<String, Edge> edges;
    private List<List<Edge>> paths;
    private RegexMatcher matcher;

    public BFS(Database db, String                                        semantic, String regex) {
        this.matcher = new RegexMatcher(regex);
        paths = new ArrayList<>();
        nodes = db.graph.getNodes();
        edges = db.graph.getEdges();
        switch (semantic) {
            case "simple_path":
                BFSSimplePath();
                break;
            case "trail":
                BFSTrail();
                break;
            case "arbitrary":
                BFSArbitrary(3);
                break;                                                                 
        }
        
    }

    private void BFSSimplePath() {
        for (Node node : nodes.values()) {
            BFSUtilSimplePath(node);
        }
    }
    private void BFSUtilSimplePath(Node node) {
        List<List<Edge>> paths = new ArrayList<>();

        Queue<List<Edge>> queue = new LinkedList<>();
        Node startNode = nodes.get(node.getId());

        for (Edge edge : edges.values()) {
            if (edge.getSource().equals(startNode)) {
                List<Edge> initialPath = new ArrayList<>();
                initialPath.add(edge);
                queue.add(initialPath);
            }
        }

        while (!queue.isEmpty()) {
            List<Edge> currentPath = queue.poll();
            Edge lastEdge = currentPath.get(currentPath.size() - 1);
            Node currentNode = lastEdge.getTarget();

            // Evitar añadir un nodo ya presente en el camino actual
            if (containsNode(currentPath, currentNode.getId())) {
                continue;
            }

            paths.add(new ArrayList<>(currentPath));

            for (Edge edge : edges.values()) {
                if (edge.getSource().equals(currentNode)) {
                    List<Edge> newPath = new ArrayList<>(currentPath);
                    newPath.add(edge);
                    queue.add(newPath);
                }
            }
        }

        this.paths.addAll(paths);
    }

    private boolean containsNode(List<Edge> path, String nodeId) {
        for (Edge edge : path) {
            if (edge.getSource().getId().equals(nodeId) || edge.getTarget().getId().equals(nodeId)) {
                return true;
            }
        }
        return false;
    }






    private void BFSTrail() {
        for (Node node : nodes.values()) {
            BFSUtilTrail(node);
        }
    }

    private void BFSUtilTrail(Node node) {
        List<List<Edge>> allPaths = new ArrayList<>();
        Queue<List<Edge>> queue = new LinkedList<>();

        // Inicializar la cola con caminos que comienzan en el nodo de inicio
        for (Map.Entry<String, Edge> entry : edges.entrySet()) {
            Edge edge = entry.getValue();
            if (edge.getSource().getId().equals(node.getId())) {
                List<Edge> path = new ArrayList<>();
                path.add(edge);
                queue.add(path);
            }
        }

        while (!queue.isEmpty()) {
            List<Edge> currentPath = queue.remove();
            
            if(matcher.match(getPaths(currentPath)) == "Accepted") {
                allPaths.add(new ArrayList<>(currentPath));
            }

            Set<String> visitedEdgesInPath = new HashSet<>();
            for (Edge e : currentPath) {
                visitedEdgesInPath.add(e.getId());
            }

            Edge lastEdge = currentPath.get(currentPath.size() - 1);
            Node lastNode = lastEdge.getTarget();

            for (Map.Entry<String, Edge> entry : edges.entrySet()) {
                Edge edge = entry.getValue();
                if (!visitedEdgesInPath.contains(edge.getId()) && edge.getSource().getId().equals(lastNode.getId())) {
                    List<Edge> newPath = new ArrayList<>(currentPath);
                    newPath.add(edge);
                    queue.add(newPath);
                }
            }
        }

        this.paths.addAll(allPaths);
    }

    private void BFSArbitrary(int maxCicles) {
        for (Node node : nodes.values()) {
            BFSUtilArbitrary(node, maxCicles);
        }
    }

    private void BFSUtilArbitrary(Node node, int maxCicles) {
        List<List<Edge>> allPaths = new ArrayList<>();
        Queue<PathWithNodeCount> queue = new LinkedList<>();

        for (Map.Entry<String, Edge> entry : edges.entrySet()) {
            Edge edge = entry.getValue();
            if (edge.getSource().getId().equals(node.getId())) {
                List<Edge> path = new ArrayList<>();
                path.add(edge);
                Map<String, Integer> visitCount = new HashMap<>();
                visitCount.put(node.getId(), 1);
                queue.add(new PathWithNodeCount(path, visitCount));
            }
        }

        while (!queue.isEmpty()) {
            PathWithNodeCount current = queue.remove();
            List<Edge> currentPath = current.path;
            Map<String, Integer> currentVisitCount = current.visitCount;

            if(matcher.match(getPaths(currentPath)) == "Accepted") {
                allPaths.add(new ArrayList<>(currentPath));
            }

            Edge lastEdge = currentPath.get(currentPath.size() - 1);
            Node lastNode = lastEdge.getTarget();

            for (Map.Entry<String, Edge> entry : edges.entrySet()) {
                Edge edge = entry.getValue();
                if (edge.getSource().getId().equals(lastNode.getId())) {
                    String targetNodeId = edge.getTarget().getId();
                    int visitCount = currentVisitCount.getOrDefault(targetNodeId, 0);
                    if (visitCount < maxCicles) {
                        List<Edge> newPath = new ArrayList<>(currentPath);
                        newPath.add(edge);
                        Map<String, Integer> newVisitCount = new HashMap<>(currentVisitCount);
                        newVisitCount.put(targetNodeId, visitCount + 1);
                        queue.add(new PathWithNodeCount(newPath, newVisitCount));
                    }
                }
            }
        }

        this.paths.addAll(allPaths);
    }

    private static class PathWithNodeCount {
        List<Edge> path;
        Map<String, Integer> visitCount;

        PathWithNodeCount(List<Edge> path, Map<String, Integer> visitCount) {
            this.path = path;
            this.visitCount = visitCount;
        }
    }

    public void printCompletePaths() {
        int i = 0;
        for (List<Edge> path : this.paths) {
            if (path.isEmpty()) {
                continue;
            }
            System.out.print(i + " " + path.get(0).getSource().getId());
            for (Edge edge : path) {
                System.out.print(" " + edge.getLabel() + " " + edge.getTarget().getId());
            }
            System.out.println();
            i++;
        }
    }

    public void printMinPaths() {
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

    public ArrayList<String> getPaths() {
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

    public String getPaths(List<Edge> path) {
        String pathString = "";
        for (Edge edge : path) {
            pathString += edge.getLabel();
        }

        return pathString;
    }
}
