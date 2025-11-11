package com.gdblab.algebra.queryplan.logical.visitor;

import java.util.Stack;

import com.gdblab.algebra.condition.First;
import com.gdblab.algebra.condition.Last;
import com.gdblab.algebra.queryplan.logical.LogicalOperator;
import com.gdblab.algebra.queryplan.logical.LogicalPlanVisitor;
import com.gdblab.algebra.queryplan.logical.impl.LogicalOpAllEdges;
import com.gdblab.algebra.queryplan.logical.impl.LogicalOpAllNodes;
import com.gdblab.algebra.queryplan.logical.impl.LogicalOpAllPathsStartingFromNode;
import com.gdblab.algebra.queryplan.logical.impl.LogicalOpDifference;
import com.gdblab.algebra.queryplan.logical.impl.LogicalOpEdgeJoin;
import com.gdblab.algebra.queryplan.logical.impl.LogicalOpEdgeProduct;
import com.gdblab.algebra.queryplan.logical.impl.LogicalOpIntersection;
import com.gdblab.algebra.queryplan.logical.impl.LogicalOpNodeJoin;
import com.gdblab.algebra.queryplan.logical.impl.LogicalOpNodeProduct;
import com.gdblab.algebra.queryplan.logical.impl.LogicalOpProjection;
import com.gdblab.algebra.queryplan.logical.impl.LogicalOpRecursive;
import com.gdblab.algebra.queryplan.logical.impl.LogicalOpReverse;
import com.gdblab.algebra.queryplan.logical.impl.LogicalOpSelection;
import com.gdblab.algebra.queryplan.logical.impl.LogicalOpSelectionByLabel;
import com.gdblab.algebra.queryplan.logical.impl.LogicalOpUnion;

public class PredicatePushdownLogicalPlanVisitor implements LogicalPlanVisitor {

    private final Stack<LogicalOperator> stack = new Stack<>();

    @Override
    public void visit(LogicalOpProjection logicalOpProjection) {
    }

    @Override
    public void visit(LogicalOpNodeJoin logicalOpNodeJoin) {
        if (!stack.isEmpty() && stack.peek() instanceof LogicalOpSelection) {
            LogicalOpSelection last = (LogicalOpSelection) stack.pop();
            if (last.getCondition() instanceof Last) {
                stack.push(new LogicalOpSelection(null, last.getCondition()));
                logicalOpNodeJoin.getRightChild().acceptVisitor(this);
                logicalOpNodeJoin.getLeftChild().acceptVisitor(this);
            } else if (last.getCondition() instanceof First) {
                logicalOpNodeJoin.getRightChild().acceptVisitor(this);
                stack.push(new LogicalOpSelection(null, last.getCondition()));
                logicalOpNodeJoin.getLeftChild().acceptVisitor(this);
            }

            // else if (last.getCondition() instanceof And) {
            //     Condition leftCond = ((And) last.getCondition()).getC1();
            //     Condition rightCond = ((And) last.getCondition()).getC2();
            //     if (leftCond instanceof First && rightCond instanceof First) {
            //         logicalOpNodeJoin.getRightChild().acceptVisitor(this);
            //         stack.push(new LogicalOpSelection(null, last.getCondition()));
            //         logicalOpNodeJoin.getLeftChild().acceptVisitor(this);
            //     } else if (leftCond instanceof Last && rightCond instanceof Last) {
            //         stack.push(new LogicalOpSelection(null, last.getCondition()));
            //         logicalOpNodeJoin.getRightChild().acceptVisitor(this);
            //         logicalOpNodeJoin.getLeftChild().acceptVisitor(this);
            //     } else {
            //         logicalOpNodeJoin.getRightChild().acceptVisitor(this);
            //         logicalOpNodeJoin.getLeftChild().acceptVisitor(this);
            //     }
            // }
        } else {
            logicalOpNodeJoin.getRightChild().acceptVisitor(this);
            logicalOpNodeJoin.getLeftChild().acceptVisitor(this);
        }
        stack.push(new LogicalOpNodeJoin(stack.pop(), stack.pop()));
    }

    @Override
    public void visit(LogicalOpEdgeJoin logicalOpEdgeJoin) {
    }

    @Override
    public void visit(LogicalOpNodeProduct logicalOpNodeProduct) {

    }

    @Override
    public void visit(LogicalOpEdgeProduct logicalOpEdgeProduct) {

    }

    @Override
    public void visit(LogicalOpUnion logicalOpUnion) {
        if (!stack.isEmpty() && stack.peek() instanceof LogicalOpSelection) {
            LogicalOpSelection lop = (LogicalOpSelection) stack.pop();
            stack.push(lop);
            logicalOpUnion.getRightChild().acceptVisitor(this);
            stack.push(lop);
            logicalOpUnion.getLeftChild().acceptVisitor(this);
        } else {
            logicalOpUnion.getRightChild().acceptVisitor(this);
            logicalOpUnion.getLeftChild().acceptVisitor(this);
        }
        stack.push(new LogicalOpUnion(stack.pop(), stack.pop()));
    }

    @Override
    public void visit(LogicalOpIntersection logicalOpIntersection) {

    }

    @Override
    public void visit(LogicalOpDifference logicalOpDifference) {

    }

    @Override
    public void visit(LogicalOpAllPathsStartingFromNode logicalOpAllPathsStartingFromNode) {

    }

    @Override
    public void visit(LogicalOpRecursive logicalOpRecursive) {
        LogicalOpSelection otherSelection = null;
        boolean isFirst = false;
        boolean isLast = false;

        if (!stack.isEmpty() && stack.peek() instanceof LogicalOpSelection lop) {
            if (lop.getCondition() instanceof First) {
                isFirst = true;
            } else if (lop.getCondition() instanceof Last) {
                isLast = true;
            }

            if (lop.getCondition() instanceof First || lop.getCondition() instanceof Last) {
                logicalOpRecursive.setFilters(isLast, isFirst);
            } else {
                otherSelection = (LogicalOpSelection) stack.pop();
            }
        }

        if (logicalOpRecursive.hasFirstFilter()) {
            // izquierdo esta filtrado y el derecho no, entonces el conjunto completo tiene que estar en el hijo derecho
            logicalOpRecursive.getLeftChild().acceptVisitor(this);
            logicalOpRecursive.setFilters(false, false);
            logicalOpRecursive.getRightChild().acceptVisitor(this);
        } else {
            // derecho esta filtrado y el izquierdo no, entonces el conjunto completo tiene que estar en el hijo izquierdo
            logicalOpRecursive.getRightChild().acceptVisitor(this);
            logicalOpRecursive.setFilters(false, false);
            logicalOpRecursive.getLeftChild().acceptVisitor(this);
        }

        // volver al estado en el que estaba para saber en el operador fisico recursivo
        logicalOpRecursive.setFilters(isLast, isFirst);

        if (otherSelection != null) {
            stack.push(new LogicalOpSelection(logicalOpRecursive, otherSelection.getCondition()));
        } else {
            stack.push(new LogicalOpRecursive(stack.pop(), stack.pop(), logicalOpRecursive.hasLastFilter(), logicalOpRecursive.hasFirstFilter()));
        }
    }

    @Override
    public void visit(LogicalOpAllNodes logicalOpAllNodes) {
        if (!stack.isEmpty() && stack.peek() instanceof LogicalOpSelection) {
            LogicalOpSelection selection = (LogicalOpSelection) stack.pop();
            if (selection.getChild() != null) {
                stack.push(selection);
            } else if (selection.getCondition() instanceof Last || selection.getCondition() instanceof First) {
                stack.push(new LogicalOpSelection(logicalOpAllNodes, selection.getCondition()));
                return;
            }
        }
        stack.push(logicalOpAllNodes);
    }

    @Override
    public void visit(LogicalOpAllEdges logicalOpAllEdges) {
        if (!stack.isEmpty() && stack.peek() instanceof LogicalOpSelection) {
            LogicalOpSelection selection = (LogicalOpSelection) stack.pop();
            if (selection.getChild() != null) {
                stack.push(selection);
            } else if (selection.getCondition() instanceof Last || selection.getCondition() instanceof First) {
                stack.push(new LogicalOpSelection(logicalOpAllEdges, selection.getCondition()));
                return;
            }
        }
        stack.push(logicalOpAllEdges);
    }

    @Override
    public void visit(LogicalOpSelection logicalOpSelection) {
        if (logicalOpSelection.getCondition() instanceof First || logicalOpSelection.getCondition() instanceof Last) {
            stack.push(new LogicalOpSelection(null, logicalOpSelection.getCondition()));
            logicalOpSelection.getChild().acceptVisitor(this);
        } else {
            logicalOpSelection.getChild().acceptVisitor(this);
            stack.push(new LogicalOpSelection(stack.pop(), logicalOpSelection.getCondition()));
        }
    }

    @Override
    public void visit(LogicalOpSelectionByLabel logicalOpSelectionByLabel) {
        if (!stack.isEmpty() && stack.peek() instanceof LogicalOpSelection) {
            LogicalOpSelection selection = (LogicalOpSelection) stack.pop();
            if (selection.getChild() != null) {
                stack.push(selection);
            } else if (selection.getCondition() instanceof Last || selection.getCondition() instanceof First) {
                stack.push(new LogicalOpSelection(logicalOpSelectionByLabel, selection.getCondition()));
                return;
            }
        }
        stack.push(logicalOpSelectionByLabel);
    }

    @Override
    public void visit(LogicalOpReverse logicalOpReverse) {

    }

    public LogicalOperator getRoot() {
        return stack.peek();
    }
}
