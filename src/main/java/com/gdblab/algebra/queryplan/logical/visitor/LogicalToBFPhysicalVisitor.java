package com.gdblab.algebra.queryplan.logical.visitor;

import java.util.Stack;

import com.gdblab.algebra.queryplan.logical.LogicalPlanVisitor;
import com.gdblab.algebra.queryplan.logical.impl.*;
import com.gdblab.algebra.queryplan.physical.PhysicalOperator;
import com.gdblab.algebra.queryplan.physical.PhysicalPlan;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpAllEdges;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpAllNodes;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpBFSAllPathsFromNode;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpBinaryUnion;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpHashNodeJoin;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpHashNodeJoinRight;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpRecursive;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpReverse;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpSelectionByLabel;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpSequentialScan;

public class LogicalToBFPhysicalVisitor implements LogicalPlanVisitor {

    private final Stack<PhysicalOperator> stack = new Stack<>();

    @Override
    public void visit(final LogicalOpSelection logicalOpSelection) {
        //we first visit the children, to keep the structure of the tree with the recursive calls
        logicalOpSelection.getChild().acceptVisitor(this);
        stack.push(new PhysicalOpSequentialScan(stack.pop(), logicalOpSelection));
    }

    @Override
    public void visit(final LogicalOpProjection logicalOpProjection) {

    }

    //JOIN(SELECT(CONJUNTO1), SELECT(Conjunto2))  SeqScan(conjunto1),
    @Override
    public void visit(final LogicalOpNodeJoin logicalOpNodeJoin) {
        //we visit both children, to get the two *push* operations, and thus we can do two *pop*s in the end
        logicalOpNodeJoin.getRightChild().acceptVisitor(this);
        logicalOpNodeJoin.getLeftChild().acceptVisitor(this);
        stack.push(new PhysicalOpHashNodeJoin(stack.pop(), stack.pop(), logicalOpNodeJoin));
    }

    @Override
    public void visit(final LogicalOpEdgeJoin logicalOpEdgeJoin) {

    }

    @Override
    public void visit(final LogicalOpNodeProduct logicalOpNodeProduct) {

    }

    @Override
    public void visit(final LogicalOpEdgeProduct logicalOpEdgeProduct) {

    }

    @Override
    public void visit(final LogicalOpUnion logicalOpUnion) {
        logicalOpUnion.getRightChild().acceptVisitor(this);
        logicalOpUnion.getLeftChild().acceptVisitor(this);
        stack.push(new PhysicalOpBinaryUnion(stack.pop(), stack.pop(), logicalOpUnion));
    }

    @Override
    public void visit(final LogicalOpIntersection logicalOpIntersection) {

    }

    @Override
    public void visit(final LogicalOpDifference logicalOpDifference) {

    }

    @Override
    public void visit(final LogicalOpAllPathsStartingFromNode logicalOpAllPathsStartingFromNode) {
        //since nullary operators don't have children (are leaves of the plan tree), we just push to the stack
        stack.push(new PhysicalOpBFSAllPathsFromNode(logicalOpAllPathsStartingFromNode));
    }

    @Override
    public void visit(final LogicalOpRecursive logicalOpRecursive) {
        if (logicalOpRecursive.hasFirstFilter()) {
            logicalOpRecursive.getLeftChild().acceptVisitor(this);
            logicalOpRecursive.getRightChild().acceptVisitor(this);
        } else {
            logicalOpRecursive.getRightChild().acceptVisitor(this);
            logicalOpRecursive.getLeftChild().acceptVisitor(this);
        }
        stack.push(new PhysicalOpRecursive(stack.pop(), stack.pop(), logicalOpRecursive));
    }

    @Override
    public void visit(final LogicalOpAllNodes logicalOpAllNodes) {
        //since nullary operators don't have children (are leaves of the plan tree), we just push to the stack
        stack.push(new PhysicalOpAllNodes(logicalOpAllNodes));
    }

    @Override
    public void visit(final LogicalOpAllEdges logicalOpAllEdges) {
        //since nullary operators don't have children (are leaves of the plan tree), we just push to the stack
        stack.push(new PhysicalOpAllEdges(logicalOpAllEdges));
    }

    @Override
    public void visit(final LogicalOpReverse logicalOpReverse) {
        logicalOpReverse.getChild().acceptVisitor(this);
        stack.push(new PhysicalOpReverse(stack.pop(), logicalOpReverse));
    }

    @Override
    public void visit(LogicalOpSelectionByLabel logicalOpSelectionByLabel) {
        stack.push(new PhysicalOpSelectionByLabel(logicalOpSelectionByLabel));
    }

    public PhysicalPlan getPhysicalPlan() {
        return stack::peek;
    }

}
