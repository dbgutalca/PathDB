package com.gdblab.algebra.queryplan.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.gdblab.algebra.queryplan.physical.PhysicalOperator;
import com.gdblab.execution.Context;
import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.Node;
import com.gdblab.graph.schema.Path;
import com.gdblab.response.ResponseQuery;
import com.gdblab.response.content.Content;

public class Utils {
    
    public static List<Path> iterToList(final PhysicalOperator physicalOp) {
        List<Path> l = new ArrayList<>();
        while (physicalOp.hasNext()) {
            l.add(physicalOp.next());
        }
        return l;
    }
    
    public static List<Edge> edgesIterToList( Iterator<Edge> edges) {
        List<Edge> l = new ArrayList<>();
        while (edges.hasNext()) {
            l.add(edges.next());
        }
        return l;
    }

    public static List<Node> nodesIterToList( Iterator<Node> nodes) {
        List<Node> l = new ArrayList<>();
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
        
        if (pathA.isNodeLinkable(pathB) && pathA.getSumEdges(pathB) <= Context.getInstance().getMaxPathsLength()) {

            switch (Context.getInstance().getSemantic()) {
                case 2:
                    if (!pathA.isTrail(pathB)) return null;
                    break;
                case 3:
                    if (!pathA.isAcyclic(pathB)) return null;
                    break;
                case 4:
                    if (!pathA.isSimplePath(pathB)) return null;
                    break;
            }

            Path join_path = new Path("", (pathA.getEdgeLength() + pathB.getEdgeLength()));

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
    
    public static ResponseQuery calculatePaths(PhysicalOperator po){
        ResponseQuery response = new ResponseQuery();

        Integer counterLP = 1;
        Integer limitCalculatePaths = Context.getInstance().getLimit();

        ArrayList<Content> returnContentList = Context.getInstance().getReturnedVariables();

        List<String> columnNames = returnContentList.stream().map(Content::getReturnName).toList();
        // columnNames.add(0, "#");
        response.setColumnNames(columnNames);
        
        while (counterLP <= limitCalculatePaths && po.hasNext()) {
            
            Path p = po.next();

            ArrayList<Object> row = new ArrayList<>();
            row.add(String.valueOf(counterLP));
            for (Content returnContent : returnContentList) {
                String content = returnContent.getContent(p);
                row.add(content);
            }

            response.addResultContent(row);
            
            counterLP++;
        }

        return response;
    }

}
