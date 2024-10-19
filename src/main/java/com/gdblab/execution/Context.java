package com.gdblab.execution;

public final class Context {
    private static Context INSTANCE;

    private Integer method;
    private Integer fixPoint;
    private String startNode;
    private String endNode;
    private String rpq;
    private String rpqFileName;
    private String time;
    private Integer number;
    private Integer totalPaths;
    private boolean isExperimental;
    private String resultFilename;

    private Context() {
        method = 1;
        fixPoint = 3;
        startNode = "";
        endNode = "";
        rpq = "";
        rpqFileName = "";
        time = "";
        number = 0;
        totalPaths = 0;
        isExperimental = false;
        resultFilename = "";
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

    public boolean optimize() {
        return false; // TODO: dejar como parámetro
    }
}
