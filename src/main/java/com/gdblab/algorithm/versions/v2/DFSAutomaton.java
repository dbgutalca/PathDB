package com.gdblab.algorithm.versions.v2;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gdblab.algebra.queryplan.util.Utils;
import com.gdblab.algorithm.automata.RegexMatcher;
import com.gdblab.algorithm.versions.utils.Algorithm;
import com.gdblab.execution.Context;
import com.gdblab.graph.Graph;
import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.GraphObject;
import com.gdblab.graph.schema.Node;
import com.gdblab.graph.schema.Path;

public class DFSAutomaton implements Algorithm {
    private List<Node> nodes;
    private List<Edge> edges;

    private String ns;
    private String ne;

    private RegexMatcher matcher;

    final private Integer fixPoint;

    private int counter = 1;

    private boolean isExperimental;

    private Writer writer;

    public DFSAutomaton(String regex, Writer writer) {
        this.matcher = new RegexMatcher(regex);
        this.fixPoint = 3;

        // this.ns = Context.getInstance().getStartNode();
        this.ns = "";
        // this.ne = Context.getInstance().getEndNode();
        this.ne = "";

        this.nodes = Utils.nodesIterToList(Graph.getGraph().getNodeIterator());
        this.edges = Utils.edgesIterToList(Graph.getGraph().getEdgeIterator());

        this.isExperimental = Context.getInstance().isExperimental();
        this.writer = writer;
    }

    public DFSAutomaton(String regex) {
        this.matcher = new RegexMatcher(regex);
        this.fixPoint = 10;

        // this.ns = Context.getInstance().getStartNode();
        this.ns = "";
        // this.ne = Context.getInstance().getEndNode();
        this.ne = "";

        this.nodes = Utils.nodesIterToList(Graph.getGraph().getNodeIterator());
        this.edges = Utils.edgesIterToList(Graph.getGraph().getEdgeIterator());

        this.isExperimental = Context.getInstance().isExperimental();
        this.writer = null;
    }

    @Override
    public void Arbitrary() {
        Iterator<Node> nodes = this.nodes.iterator();

        while (nodes.hasNext()) {
            Map <String, Integer> visitCount = new HashMap<>();
            Path iterPath = new Path("");
            Node node = nodes.next();
            ArbitraryUtils(node, visitCount, iterPath);
        }
        
        checkZeroPaths();
    }

    private void ArbitraryUtils(Node node, Map<String, Integer> visitCount, Path iterPath) {
        
        visitCount.put(node.getId(), visitCount.getOrDefault(node.getId(), 0) + 1);

        Iterator<Edge> edgeIt = edges.iterator();

        while (edgeIt.hasNext()) {
            Edge edge = edgeIt.next();

            if (edge.getSource().getId().equals(node.getId()) && visitCount.getOrDefault(edge.getTarget().getId(), 0) <= this.fixPoint) {
                iterPath.insertEdge(edge);

                if ( this.matcher.match(iterPath.getStringEdgeSequence()) == "Accepted" ) {
                    Path newPath = new Path("");
                    newPath.setSequence(iterPath.getSequence());
                    printPath(newPath);
                    ArbitraryUtils(edge.getTarget(), visitCount, iterPath);
                }

                else if ( this.matcher.match(iterPath.getStringEdgeSequence()) == "Substring") {
                    ArbitraryUtils(edge.getTarget(), visitCount, iterPath);
                }
                
                iterPath.pop();
            }
        }

        visitCount.put(node.getId(), visitCount.getOrDefault(node.getId(), 0) - 1);
    }

    @Override
    public void Trail() {
        checkZeroPaths();
        
        if (ns.equals("")) {
            Iterator<Node> nodes = this.nodes.iterator();
            while (this.counter <= 100 && nodes.hasNext()) {
                Node node = nodes.next();
                Set<String> visitedEdges = new HashSet<>();
                Path iterPath = new Path("");
                TrailUtils(node, visitedEdges, iterPath);
            }
        }
        else {
            this.nodes.stream().filter(node -> node.getId().equals(ns)).findFirst().ifPresent(node -> {
                Set<String> visitedEdges = new HashSet<>();
                Path iterPath = new Path("");
                TrailUtils(node, visitedEdges, iterPath);
            });
        }
    }

    private void TrailUtils(Node node, Set<String> visitedEdges, Path iterPath) {

        Iterator<Edge> neighbours = Graph.getGraph().getNeighbours(node.getId()).iterator();

        while (this.counter <= 100 && neighbours.hasNext()) {
            Edge edge = neighbours.next();

            if (!visitedEdges.contains(edge.getId())) {
                iterPath.insertEdge(edge);
                visitedEdges.add(edge.getId());

                if ( this.matcher.match(iterPath.getStringEdgeSequenceAscii()) == "Accepted" ) {
                    Path newPath = new Path("");
                    newPath.setSequence(iterPath.getSequence());
                    if (this.ne.equals("")) {
                        if (isExperimental) {
                            // this.writePath(newPath);
                            this.printPath(newPath);
                        }
                        else {
                            this.printPath(newPath);
                        }
                    }
                    else {
                        if (newPath.last().getId().equals(ne)) {
                            if (isExperimental) {
                                // this.writePath(newPath);    
                                this.printPath(newPath);
                            }
                            else {
                                this.printPath(newPath);
                            }
                        }
                    }
                    TrailUtils(edge.getTarget(), visitedEdges, iterPath);
                }
                
                else if ( this.matcher.match(iterPath.getStringEdgeSequenceAscii()) == "Substring" ) {
                    TrailUtils(edge.getTarget(), visitedEdges, iterPath);
                }

                iterPath.pop();
                visitedEdges.remove(edge.getId());
            }
        }

        return;
    }

    @Override
    public void Simple() {
        Iterator<Node> nodes = this.nodes.iterator();

        while (nodes.hasNext()) {
            Path iterPath = new Path("");
            Node node = nodes.next();
            Set<String> visitedNodes = new HashSet<>();
            SimpleUtils(node, visitedNodes, iterPath);
        }

        checkZeroPaths();
    }

    private void SimpleUtils(Node node, Set<String> visitedNodes, Path iterPath) {
        visitedNodes.add(node.getId());

        Iterator<Edge> edgeIt = edges.iterator();

        while (edgeIt.hasNext()) {
            Edge edge = edgeIt.next();

            if (edge.getSource().getId().equals(node.getId()) && !visitedNodes.contains(edge.getTarget().getId())) {
                iterPath.insertEdge(edge);

                if ( this.matcher.match(iterPath.getStringEdgeSequence()) == "Accepted" ) {
                    Path newPath = new Path("");
                    newPath.setSequence(iterPath.getSequence());
                    printPath(newPath);
                    SimpleUtils(edge.getTarget(), visitedNodes, iterPath);
                }
                
                else if ( this.matcher.match(iterPath.getStringEdgeSequence()) == "Substring" ) {
                    SimpleUtils(edge.getTarget(), visitedNodes, iterPath);
                }

                iterPath.pop();
            }
        }

        visitedNodes.remove(node.getId());
    }

    @Override
    public void checkZeroPaths() {
        if (this.matcher.match("") == "Accepted") {
            Iterator<Node> it = nodes.iterator();

            while (it.hasNext()) {
                Node node = it.next();

                // if (!Context.getInstance().getStartNode().equals("") &&
                //     !Context.getInstance().getStartNode().equals(node.getId())) {
                //     continue;
                // }

                // if (!Context.getInstance().getEndNode().equals("") &&
                //     !Context.getInstance().getEndNode().equals(node.getId())) {
                //     continue;
                // }

                Path path = new Path("");
                path.insertNode(node);
                
                if (isExperimental) {
                    this.writePath(path);
                }
                else {
                    this.printPath(path);
                }
            }
        }
    }

    @Override
    public void printPath(Path p) {

        if (this.counter <= 100) {
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
    public void writePath(Path p) {
        counter++;
    }

    
    @Override
    public int getTotalPaths() {
        return counter - 1;
    }
    
}
