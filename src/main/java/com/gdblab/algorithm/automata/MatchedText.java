package com.gdblab.algorithm.automata;

/**
 * Created on 5/25/15.
 */
public class MatchedText {
    private String text;
    private int Pos;

    MatchedText(String text, int Pos) {
        this.text = text;
        this.Pos = Pos;
    }

    public String getText() {
        return text;
    }

    public int getPos() {
        return Pos;
    }

    @Override
    public String toString() {
        return "MatchedText{" +
                "text='" + text + '\'' +
                ", Pos=" + Pos +
                '}';
    }
}
