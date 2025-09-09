package com.gdblab.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;

import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.GraphObject;
import com.gdblab.graph.schema.Node;
import com.gdblab.graph.schema.Path;

public final class Tools {

    public static String getConditional(String evaluation) {
        if (evaluation == null) return "null";
    
        String regex = "!=|<=|>=|<|>|=";
        Pattern pattern = java.util.regex.Pattern.compile(regex);
        Matcher matcher = pattern.matcher(evaluation);
    
        return matcher.find() ? matcher.group() : "null";
    }

    public static void resetContext() {
        Context.getInstance().setMaxPathsLength(10);
        Context.getInstance().setMaxRecursion(4);
        Context.getInstance().setTotalPathsObtained(0);
        Context.getInstance().setSemantic(2);
        Context.getInstance().setLimit(Integer.MAX_VALUE);

        Context.getInstance().setLeftVarName("");
        Context.getInstance().setRightVarName("");
        Context.getInstance().setPathsName("");
        Context.getInstance().setCondition(null);
        Context.getInstance().setRegularExpression(null);
        Context.getInstance().setCompleteQuery("");
        Context.getInstance().setReturnedVariables(new ArrayList<>());
    }

    @SuppressWarnings("unchecked")
    public static JSONObject nodeToJson(Node node) {
        JSONObject json = new JSONObject();
        json.put("id", node.getId());
        json.put("label", node.getLabel());
        JSONObject propertiesJson = new JSONObject();
        node.getProperties().forEach(propertiesJson::put);

        json.put("properties", propertiesJson);
        return json;
    }

    @SuppressWarnings("unchecked")
    public static JSONObject edgeToJson(Edge edge) {
        JSONObject json = new JSONObject();
        json.put("id", edge.getId());
        json.put("label", edge.getLabel());
        json.put("start", nodeToJson(edge.getSource()));
        json.put("end", nodeToJson(edge.getTarget()));
        return json;
    }

    @SuppressWarnings("unchecked")
    public static JSONObject pathToJson(Path path) {
        JSONObject json = new JSONObject();
        json.put("id", path.getId());
        json.put("label", path.getLabel());

        List<GraphObject> goList = path.getSequence();

        for (GraphObject go : goList) {
            if (go instanceof Node) {
                json.put("start", nodeToJson((Node) go));
            } else if (go instanceof Edge) {
                json.put("end", edgeToJson((Edge) go));
            }
        }

        return json;
    }
}
