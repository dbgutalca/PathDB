package com.gdblab.execution;

public final class Context {
    private static Context INSTANCE;

    private Integer method;
    private Integer fixPoint;
    private String startNode;
    private String endNode;

    private Context() {
        method = 1;
        fixPoint = 3;
        startNode = "";
        endNode = "";
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

}
