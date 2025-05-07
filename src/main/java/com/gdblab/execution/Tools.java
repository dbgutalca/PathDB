package com.gdblab.execution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;

import java.util.regex.Matcher;

import com.gdblab.graph.DefaultGraph2;
import com.gdblab.graph.Graph;
import com.gdblab.graph.interfaces.InterfaceGraph;
import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.GraphObject;
import com.gdblab.graph.schema.Node;
import com.gdblab.graph.schema.Path;

public final class Tools {

    public static void clearConsole() {
        try {
            String os = System.getProperty("os.name");
    
            if (os.contains("Windows")) {
                // Ejecutar comando cls en Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Secuencia de escape ANSI para limpiar la consola en Linux/Unix/MacOS
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
            initMessage();
        } catch (Exception e) {}
    }

    public static void initMessage() {
        System.out.println();
        String[] logo = {
        "       ╔═════════════════════════════════════════════════════╗",
        "       ║    _____            _     _       _____    ____     ║",
        "       ║   |  __ \\          | |   | |     |  __ \\  |  _ \\    ║",
        "       ║   | |__) |   __ _  | |_  | |__   | |  | | | |_) |   ║",
        "       ║   |  ___/   / _` | | __| | '_ \\  | |  | | |  _ <    ║",
        "       ║   | |      | (_| | | |_  | | | | | |__| | | |_) |   ║",
        "       ║   |_|       \\__,_|  \\__| |_| |_| |_____/  |____/    ║",
        "       ║                                                v1.0 ║",
        "       ╚═════════════════════════════════════════════════════╝"
        };

        for (String u : logo) {
            System.out.println(u);
        }
        System.out.println("\n");
    }

    public static void loadCustomGraphFiles(String nodesFile, String edgesFile) {

        try (BufferedReader br = new BufferedReader(new FileReader(nodesFile))) {
            String line;

            ArrayList<String> schemaNode = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                
                if (line.startsWith("@")) {
                    schemaNode = new ArrayList<>(Arrays.asList(line.split("\\|")));
                }

                else {
                    ArrayList<String> data = new ArrayList<>(Arrays.asList(line.split("\\|")));
                    
                    HashMap<String, String> properties = new HashMap<>();

                    for (int i = 2; i < data.size() && i < schemaNode.size(); i++) {
                        properties.put(schemaNode.get(i), data.get(i));
                    }

                    Node node = new Node(
                        data.get(0),
                        data.get(1),
                        properties
                    );

                    Graph.getGraph().insertNode(node);
                }

            }
        } catch (Exception e) {
            Tools.clearConsole();
            Tools.showUsageArgsErrorLoadingGraph(nodesFile, edgesFile);
            Tools.loadDefaultGraph();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(edgesFile))) {
            String line;

            ArrayList<String> schemaEdge = new ArrayList<>();
            while ((line = br.readLine()) != null) {

                    if (line.startsWith("@")) {
                        schemaEdge = new ArrayList<>(Arrays.asList(line.split("\\|")));
                    }
                    else {
                        ArrayList<String> data = new ArrayList<>(Arrays.asList(line.split("\\|")));
                        HashMap<String, String> properties = new HashMap<>();

                        for (int j = 4; j < data.size() && j < schemaEdge.size(); j++) {
                            properties.put(schemaEdge.get(j), data.get(j));
                        }

                        Edge e = new Edge(
                            data.get(0),
                            data.get(1),
                            Graph.getGraph().getNode(data.get(2)),
                            Graph.getGraph().getNode(data.get(3)),
                            properties
                        );

                        Graph.getGraph().insertEdge(e);
                    }
                }

            Tools.clearConsole();
            Tools.showUsageArgsLoadedCustomGraph(nodesFile, edgesFile);
        } catch (Exception e) {
            Tools.clearConsole();
            Tools.showUsageArgsErrorLoadingGraph(nodesFile, edgesFile);
            Graph.getGraph().cleanNodes();
            Tools.loadDefaultGraph();
            return;
        }




    }

    public static void loadDefaultGraph() {
        InterfaceGraph graph = Graph.getGraph();

        Node[] nodes = DefaultGraph2.getDefaultNodes();
        for (Node n : nodes) { graph.insertNode(n); }

        Edge[] edges = DefaultGraph2.getDefaultEdges();
        for (Edge e : edges) {
            graph.insertEdge(e);
        }
    }

    public static void showUsageNoArgs() {
        String[] usage = {
            "Welcome to PathDB! A tool to evaluate Regular Path Queries in Property Graphs.",
            "",
            "No graph loaded. Using default graph.",
            "If you want to use a custom graph, run the program with the following arguments: ",
            "java -jar PathDB.jar -n nodes_file.txt -e edges_file.txt",
            "",
            "For help, type /h.",
            ""
        };

        for (String u : usage) {
            System.out.println(u);
        }
    }

    public static void showUsageArgsLoadingCustomGraph(String nf, String ef) {
        String[] usage = {
            "Welcome to PathDB! A tool to evaluate Regular Path Queries in Property Graphs.",
            "",
            "Uploading custom graph to PathDB...",
            "",
            "Using " + nf + " and " + ef + " files.",
            ""
        };

        for (String u : usage) {
            System.out.println(u);
        }
    }

    public static void showUsageArgsLoadedCustomGraph(String nf, String ef) {
        String[] usage = {
            "Welcome to PathDB! A tool to evaluate Regular Path Queries in Property Graphs.",
            "",
            "Graph loaded successfully. Using " + nf + " and " + ef + " files.",
            "",
            "For help, type /h.",
            ""
        };

        for (String u : usage) {
            System.out.println(u);
        }
    }

    public static void showUsageArgsErrorLoadingGraph(String nf, String ef) {
        String[] usage = {
            "Welcome to PathDB! A tool to evaluate Regular Path Queries in Property Graphs.",
            "",
            "An error occurred while trying to load the database. Loading default graph instead.",
            "",
            "For help, type /h.",
            ""
        };

        for (String u : usage) {
            System.out.println(u);
        }
    }

    public static void showHelp() {
        String[] help = {
            "List of available commands:",
            "   /h, /help                           Show this help.",
            "   /ml <#>, /max_length <#>            Set the fix point of max path length (Default is 10).",
            "   /mr, /max_recursion <#>             Set the max recursion depth (Default is 6).",
            "   /pts <#>, </paths_to_show> <#>      Set the number of paths to show (Default is 10).",
            "   /in, /information                   Show the information of the graph.",
            "   /la, /labels                        Show a sample of each label in the graph.",
            "   /q, /quit                           Quit the program."
        };
        for (String u : help) {
            System.out.println(u);
        }
        System.out.println();
    }

    public static ArrayList<String> readRPQsFromFile(String rpqs_file) {
        ArrayList<String> rpqs = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rpqs_file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) continue;
                rpqs.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rpqs;
    }
 
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
        Context.getInstance().setTotalPathsToShow(Integer.MAX_VALUE);

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
