package com.gdblab.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RPQtoER {

    private String rpq;

    public RPQtoER(String rpq) {
        this.rpq = rpq;
    }

    public String Convert() {
        this.rpq = addParenthesis(this.rpq);
        this.rpq = this.rpq.replace(".", "");
        
        return rpq;
    }

    private String addParenthesis(String rpq) {
        // Define el patrón para las etiquetas (letras, números y guion bajo)
        Pattern pattern = Pattern.compile("\\b[a-zA-Z0-9_]+\\b");
        Matcher matcher = pattern.matcher(rpq);

        // StringBuffer para construir el resultado
        StringBuffer result = new StringBuffer();

        // Encuentra todas las coincidencias y añade paréntesis
        while (matcher.find()) {
            matcher.appendReplacement(result, "(" + matcher.group() + ")");
        }
        matcher.appendTail(result);

        return result.toString();
    }
}
