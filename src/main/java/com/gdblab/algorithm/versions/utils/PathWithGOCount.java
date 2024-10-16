package com.gdblab.algorithm.versions.utils;

import java.util.Map;

import com.gdblab.graph.schema.Path;

public class PathWithGOCount {
    Path path;
    Map<String, Integer> visitedGOCount;

    public PathWithGOCount(Path path, Map<String, Integer> visitedGOCount) {
        this.path = path;
        this.visitedGOCount = visitedGOCount;
    }

    public Path getPath() {
        return path;
    }

    public Map<String, Integer> getVisitedGOCount() {
        return visitedGOCount;
    }
    
}
