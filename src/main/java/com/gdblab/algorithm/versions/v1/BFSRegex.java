package com.gdblab.algorithm.versions.v1;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Pattern;

import com.gdblab.algebra.queryplan.util.Utils;
import com.gdblab.algorithm.versions.utils.Algorithm;
import com.gdblab.algorithm.versions.utils.PathWithGOCount;
import com.gdblab.execution.Context;
import com.gdblab.graph.Graph;
import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.GraphObject;
import com.gdblab.graph.schema.Node;
import com.gdblab.graph.schema.Path;

public class BFSRegex implements Algorithm {
    private List<Node> nodes;
    private List<Edge> edges;

    private String ns;
    private String ne;

    private Pattern pattern;

    private int fixPoint = 3;

    private int counter = 1;

    private boolean isExperimental;

    private String outputFilename;

    public BFSRegex(String regex) {
        this.pattern = Pattern.compile(regex);
        this.ns = Context.getInstance().getStartNode();
        this.ne = Context.getInstance().getEndNode();

        this.nodes = Utils.nodesIterToList(Graph.getGraph().getNodeIterator());
        this.edges = Utils.edgesIterToList(Graph.getGraph().getEdgeIterator());

        this.isExperimental = Context.getInstance().isExperimental();
        this.outputFilename = Context.getInstance().getResultFilename();
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

            if (pattern.matcher(currentPath.getStringEdgeSequence()).matches()) {
                printPath(currentPath);
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

            if (pattern.matcher(currentPath.getStringEdgeSequenceAscii()).matches()) {
                if (this.ne.equals("")) {
                    if (isExperimental) {
                        this.writePath(currentPath);
                    }
                    else {
                        printPath(currentPath);
                    }
                }
                else {
                    if (currentPath.last().getId().equals(this.ne)) {
                        if (isExperimental) {
                            this.writePath(currentPath);
                        }
                        else {
                            printPath(currentPath);
                        }
                    }
                }
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

            if (e.getSource().getId().equals(node.getId()) && !e.getSource().getId().equals(e.getTarget().getId()) ) {
                Path p = new Path("", e);
                Map<String, Integer> visitCountMap = new HashMap<>();
                visitCountMap.put(e.getSource().getId(), 1);
                visitCountMap.put(e.getTarget().getId(), 1);
                queue.add(new PathWithGOCount(p, visitCountMap));
            }
        }

        while (!queue.isEmpty()) {
            PathWithGOCount current = queue.poll();
            Path currentPath = current.getPath();
            Map<String, Integer> currentVisitCount = current.getVisitedGOCount();

            if (pattern.matcher(currentPath.getStringEdgeSequence()).matches()) {
                printPath(currentPath);
            }

            edgeIt = edges.iterator();

            while (edgeIt.hasNext()) {
                Edge e= edgeIt.next();

                if ( currentPath.last().getId().equals(e.getSource().getId() ) && currentVisitCount.getOrDefault(e.getTarget().getId(), 0) < 1 ) {
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
            Iterator<Node> it = nodes.iterator();

            while (it.hasNext()) {
                Node node = it.next();

                Path path = new Path("");
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
    public void writePath(Path p) {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilename), "utf-8"));

            writer.write("Path #" + counter + " - ");
            for (GraphObject go : p.getSequence()) {
                writer.write(go.getLabel() + " ");
            }
            writer.write("\n");

        } catch (Exception e) {
        } finally {
            try { writer.close(); } catch (Exception e) {}
            counter++;
        }
    }

    @Override
    public int getTotalPaths() {
        return counter - 1;
    }
    
}
