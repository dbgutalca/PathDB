package com.gdblab.algebra.queryplan.physical.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.gdblab.algebra.queryplan.logical.impl.LogicalOpRecursive;
import com.gdblab.algebra.queryplan.physical.PhysicalOperator;
import com.gdblab.algebra.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.algebra.queryplan.util.Utils;
import com.gdblab.execution.Context;
import com.gdblab.graph.schema.Node;
import com.gdblab.graph.schema.Path;

public class PhysicalOpRecursive extends BinaryPhysicalOp {

    protected final LogicalOpRecursive lop;
    protected Path slot = null;
    protected List<Path> leftList = new ArrayList<>();
    protected List<Path> rightList = new ArrayList<>();
    protected final List<Path> resultsList = new ArrayList<>();
    
    protected Iterator<Path> leftIterator = null;
    protected Iterator<Path> rightIterator = null;

    protected HashMap<String, List<Path>> HashTable = new HashMap<>();

    protected Path nextLeft = null;
    protected Path nextRight = null;
        
    protected final Integer maxRecursion;
    protected Integer currentRecursion;

    public PhysicalOpRecursive(final PhysicalOperator leftChild, final PhysicalOperator rightChild , final LogicalOpRecursive lop) {
        super(leftChild, rightChild);
        this.maxRecursion = Context.getInstance().getMaxRecursion();
        this.lop = lop;
        if (this.lop.hasLastFilter()) {
            saveRightListAsHashMap(leftChild);
        }
        else {
            saveLeftListAsHashMap(rightChild);
        }
        
        this.currentRecursion = 1;
    }

    @Override
    public void acceptVisitor(final PhysicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean hasNext() {
        if ( slot == null ) {
            slot = this.lop.hasLastFilter() ? getNextRightPath() : getNextLeftPath();
            return slot != null;
        }
        return true;
    }

    @Override
    public Path next() {
        if (this.maxRecursion == 0) return null;
        final Path r = slot;
        slot = null;
        return r;
    }

    protected Path getNextRightPath() {
        System.out.println("Right");
        while (true) {
            while (this.rightChild.hasNext()) {
                final Path path = this.rightChild.next();
    
                if (path.getEdgeLength() <= Context.getInstance().getFixPoint()) {
                    this.rightList.add(path);
                    return path;
                }
            }

            if (this.rightList.isEmpty()) return null;

            if (this.currentRecursion == this.maxRecursion) return null;
    
            if (this.rightIterator == null) { 
                this.rightIterator = this.rightList.iterator();
            }
    
            while (rightIterator.hasNext() || this.nextRight != null) {
                if (this.nextRight == null) {
                    this.nextRight = rightIterator.next();
                }

                if (this.leftIterator == null) { 
                    this.leftIterator = this.HashTable.get(this.nextRight.first()).iterator();
                }
    
                while (this.leftIterator.hasNext()) {
                    final Path leftPath = this.leftIterator.next();
                    final Path result = Utils.NodeLink(leftPath, this.nextRight);

                    if (result != null) {
                        this.resultsList.add(result);
                        return result;
                    }
                }
    
                this.nextRight = null;
                this.leftIterator = null;
            }

            this.rightList.clear();
            this.rightList.addAll(this.resultsList);
            this.resultsList.clear();
            this.leftIterator = null;
            this.rightIterator = null;
    
            this.currentRecursion++;
        }
    }

    protected Path getNextLeftPath() {
        while (true) {
            
            
            while (this.leftChild.hasNext()) {
                final Path path = this.leftChild.next();
    
                if (path.getEdgeLength() <= Context.getInstance().getFixPoint()) {
                    this.leftList.add(path);
                    return path;
                }
            }

            if (this.leftList.isEmpty()) return null;

            if (this.currentRecursion == this.maxRecursion) return null;
    
            if (this.leftIterator == null) { 
                this.leftIterator = this.leftList.iterator();
            }
    
            while (this.leftIterator.hasNext() || this.nextLeft != null) {
                if (this.nextLeft == null) {
                    this.nextLeft = this.leftIterator.next();
                }

                if (this.rightIterator == null) {
                    List<Path> r = this.HashTable.get(this.nextLeft.last().getId());
                    if (r != null) {
                        this.rightIterator = r.iterator();
                    }
                    
                }
    
                while (this.rightIterator != null && this.rightIterator.hasNext()) {
                    final Path rightPath = this.rightIterator.next();
                    final Path result = Utils.NodeLink(this.nextLeft, rightPath);

                    if (result != null) {
                        this.resultsList.add(result);
                        return result;
                    }
                }
    
                this.nextLeft = null;
                this.rightIterator = null;
            }

            this.leftList.clear();
            this.leftList.addAll(this.resultsList);
            this.resultsList.clear();
            this.rightIterator = null;
            this.leftIterator = null;
    
            this.currentRecursion++;
        }
    }
    
    private void saveLeftListAsHashMap(PhysicalOperator op) {
        while (op.hasNext()) {
            final Path path = op.next();
            if (path.getEdgeLength() <= Context.getInstance().getFixPoint()) {
                final String key = path.first().getId();
                if (!this.HashTable.containsKey(key)) {
                    this.HashTable.put(key, new ArrayList<>());
                }
                this.HashTable.get(key).add(path);
            }
            
        }
        
    }
    
    private void saveRightListAsHashMap(PhysicalOperator op) {
        while (op.hasNext()) {
            final Path path = op.next();
            if (path.getEdgeLength() <= Context.getInstance().getFixPoint()) {
                final String key = path.last().getId();
                if (!this.HashTable.containsKey(key)) {
                    this.HashTable.put(key, new ArrayList<>());
                }
                this.HashTable.get(key).add(path);
            }
        }
    }
}
