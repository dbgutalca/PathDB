package com.gdblab.queryplan.logical.visitor;

import com.gdblab.queryplan.logical.LogicalPlanVisitor;
import com.gdblab.queryplan.logical.impl.*;
import com.gdblab.queryplan.physical.PhysicalOperator;
import com.gdblab.queryplan.physical.PhysicalPlan;
import com.gdblab.queryplan.physical.impl.PhysicalOpAllEdges;
import com.gdblab.queryplan.physical.impl.PhysicalOpAllNodes;
import com.gdblab.queryplan.physical.impl.PhysicalOpBFSAllPathsFromNode;
import com.gdblab.queryplan.physical.impl.PhysicalOpBinaryUnion;
import com.gdblab.queryplan.physical.impl.PhysicalOpNestedLoopNodeJoin;
import com.gdblab.queryplan.physical.impl.PhysicalOpRecursive;
import com.gdblab.queryplan.physical.impl.PhysicalOpSequentialScan;

import java.util.Stack;

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
        stack.push(new PhysicalOpNestedLoopNodeJoin(stack.pop(), stack.pop(), logicalOpNodeJoin));
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
        logicalOpRecursive.getChild().acceptVisitor(this);
        //TODO Link with Physical operator
        stack.push(new PhysicalOpRecursive(stack.pop(), logicalOpRecursive));
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

    public PhysicalPlan getPhysicalPlan() {
        return stack::peek;
    }

}
