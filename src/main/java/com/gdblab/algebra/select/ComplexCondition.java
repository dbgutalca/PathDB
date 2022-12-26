/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.algebra.select;

/**
 *
 * @author ramhg
 */
public class ComplexCondition {
    boolean value1;
    boolean value2;
    boolean isAnd;
    boolean result;

    public ComplexCondition(boolean value1, boolean value2, boolean isAnd) {
        this.value1 = value1;
        this.value2 = value2;
        this.isAnd = isAnd;
        if(!isAnd)
            result = value1 || value2;
        else
            result = value1 && value2;
    }
    
    
    public static boolean andCondition (boolean value1, boolean value2 ){
        return value1 && value2;
    }
    
    public static boolean orCondition (boolean value1, boolean value2 ){
        return value1 || value2;
    }
    
    public static boolean notCondition (boolean value1 ){
        return !value1;
    }
    

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean isValue1() {
        return value1;
    }

    public void setValue1(boolean value1) {
        this.value1 = value1;
    }

    public boolean isValue2() {
        return value2;
    }

    public void setValue2(boolean value2) {
        this.value2 = value2;
    }

    public boolean isIsAnd() {
        return isAnd;
    }

    public void setIsAnd(boolean isAnd) {
        this.isAnd = isAnd;
    }
    
    
}
   