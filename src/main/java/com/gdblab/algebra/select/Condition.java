/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.algebra.select;

/**
 *
 * @author ramhg
 */
public class Condition {
    boolean value;
    boolean isAnd;
    boolean isNot;
    Condition next;

    public Condition(boolean value) {
        this.value = value;
        this.isAnd = false;
        this.isNot = false;
        this. next = null;
    }

    public Condition(boolean value, boolean orAnd, Condition next) {
        this.value = value;
        this.isAnd = orAnd;
        this.isNot = false;
        this.next = next;
    }

    public Condition(boolean value, boolean isNot) {
        this.value = value;
        this.isNot = isNot;
        this.isAnd = false;
        this. next = null;
    }

    public Condition(boolean value, boolean isAnd, boolean isNot, Condition next) {
        this.value = value;
        this.isAnd = isAnd;
        this.isNot = isNot;
        this.next = next;
    }

    public boolean getIsNot() {
        return isNot;
    }

    public void setIsNot(boolean isNot) {
        this.isNot = isNot;
    }
    
    
    public boolean getValue() {
        if(isNot)
            return !value;
        else
            return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean getIsAnd() {
        return isAnd;
    }

    public void setIsAnd(boolean isAnd) {
        this.isAnd = isAnd;
    }

    public Condition getNext() {
        return next;
    }

    public void setNext(Condition next) {
        this.next = next;
    }
    
    
    
}
