package com.gdblab.response;

import java.util.HashMap;

public class ResponseDatabase {
    
    private boolean success;
    private String message;
    private HashMap<String, Object> data;

    public ResponseDatabase(boolean success, String message, HashMap<String, Object> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }



}
