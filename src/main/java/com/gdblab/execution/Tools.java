package com.gdblab.execution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.gdblab.graph.DefaultGraph;
import com.gdblab.graph.Graph;
import com.gdblab.graph.interfaces.InterfaceGraph;

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
        } catch (Exception e) {
        }
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
                    schemaNode.get(0).replaceAll("@", "");
                    schemaNode.get(1).replaceAll("@", "");
                }

                else {
                    ArrayList<String> data = new ArrayList<>(Arrays.asList(line.split("\\|")));

                    String id = data.get(0);
                    String node = "";

                    for (int i = 0; i < schemaNode.size(); i++) {
                        node = node + schemaNode.get(i) + ":" + data.get(i) + "|";
                    }

                    node.substring(0, node.length() - 1);
                    Graph.getGraph().insertNode(id, node);
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
                    schemaEdge.get(0).replaceAll("@", "");
                    schemaEdge.get(1).replaceAll("@", "");
                    schemaEdge.get(2).replaceAll("@", "");
                    schemaEdge.get(3).replaceAll("@", "");
                    schemaEdge.get(4).replaceAll("@", "");
                } else {
                    ArrayList<String> data = new ArrayList<>(Arrays.asList(line.split("\\|")));
                    String edgeLabel = data.get(1);
                    String edge = "";

                    for (int j = 0; j < data.size() && j < schemaEdge.size(); j++) {
                        edge += schemaEdge.get(j) + ":" + data.get(j) + "|";
                    }

                    Graph.getGraph().insertEdge(edgeLabel, edge);
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

        String[] nodes = DefaultGraph.getDefaultStringNodes();
        for (String n : nodes) {
            String[] nodeData = n.split("\\|");
            graph.insertNode(nodeData[0].split(":")[1], n);
        }

        String[] edges = DefaultGraph.getDefaultStringEdges();
        for (String e : edges) {
            String[] edgeData = e.split("\\|");
            graph.insertEdge(edgeData[1].split(":")[1], e);
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
                if (line.startsWith("#"))
                    continue;
                rpqs.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rpqs;
    }

    public static String getConditional(String evaluation) {
        if (evaluation == null)
            return "null";

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
        // Context.getInstance().setTotalPathsToShow(Integer.MAX_VALUE);

        Context.getInstance().setLeftVarName("");
        Context.getInstance().setRightVarName("");
        Context.getInstance().setPathsName("");
        Context.getInstance().setCondition(null);
        Context.getInstance().setRegularExpression(null);
        Context.getInstance().setCompleteQuery("");
        Context.getInstance().setReturnedVariables(new ArrayList<>());
    }

    public static String getIdOfNode(String node) {
        String[] nodeDataParts = node.split("\\|");
        return nodeDataParts[0].split(":")[1];
    }

    public static String getLabelOfNode(String node) {
        String[] nodeDataParts = node.split("\\|");
        return nodeDataParts[1].split(":")[1];
    }

    public static String getIdOfEdge(String edge) {
        String[] edgeDataParts = edge.split("\\|");
        return edgeDataParts[0].split(":")[1];
    }

    public static String getLabelOfEdge(String edge) {
        String[] edgeDataParts = edge.split("\\|");
        return edgeDataParts[1].split(":")[1];
    }

    public static String getSourceIdOfEdge(String edge) {
        String[] edgeDataParts = edge.split("\\|");
        return edgeDataParts[2].split(":")[1];
    }

    public static String getTargetIdOfEdge(String edge) {
        String[] edgeDataParts = edge.split("\\|");
        return edgeDataParts[3].split(":")[1];
    }

}
