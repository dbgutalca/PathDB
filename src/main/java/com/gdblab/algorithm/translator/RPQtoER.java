package com.gdblab.algorithm.translator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gdblab.algorithm.utils.LabelMap;

/**
 * This class is used to translate a RPQ to an ER.
 */
public class RPQtoER {
    
    /**
     * This method is used to translate a RPQ to an ER.
     * 
     * @param RPQ The RPQ to be translated.
     * @return The ER translated from the RPQ.
     */
    public static String Translate(String RPQ) {
        String er = mappingAscii(RPQ);
        er = addParentheses(er);
        er = deleteDots(er);
        er = changeOr(er);
        er = changeNegation(er);
        return er;
    }

    /**
     * This method replaces label names in a regular path query (RPQ) 
     * with their corresponding ASCII character mappings.
     * <p>
     * For example, the RPQ "knows.likes" will be mapped to "a.b".
     * </p>
     * 
     * <b>Example:</b>
     * <pre>
     * mappingAscii("knows.likes") -> "a.b"
     * </pre>
     * 
     * @param text the regular path query containing label names.
     *             For example: "knows.likes"
     * @return the RPQ with the labels replaced by ASCII characters.
     */
    private static String mappingAscii(String text) {
        Pattern p = Pattern.compile("([a-zA-Z0-9_]+)");
        Matcher m = p.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, LabelMap.getByLabel(m.group(1)));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * This method adds parentheses around each lowercase letter in a regular path query (RPQ).
     * <p>
     * For example, the RPQ "a.b" will be transformed to "(a).(b)".
     * </p>
     * 
     * <b>Example:</b>
     * <pre>
     * addParentheses("a.b") -> "(a).(b)"
     * </pre>
     * 
     * @param text the regular path query (RPQ) in which parentheses will be added around 
     *             each lowercase letter.
     *             For example: "a.b"
     * @return the RPQ with parentheses added around each lowercase letter.
     */
    private static String addParentheses(String text) {
        Pattern p = Pattern.compile("([a-z])");
        Matcher m = p.matcher(text);
        return m.replaceAll("($1)");
    }

    /**
     * This method removes the dots from a regular path query (RPQ).
     * <p>
     * Dots are typically used to concatenate labels in an RPQ, but in expression
     * representation (ER), concatenation is implicit and does not require dots.
     * This method removes the dots to make the RPQ compatible with ER.
     * </p>
     * 
     * <b>Example:</b>
     * <pre>
     * deleteDots("a.b.c") -> "abc"
     * </pre>
     * 
     * @param text the regular path query (RPQ) from which the dots will be removed.
     *             For example: "a.b.c"
     * @return the RPQ without dots. For example: "abc"
     */
    private static String deleteDots(String text) {
        Pattern p = Pattern.compile("\\.");
        Matcher m = p.matcher(text);
        return m.replaceAll("");
    }

    /**
     * This method adds parentheses around disjunctions (OR operations) in a regular path query (RPQ).
     * <p>
     * The method looks for expressions in the form of "a|b" and transforms them into "(a|b)"
     * to make the disjunction explicit.
     * </p>
     * 
     * <b>Example:</b>
     * <pre>
     * changeOr("(a)|(b)") -> "((a)|(b))"
     * </pre>
     * 
     * @param text the regular path query (RPQ) containing disjunctions (OR) between labels.
     *             For example: "(a)|(b)"
     * @return the RPQ with disjunctions wrapped in parentheses. For example: "((a)|(b))"
     */
    private static String changeOr(String text) {
        Pattern p = Pattern.compile("([a-z])[|]([a-z])");
        Matcher m = p.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "(" + m.group(1) + "|" + m.group(2) + ")");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * Converts negation expressions in a regular path query (RPQ) 
     * from the form "! (a)" to the form "[^a]".
     * <p>
     * This method identifies negation expressions that are expressed as 
     * "!( (a) )" and replaces them with the equivalent form "[^a]", 
     * making the negation explicit in a way that is compatible with 
     * regular expression syntax.
     * </p>
     * 
     * <b>Example:</b>
     * <pre>
     * changeNegation("!(a)") -> "[^a]"
     * </pre>
     * 
     * @param text the regular path query (RPQ) containing negation expressions to be converted.
     *             For example: "!(a)"
     * @return the RPQ with negation expressions converted to regular expression syntax. 
     *         For example: "[^a]"
     */
    private static String changeNegation(String text) {
        Pattern p = Pattern.compile("!([(][a-z][)])");
        Matcher m = p.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "[^" + m.group(1) + "]");
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
