package com.gdblab.algorithm.versions.utils;

import com.gdblab.graph.schema.Path;

public interface Algorithm {
    public void execute();

    public void printPath(Path p);
    public void checkZeroPaths();

    public int getTotalPaths();
}
