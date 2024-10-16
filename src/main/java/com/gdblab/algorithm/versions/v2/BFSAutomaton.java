package com.gdblab.algorithm.versions.v2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import com.gdblab.algebra.queryplan.util.Utils;
import com.gdblab.algorithm.automata.RegexMatcher;
import com.gdblab.algorithm.versions.utils.Algorithm;
import com.gdblab.algorithm.versions.utils.PathWithGOCount;
import com.gdblab.execution.Context;
import com.gdblab.graph.Graph;
import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.GraphObject;
import com.gdblab.graph.schema.Node;
import com.gdblab.graph.schema.Path;

public class BFSAutomaton implements Algorithm {

    private List<Node> nodes;
    private List<Edge> edges;

    private String ns;
    private String ne;

    private RegexMatcher matcher;

    final private Integer fixPoint;

    private int counter = 1;

    public BFSAutomaton(String regex) {
        this.matcher = new RegexMatcher(regex);

        this.ns = Context.getInstance().getStartNode();
        this.ne = Context.getInstance().getEndNode();

        this.fixPoint = 3;

        this.nodes = Utils.nodesIterToList(Graph.getGraph().getNodeIterator());
        this.edges = Utils.edgesIterToList(Graph.getGraph().getEdgeIterator());
    }

    @Override
    public void Arbitrary() {
        Iterator<Node> nodes = this.nodes.iterator();

        while (nodes.hasNext()) {
            Node node = nodes.next();
            ArbitraryUtils(node);
        }
    }

    private void ArbitraryUtils(Node node) {
        Queue<PathWithGOCount> queue = new LinkedList<>();

        Iterator<Edge> edgeIt = edges.iterator();

        while (edgeIt.hasNext()) {
            Edge e = edgeIt.next();

            if (e.getSource().getId().equals(node.getId())) {
                Path p = new Path("", e);
                Map<String, Integer> visitCountMap = new HashMap<>();
                visitCountMap.put(e.getId(), 1);
                queue.add(new PathWithGOCount(p, visitCountMap));
            }
        }

        while (!queue.isEmpty()) {
            PathWithGOCount current = queue.poll();
            Path currentPath = current.getPath();
            Map<String, Integer> currentVisitCount = current.getVisitedGOCount();

            if (this.matcher.match(currentPath.getStringEdgeSequence()) == "Accepted") {
                printPath(currentPath);
            }

            else if (this.matcher.match(currentPath.getStringEdgeSequence()) == "Rejected") {
                continue;
            }

            edgeIt = edges.iterator();

            while (edgeIt.hasNext()) {
                Edge e = edgeIt.next();

                if ( currentPath.last().getId().equals(e.getSource().getId()) && currentVisitCount.getOrDefault(e.getId(), 0) < this.fixPoint ) {
                    Path p = new Path("", currentPath.getEdgeSequence());
                    p.insertEdge(e);
                    Map<String, Integer> visitCountMap = new HashMap<>(currentVisitCount);
                    visitCountMap.put(e.getId(), visitCountMap.getOrDefault(e.getId(), 0) + 1);
                    queue.add(new PathWithGOCount(p, visitCountMap));
                }
            }
        }
    }

    @Override
    public void Trail() {
        if (ns.equals("")) {
            Iterator<Node> nodes = this.nodes.iterator();
            while (nodes.hasNext()) {
                Node node = nodes.next();
                TrailUtils(node);
            }
        }
        else {
            this.nodes.stream().filter(node -> node.getId().equals(ns)).findFirst().ifPresent(node -> {
                TrailUtils(node);
            });
        }
    }

    private void TrailUtils(Node node) {
        Queue<PathWithGOCount> queue = new LinkedList<>();

        Iterator<Edge> edgeIt = edges.iterator();

        while (edgeIt.hasNext()) {
            Edge e = edgeIt.next();

            if (e.getSource().getId().equals(node.getId())) {
                Path p = new Path("", e);
                Map<String, Integer> visitCountMap = new HashMap<>();
                visitCountMap.put(e.getId(), 1);
                queue.add(new PathWithGOCount(p, visitCountMap));
            }
        }

        while (!queue.isEmpty()) {
            PathWithGOCount current = queue.poll();
            Path currentPath = current.getPath();
            Map<String, Integer> currentVisitCount = current.getVisitedGOCount();

            if (this.matcher.match(currentPath.getStringEdgeSequence()) == "Accepted") {
                if (this.ne.equals("")) {
                    printPath(currentPath);
                }
                else {
                    if (currentPath.last().getId().equals(this.ne)) {
                        printPath(currentPath);
                    }
                }
            }

            else if (this.matcher.match(currentPath.getStringEdgeSequence()) == "Rejected") {
                continue;
            }

            edgeIt = edges.iterator();

            while (edgeIt.hasNext()) {
                Edge e = edgeIt.next();

                if ( currentPath.last().getId().equals(e.getSource().getId()) && currentVisitCount.getOrDefault(e.getId(), 0) < 1 ) {
                    Path p = new Path("", currentPath.getEdgeSequence());
                    p.insertEdge(e);
                    Map<String, Integer> visitCountMap = new HashMap<>(currentVisitCount);
                    visitCountMap.put(e.getId(), visitCountMap.getOrDefault(e.getId(), 0) + 1);
                    queue.add(new PathWithGOCount(p, visitCountMap));
                }
            }
        }
    }

    @Override
    public void Simple() {
        Iterator<Node> nodes = this.nodes.iterator();

        while (nodes.hasNext()) {
            Node node = nodes.next();
            SimpleUtils(node);
        }
    }

    private void SimpleUtils(Node node) {
        Queue<PathWithGOCount> queue = new LinkedList<>();

        Iterator<Edge> edgeIt = edges.iterator();

        while (edgeIt.hasNext()) {
            Edge e = edgeIt.next();

            if (e.getSource().getId().equals(node.getId()) && !e.getSource().getId().equals(e.getTarget().getId())) {
                Path p = new Path("", e);
                Map<String, Integer> visitCountMap = new HashMap<>();
                visitCountMap.put(e.getId(), 1);
                queue.add(new PathWithGOCount(p, visitCountMap));
            }
        }

        while (!queue.isEmpty()) {
            PathWithGOCount current = queue.poll();
            Path currentPath = current.getPath();
            Map<String, Integer> currentVisitCount = current.getVisitedGOCount();

            if (this.matcher.match(currentPath.getStringEdgeSequence()) == "Accepted") {
                printPath(currentPath);
            }

            else if (this.matcher.match(currentPath.getStringEdgeSequence()) == "Rejected") {
                continue;
            }

            edgeIt = edges.iterator();

            while (edgeIt.hasNext()) {
                Edge e = edgeIt.next();

                if ( currentPath.last().getId().equals(e.getSource().getId()) && currentVisitCount.getOrDefault(e.getId(), 0) < 1 ) {
                    Path p = new Path("", currentPath.getEdgeSequence());
                    p.insertEdge(e);
                    Map<String, Integer> visitCountMap = new HashMap<>(currentVisitCount);
                    visitCountMap.put(e.getId(), visitCountMap.getOrDefault(e.getId(), 0) + 1);
                    queue.add(new PathWithGOCount(p, visitCountMap));
                }
            }
        }
    }

    @Override
    public void checkZeroPaths() {
        if (this.matcher.match("") == "Accepted") {
            for (Node node : nodes) {
                Path path = new Path(UUID.randomUUID().toString());
                path.insertNode(node);
                printPath(path);
            }
        }
    }

    @Override
    public void printPath(Path p) {

        if (this.counter <= 10) {
            System.out.print("Path #" + counter + " - ");
            for (GraphObject go : p.getSequence()) {
                System.out.print( go.getLabel() + " ");
            }
            System.out.println();
        }
        
        if (this.counter == 11) {
            System.out.println("...");

        }

        counter++;
    }

    @Override
    public int getTotalPaths() {
        return counter - 1;
    }
    
}
