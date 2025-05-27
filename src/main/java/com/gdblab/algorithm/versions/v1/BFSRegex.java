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
    public void execute() {
        checkZeroPaths();
        Iterator<Node> nodes = Graph.getGraph().getNodeIterator();

        Integer semantic = Context.getInstance().getSemantic();
        switch (semantic) {
            case 1:
                while (nodes.hasNext()) {
                    Node node = nodes.next();
                    walk(node);
                }
                break;
            case 2:
                while (nodes.hasNext()) {
                    Node node = nodes.next();
                    trail(node);
                }
                break;
            case 3:
                while (nodes.hasNext()) {
                    Node node = nodes.next();
                    simple(node);
                }
                break;
            case 4:
                while (nodes.hasNext()) {
                    Node node = nodes.next();
                    acyclic(node);
                }
                break;
        }
    }

    private void walk(Node node) {

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
                
                if ( currentVisitCount.getOrDefault(e.getId(), 0) <= this.maxAppearances) {
                    Path p = new Path("", currentPath.getEdgeSequence());
                    p.insertEdge(e);
                    Map<String, Integer> visitCountMap = new HashMap<>(currentVisitCount);
                    visitCountMap.put(e.getId(), visitCountMap.getOrDefault(e.getId(), 0) + 1);
                    queue.add(new PathWithGOCount(p, visitCountMap));
                }
            }
        }

    }

    private void trail(Node node) {
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
                    queue.add(new PathWithGOCount(p, visitCountMap));
                }
                
            }
        }

        return;
    }

    private void simple(Node node) {
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

    private void acyclic(Node node) {
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

        counter++;
    }

    @Override
    public int getTotalPaths() {
        return counter;
    }
    
}
