package com.gdblab.algebra.queryplan.util;

import com.gdblab.algebra.queryplan.physical.PhysicalOperator;
import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.GraphObject;
import com.gdblab.graph.schema.Node;
import com.gdblab.graph.schema.Path;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Utils {
    public static List<Path> iterToList(final PhysicalOperator physicalOp) {
        List<Path> l = new LinkedList<>();
        while (physicalOp.hasNext()) {
            l.add(physicalOp.next());
        }
        return l;
    }
    
    public static List<Edge> edgesIterToList( Iterator<Edge> edges) {
        List<Edge> l = new LinkedList<>();
        while (edges.hasNext()) {
            l.add(edges.next());
        }
        return l;
    }

    public static List<Node> nodesIterToList( Iterator<Node> nodes) {
        List<Node> l = new LinkedList<>();
        while (nodes.hasNext()) {
            l.add(nodes.next());
        }
        return l;
    }

    public static String getTime(long start, long end){
        long duration = end - start;
        double durationInSeconds = (double) duration / 1_000_000_000.0;
        return String.format("%.3f", durationInSeconds);
    }

    public static Path NodeLink (Path pathA, Path pathB) {
        if (pathA.isNodeLinkable(pathB)) {
            Path join_path = new Path("");

            if (pathA.getNodesAmount() == 1 && pathB.getNodesAmount() == 1) {
                join_path.insertNode(pathA.first());
            } else {
                join_path.setSequence(pathA.getSequence());
                join_path.appendSequence(pathB.getSequence());
            }

            return join_path;
        }

        return null;
    }
    
    public static int printAndCountPaths(PhysicalOperator po){
        int counter = 1;

        Integer ms = 10;
        if (ms <= 0) {
            while (po.hasNext()) {
                Path p = po.next();
                System.out.print("Path #" + counter + " - ");
                for (GraphObject go : p.getSequence()) {
                    System.out.print( go.getLabel() + " ");
                }
                System.out.println();
                counter++;
            }
        }

        else {
            while ( po.hasNext() ) {
                Path p = po.next();
    
                if (counter <= ms) {
                    System.out.print("Path #" + counter + " - ");
                    for (GraphObject go : p.getSequence()) {
                        System.out.print( go.getLabel() + " ");
                    }
                    System.out.println();
                }
                if (counter == (ms + 1)) {
                    System.out.println("...");
                }
                counter++;
            }
        }
        
        return counter;
    }
}
