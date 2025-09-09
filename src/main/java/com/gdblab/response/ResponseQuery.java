package com.gdblab.response;

import java.util.ArrayList;
import java.util.List;

public class ResponseQuery {
    
    private boolean success;
    private String message;
    private String time;
    private List<String> columnNames;
    private List<List<Object>> resultsContent;

    public ResponseQuery() {
        this.success = false;
        this.message = "";
        this.time = "";
        this.columnNames = new ArrayList<>();
        this.resultsContent = new ArrayList<>();
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public void addResultContent(ArrayList<Object> resultContent) {
        this.resultsContent.add(resultContent);
    }

     public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public List<List<Object>> getResultsContent() {
        return resultsContent;
    }

}
