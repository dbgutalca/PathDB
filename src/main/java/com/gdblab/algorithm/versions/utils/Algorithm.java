package com.gdblab.algorithm.versions.utils;

import com.gdblab.graph.schema.Path;

public interface Algorithm {
    public void Arbitrary();
    public void Trail();
    public void Simple();

    public void printPath(Path p);
    public void writePath(Path p);
    public void checkZeroPaths();

    public int getTotalPaths();
}
