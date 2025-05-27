package com.gdblab.algorithm.versions.v1;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Pattern;

import com.gdblab.algorithm.versions.utils.Algorithm;
import com.gdblab.algorithm.versions.utils.PathWithGOCount;
import com.gdblab.execution.Context;
import com.gdblab.graph.Graph;
import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.GraphObject;
import com.gdblab.graph.schema.Node;
import com.gdblab.graph.schema.Path;

public class BFSRegex implements Algorithm {
    private Pattern pattern;
    private int limit;
    private int maxAppearances;
    private int counter;

    public BFSRegex(String regex) {
        this.pattern = Pattern.compile(regex);
        this.limit = Context.getInstance().getLimit();
        this.maxAppearances = Context.getInstance().getMaxRecursion();
        this.counter = 0;
    }

    @Override
    public void Arbitrary() {
        checkZeroPaths();
        Iterator<Node> nodes = Graph.getGraph().getNodeIterator();

        while (nodes.hasNext()) {
            Node node = nodes.next();
            ArbitraryUtils(node);
        }
    }

    private void ArbitraryUtils(Node node) {

        Queue<PathWithGOCount> queue = new LinkedList<>();

        Iterator<Edge> neighbours = Graph.getGraph().getNeighbours(node.getId()).iterator();

        while (neighbours.hasNext()) {
            Edge e = neighbours.next();
            Path p = new Path("", e);
            Map<String, Integer> visitCountMap = new HashMap<>();
            visitCountMap.put(e.getId(), 1);
            queue.add(new PathWithGOCount(p, visitCountMap));
        }

        while (!queue.isEmpty()) {
            PathWithGOCount current = queue.poll();
            Path currentPath = current.getPath();
            Map<String, Integer> currentVisitCount = current.getVisitedGOCount();

            if (pattern.matcher(currentPath.getStringEdgeSequence()).matches()) {
                printPath(currentPath);
            }

            Iterator<Edge> neighIterator = Graph.getGraph().getNeighbours(currentPath.last().getId()).iterator();

            while (neighIterator.hasNext()) { 
                Edge e = neighIterator.next();
                Path p = new Path("", currentPath.getEdgeSequence());
                p.insertEdge(e);
                Map<String, Integer> visitCountMap = new HashMap<>(currentVisitCount);
                visitCountMap.put(e.getId(), visitCountMap.getOrDefault(e.getId(), 0) + 1);
                if (visitCountMap.get(e.getId()) <= this.maxAppearances) {
                    queue.add(new PathWithGOCount(p, visitCountMap));
                }
            }
        }

    }

    @Override
    public void Trail() {
        checkZeroPaths();
        Iterator<Node> nodes = Graph.getGraph().getNodeIterator();

        while (nodes.hasNext()) {
            Node node = nodes.next();
            TrailUtils(node);
        }
    }

    private void TrailUtils(Node node) {
        Queue<PathWithGOCount> queue = new LinkedList<>();

        Iterator<Edge> neighbours = Graph.getGraph().getNeighbours(node.getId()).iterator();

        while (neighbours.hasNext()) {
            Edge e = neighbours.next();
            Path p = new Path("", e);
            Map<String, Integer> visitCountMap = new HashMap<>();
            visitCountMap.put(e.getId(), 1);
            queue.add(new PathWithGOCount(p, visitCountMap));
        }

        while (!queue.isEmpty()) {
            PathWithGOCount current = queue.poll();
            Path currentPath = current.getPath();
            Map<String, Integer> currentVisitCount = current.getVisitedGOCount();

            if (pattern.matcher(currentPath.getStringEdgeSequenceAscii()).matches()) {
                printPath(currentPath);
            }

            Iterator<Edge> neighIterator = Graph.getGraph().getNeighbours(currentPath.last().getId()).iterator();

            while (neighIterator.hasNext()) {
                Edge e = neighIterator.next();

                if (currentVisitCount.getOrDefault(e.getId(), 0) < 1) {
                    Path p = new Path("", currentPath.getEdgeSequence());
                    p.insertEdge(e);
                    Map<String, Integer> visitCountMap = new HashMap<>(currentVisitCount);
                    visitCountMap.put(e.getId(), visitCountMap.getOrDefault(e.getId(), 0) + 1);
                    if (visitCountMap.get(e.getId()) <= this.maxAppearances) {
                        queue.add(new PathWithGOCount(p, visitCountMap));
                    }
                }
                
            }
        }

        return;
    }

    @Override
    public void Simple() {
        Iterator<Node> nodes = Graph.getGraph().getNodeIterator();

        while (nodes.hasNext()) {
            Node node = nodes.next();
            SimpleUtils(node);
        }
    }

    private void SimpleUtils(Node node) {
        Queue<PathWithGOCount> queue = new LinkedList<>();
        
        Iterator<Edge> neighbours = Graph.getGraph().getNeighbours(node.getId()).iterator();

        while (neighbours.hasNext()) {
            Edge e = neighbours.next();
            Path p = new Path("", e);
            Map<String, Integer> visitCountMap = new HashMap<>();
            visitCountMap.put(e.getTarget().getId(), 1);
            queue.add(new PathWithGOCount(p, visitCountMap));
        }

        while (!queue.isEmpty()) {
            PathWithGOCount current = queue.poll();
            Path currentPath = current.getPath();
            Map<String, Integer> currentVisitCount = current.getVisitedGOCount();

            if (pattern.matcher(currentPath.getStringEdgeSequenceAscii()).matches()) {
                printPath(currentPath);
            }

            Iterator<Edge> neighIterator = Graph.getGraph().getNeighbours(currentPath.last().getId()).iterator();

            while (neighIterator.hasNext()) {
                Edge e = neighIterator.next();

                if (currentVisitCount.getOrDefault(e.getTarget().getId(), 0) < 1) {
                    Path p = new Path("", currentPath.getEdgeSequence());
                    p.insertEdge(e);
                    Map<String, Integer> visitCountMap = new HashMap<>(currentVisitCount);
                    visitCountMap.put(e.getTarget().getId(), visitCountMap.getOrDefault(e.getTarget().getId(), 0) + 1);
                    queue.add(new PathWithGOCount(p, visitCountMap));
                }
            }
        }
    }

    @Override
    public void Acyclic() {
        Iterator<Node> nodes = Graph.getGraph().getNodeIterator();

        while (nodes.hasNext()) {
            Node node = nodes.next();
            AcyclicUtils(node);
        }
    }

    private void AcyclicUtils(Node node) {
        Queue<PathWithGOCount> queue = new LinkedList<>();
        
        Iterator<Edge> neighbours = Graph.getGraph().getNeighbours(node.getId()).iterator();

        while (neighbours.hasNext()) {
            Edge e = neighbours.next();
            Path p = new Path("", e);
            Map<String, Integer> visitCountMap = new HashMap<>();
            visitCountMap.put(e.getTarget().getId(), 1);
            queue.add(new PathWithGOCount(p, visitCountMap));
        }

        while (!queue.isEmpty()) {
            PathWithGOCount current = queue.poll();
            Path currentPath = current.getPath();
            Map<String, Integer> currentVisitCount = current.getVisitedGOCount();

            if (pattern.matcher(currentPath.getStringEdgeSequenceAscii()).matches() && !currentPath.first().equals(currentPath.last())) {
                printPath(currentPath);
            }

            Iterator<Edge> neighIterator = Graph.getGraph().getNeighbours(currentPath.last().getId()).iterator();

            while (neighIterator.hasNext()) {
                Edge e = neighIterator.next();

                if (currentVisitCount.getOrDefault(e.getTarget().getId(), 0) < 1) {
                    Path p = new Path("", currentPath.getEdgeSequence());
                    p.insertEdge(e);
                    Map<String, Integer> visitCountMap = new HashMap<>(currentVisitCount);
                    visitCountMap.put(e.getTarget().getId(), visitCountMap.getOrDefault(e.getTarget().getId(), 0) + 1);
                    queue.add(new PathWithGOCount(p, visitCountMap));
                }
            }
        }
    }

    @Override
    public void checkZeroPaths() {
        if (pattern.matcher("").matches()) {
            Iterator<Node> it = Graph.getGraph().getNodeIterator();

            while (it.hasNext()) {
                Node node = it.next();
                Path path = new Path("");
                path.insertNode(node);
                this.printPath(path);
            }
        }
    } 

    @Override
    public void printPath(Path p) {

        if (this.counter < this.limit) {
            System.out.print("Path #" + counter + " - ");
            for (GraphObject go : p.getSequence()) {
                if (go instanceof Edge) {
                    System.out.print(go.getId() + "(" + go.getLabel() + ") ");
                }
                else {
                    System.out.print(go.getId() + " ");
                }
            }
            System.out.println();
        }    

        // if (this.counter == 11) {
        //     System.out.println("...");

        // }

        counter++;
    }

    @Override
    public int getTotalPaths() {
        return counter - 1;
    }
    
}
