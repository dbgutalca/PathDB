package com.gdblab.queryplan.util;

import com.gdblab.queryplan.physical.PhysicalOperator;
import com.gdblab.schema.Path;

import java.util.LinkedList;
import java.util.List;

public class Utils {
    public static List<Path> iterToList(PhysicalOperator physicalOp) {
        List<Path> l = new LinkedList<>();
        while (physicalOp.hasNext()) {
            l.add(physicalOp.next());
        }
        return l;
    }
}
