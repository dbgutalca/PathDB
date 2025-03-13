package com.gdblab.queryplan.physical.impl;

import com.gdblab.algebra.queryplan.physical.PhysicalOperator;
import com.gdblab.algebra.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpHashNodeJoinRight;
import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.Node;
import com.gdblab.graph.schema.Path;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class NodeHashJoinRightTest {

    private PhysicalOperator leftChild;
    private PhysicalOperator rightChild;

    @Before
    public void setup() {
        leftChild = new MockPhysicalOperator(Arrays.asList(
                new Path("x", List.of(new Edge("e1", new Node("A"), new Node("B")))),
                new Path("y", List.of(new Edge("e2", new Node("B"), new Node("C"))))
        ));

        rightChild = new MockPhysicalOperator(Arrays.asList(
                new Path("z", List.of(new Edge("e3", new Node("C"), new Node("E")))),
                new Path("w", List.of(new Edge("e4", new Node("B"), new Node("E"))))
        ));
    }

    @Test
    public void testSimpleJoin() {
        PhysicalOpHashNodeJoinRight joinOp = new PhysicalOpHashNodeJoinRight(leftChild, rightChild);

        assertTrue(joinOp.hasNext());
        Path result1 = joinOp.next();
        assertEquals(new Path("t", List.of(new Edge("e1", new Node("A"), new Node("B")),
                        new Edge("e4", new Node("B"), new Node("E"))))
                , result1);

        assertTrue(joinOp.hasNext());
        Path result2 = joinOp.next();
        assertEquals(new Path("s", List.of(new Edge("e2", new Node("B"), new Node("C")),
                        new Edge("e3", new Node("C"), new Node("E")))),
                result2);

        assertFalse(joinOp.hasNext());
    }

    @Test
    public void testNoMatches() {
        leftChild = new MockPhysicalOperator(List.of(
                new Path("t", List.of(new Edge("e6", new Node("X"), new Node("Y"))))
        ));

        rightChild = new MockPhysicalOperator(List.of(
                new Path("s", List.of(new Edge("e7", new Node("Z"), new Node("W"))))
        ));

        PhysicalOpHashNodeJoinRight joinOp = new PhysicalOpHashNodeJoinRight(leftChild, rightChild);
        assertFalse(joinOp.hasNext());
    }

    @Test
    public void testMultipleMatches() {
        leftChild = new MockPhysicalOperator(List.of(
                new Path("v", List.of(new Edge("e8", new Node("A"), new Node("B"))))
        ));

        rightChild = new MockPhysicalOperator(Arrays.asList(
                new Path("w", List.of(new Edge("e9", new Node("B"), new Node("C")))),
                new Path("u", List.of(new Edge("e10", new Node("B"), new Node("D"))))
        ));

        PhysicalOpHashNodeJoinRight joinOp = new PhysicalOpHashNodeJoinRight(leftChild, rightChild);

        assertTrue(joinOp.hasNext());
        Path result1 = joinOp.next();
        assertEquals(new Path("t", List.of(new Edge("e8", new Node("A"), new Node("B")),
                new Edge("e9", new Node("B"), new Node("C")))), result1);

        assertTrue(joinOp.hasNext());
        Path result2 = joinOp.next();
        assertEquals(new Path("s", List.of(new Edge("e8", new Node("A"), new Node("B")),
                new Edge("e10", new Node("B"), new Node("D")))), result2);

        assertFalse(joinOp.hasNext());
    }

    @Test
    public void testEmptyInput() {
        leftChild = new MockPhysicalOperator(List.of());
        rightChild = new MockPhysicalOperator(List.of());

        PhysicalOpHashNodeJoinRight joinOp = new PhysicalOpHashNodeJoinRight(leftChild, rightChild);
        assertFalse(joinOp.hasNext());
    }

    // Mock implementation of PhysicalOperator for testing
    private static class MockPhysicalOperator implements PhysicalOperator {
        private final Iterator<Path> iterator;

        public MockPhysicalOperator(List<Path> paths) {
            this.iterator = paths.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Path next() {
            return iterator.next();
        }

        @Override
        public void acceptVisitor(PhysicalPlanVisitor visitor) {

        }
    }
}
