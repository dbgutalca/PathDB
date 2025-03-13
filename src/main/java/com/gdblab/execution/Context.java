package com.gdblab.execution;

import java.util.concurrent.TimeUnit;

public final class Context {
    private static Context INSTANCE;

    private Integer method;
    private Integer fixPoint;
    private Integer maxRecursion;
    private String startNode;
    private String endNode;
    private String rpq;
    private String rpqFileName;
    private String time;
    private Integer number;
    private Integer totalPaths;
    private boolean isExperimental;
    private String resultFilename;
    private boolean optimize;
    private Integer semantic;
    private Integer timeoutDuration;
    private TimeUnit timeoutTimeUnit;
    private Integer showPaths;

    private boolean activateMaxRecursion;
    private boolean activateFixpoint;
    private Integer maxPaths;

    private Context() {
        method = 1;
        fixPoint = 10;
        maxRecursion = 4;
        startNode = "";
        endNode = "";
        rpq = "";
        rpqFileName = "";
        time = "";
        number = 0;
        totalPaths = 0;
        isExperimental = false;
        resultFilename = "";
        optimize = true;
        semantic = 2;
        timeoutDuration = 2;
        timeoutTimeUnit = TimeUnit.MINUTES;
        showPaths = 10;

        activateMaxRecursion = false;
        activateFixpoint = false;
        maxPaths = Integer.MAX_VALUE;
    }

    public static Context getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Context();
        }

        return INSTANCE;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public Integer getMethod() {
        return method;
    }

    public void setFixPoint(Integer fixPoint) {
        this.fixPoint = fixPoint;
    }

    public Integer getFixPoint() {
        return fixPoint;
    }

    public void setMaxRecursion(Integer maxRecursion) {
        this.maxRecursion = maxRecursion;
    }

    public Integer getMaxRecursion() {
        return maxRecursion;
    }

    public void setStartNode(String startNode) {
        this.startNode = startNode;
    }

    public String getStartNode() {
        return startNode;
    }

    public void setEndNode(String endNode) {
        this.endNode = endNode;
    }

    public String getEndNode() {
        return endNode;
    }

    public void setRPQ(String rpq) {
        this.rpq = rpq;
    }

    public String getRPQ() {
        return rpq;
    }

    public void setRPQFileName(String rpqFileName) {
        this.rpqFileName = rpqFileName;
    }

    public String getRPQFileName() {
        return rpqFileName;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public void setTotalPaths(Integer totalPaths) {
        this.totalPaths = totalPaths;
    }

    public Integer getTotalPaths() {
        return totalPaths;
    }

    public void setExperimental(boolean isExperimental) {
        this.isExperimental = isExperimental;
    }

    public boolean isExperimental() {
        return isExperimental;
    }

    public void setResultFilename(String resultFilename) {
        this.resultFilename = resultFilename;
    }

    public String getResultFilename() {
        return resultFilename;
    }

    public void setOptimize(boolean optimize) {
        this.optimize = optimize;
    }

    public boolean isOptimize() {
        return optimize;
    }

    public void setSemantic(Integer semantic) {
        this.semantic = semantic;
    }

    public Integer getSemantic() {
        return semantic;
    }

    public void setTimeoutDuration(Integer timeoutDuration) {
        this.timeoutDuration = timeoutDuration;
    }

    public Integer getTimeoutDuration() {
        return timeoutDuration;
    }

    public void setTimeoutTimeUnit(TimeUnit timeoutTimeUnit) {
        this.timeoutTimeUnit = timeoutTimeUnit;
    }

    public TimeUnit getTimeoutTimeUnit() {
        return timeoutTimeUnit;
    }

    public void setShowPaths(Integer showPaths) {
        this.showPaths = showPaths;
    }

    public Integer getShowPaths() {
        return showPaths;
    }

    public void setActivateMaxRecursion(boolean activateMaxRecursion) {
        this.activateMaxRecursion = activateMaxRecursion;
    }

    public boolean isActivateMaxRecursion() {
        return activateMaxRecursion;
    }

    public void setActivateFixpoint(boolean activateFixpoint) {
        this.activateFixpoint = activateFixpoint;
    }

    public boolean isActivateFixpoint() {
        return activateFixpoint;
    }

    public void setMaxPaths(Integer maxPaths) {
        this.maxPaths = maxPaths;
    }

    public Integer getMaxPaths() {
        return maxPaths;
    }
}
