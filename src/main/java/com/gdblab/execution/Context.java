package com.gdblab.execution;

import java.util.ArrayList;

import com.gdblab.algebra.condition.Condition;
import com.gdblab.algebra.parser.RPQExpression;
import com.gdblab.algebra.returncontent.ReturnContent;

public final class Context {
    private static Context INSTANCE;

    private Integer maxPathLength;
    private Integer maxRecursion;
    private Integer totalPathsObtained;
    private Integer semantic;
    private Integer limit;
    private Integer totalPathsToShow;

    private String leftVarName;
    private String rightVarName;
    private String pathsName;
    private Condition condition;
    private RPQExpression regularExpression;
    private String completeQuery;
    private ArrayList<ReturnContent> returnedVariables;

    private Context() {
        maxPathLength = 10;
        maxRecursion = 4;
        totalPathsObtained = 0;
        semantic = 2; // 1 -> Walk, 2 -> Trail, 3 -> Simple Path, 4 -> Acyclic
        limit = 100;
        totalPathsToShow = Integer.MAX_VALUE;

        leftVarName = "";
        rightVarName = "";
        pathsName = "";
        condition = null;
        regularExpression = null;
        completeQuery = "";
        returnedVariables = new ArrayList<>();
    }

    public static Context getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Context();
        }

        return INSTANCE;
    }

    public void setMaxPathsLength(Integer maxPathLength) {
        this.maxPathLength = maxPathLength;
    }

    public Integer getMaxPathsLength() {
        return maxPathLength;
    }

    public void setMaxRecursion(Integer maxRecursion) {
        this.maxRecursion = maxRecursion;
    }

    public Integer getMaxRecursion() {
        return maxRecursion;
    }

    public void setTotalPathsObtained(Integer totalPathsObtained) {
        this.totalPathsObtained = totalPathsObtained;
    }

    public Integer getTotalPathsObtained() {
        return totalPathsObtained;
    }

    public void setSemantic(Integer semantic) {
        this.semantic = semantic;
    }

    public Integer getSemantic() {
        return semantic;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setTotalPathsToShow(Integer totalPathsToShow) {
        this.totalPathsToShow = totalPathsToShow;
    }

    public Integer getTotalPathsToShow() {
        return totalPathsToShow;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setLeftVarName(String leftVarName) {
        this.leftVarName = leftVarName;
    }

    public String getLeftVarName() {
        return leftVarName;
    }

    public void setRightVarName(String rightVarName) {
        this.rightVarName = rightVarName;
    }

    public String getRightVarName() {
        return rightVarName;
    }

    public void setPathsName(String pathsName) {
        this.pathsName = pathsName;
    }

    public String getPathsName() {
        return pathsName;
    }

    public void setRegularExpression(RPQExpression regularExpression) {
        this.regularExpression = regularExpression;
    }

    public RPQExpression getRegularExpression() {
        return regularExpression;
    }

    public void setCompleteQuery(String completeQuery) {
        this.completeQuery = completeQuery;
    }

    public String getCompleteQuery() {
        return completeQuery;
    }

    public void setReturnedVariables(ArrayList<ReturnContent> returnedVariables) {
        this.returnedVariables = returnedVariables;
    }

    public ArrayList<ReturnContent> getReturnedVariables() {
        return returnedVariables;
    }
    
    public void clearReturnedVariables() {
        this.returnedVariables.clear();
    }
}
