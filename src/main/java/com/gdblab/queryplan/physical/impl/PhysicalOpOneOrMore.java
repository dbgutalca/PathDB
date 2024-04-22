package com.gdblab.queryplan.physical.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.transform.Result;

import com.gdblab.algebra.PathAlgebra;
import com.gdblab.queryplan.logical.impl.LogicalOpOneOrMore;
import com.gdblab.queryplan.physical.PhysicalOperator;
import com.gdblab.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.queryplan.util.Utils;
import com.gdblab.schema.Path;

public class PhysicalOpOneOrMore extends UnaryPhysicalOp {

    protected final LogicalOpOneOrMore loom;
    protected Path slot = null;
    protected final List<Path> childRows;
    protected final List<Path> originalChild;
    private List<Path> results;
    private Iterator<Path> childIterator;
    private Integer counterFixPoint = 0;

    public PhysicalOpOneOrMore(final PhysicalOperator child, final LogicalOpOneOrMore loom) {
        super(child);
        this.loom = loom;
        results = new LinkedList<Path>();
        childRows = Utils.iterToList(child);
        originalChild = Utils.iterToList(child);
        childIterator = childRows.iterator();
    }

    @Override
    public void acceptVisitor(PhysicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean hasNext() {
        if ( slot == null ) {
            slot = getNextPath();
            return slot != null;
        }
        return true;
    }

    @Override
    public Path next() {
        final Path r = slot;
        slot = null;
        return r;
    }

    protected Path getNextPath() {
        for ( ;; ) {
            if (counterFixPoint >= 3) {
                return null;
            }
    
            while (childIterator.hasNext()) {
                Path path = childIterator.next();
                results.add(path);
                return path;
            }

            for (Path result_path: results) {
                for (Path orinigal_path: originalChild) {
                    Path newPath = PathAlgebra.NodeLink(result_path, orinigal_path);
                    if (newPath != null) {
                        childRows.add(newPath);
                    }
                }
            }

            childIterator = childRows.iterator();
            results.clear();        
            counterFixPoint++;
        }
    }
    
}
