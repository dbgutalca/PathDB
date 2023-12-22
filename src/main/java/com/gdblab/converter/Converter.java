package com.gdblab.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter {

    public Converter() {}

    public String RPQtoER (String rpq) {
        String er = "";
        er = changeOr(rpq);
        // er = changeNegation(er);
        er = addParentheses(er);
        er = deleteDots(er);
        
        return er;
    }

    private String addParentheses(String text) {
        Pattern p = Pattern.compile("([a-zA-Z0-9_]+)");
        Matcher m = p.matcher(text);
        return m.replaceAll("($1)");
    }

    private String changeOr(String text) {
        Pattern p = Pattern.compile("([a-zA-Z0-9_]+)[|]([a-zA-Z0-9_]+)");
        Matcher m = p.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "(" + m.group(1) + "|" + m.group(2) + ")");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private String changeNegation(String text) {
        Pattern p = Pattern.compile("![(]([a-zA-Z0-9_]+)[)]");
        Matcher m = p.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "(?!(" + m.group(1) + ")$)");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private String deleteDots(String text) {
        Pattern p = Pattern.compile("\\.");
        Matcher m = p.matcher(text);
        return m.replaceAll("");
    }
}
