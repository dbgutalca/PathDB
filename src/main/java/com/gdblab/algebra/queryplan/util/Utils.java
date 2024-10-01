package com.gdblab.algebra.queryplan.util;

import com.gdblab.algebra.queryplan.physical.PhysicalOperator;
import com.gdblab.execution.Context;
import com.gdblab.schema.Path;

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
    
}
