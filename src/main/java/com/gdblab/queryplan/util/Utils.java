package com.gdblab.queryplan.util;

import com.gdblab.execution.Context;
import com.gdblab.queryplan.physical.PhysicalOperator;
import com.gdblab.schema.Path;
import com.gdblab.schema.PathInterface;

import java.util.LinkedList;
import java.util.List;

public class Utils {
    public static List<PathInterface> iterToList(final PhysicalOperator physicalOp) {
        List<PathInterface> l = new LinkedList<>();
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
    
}
