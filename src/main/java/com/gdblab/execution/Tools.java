package com.gdblab.execution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.gdblab.algorithm.utils.LabelMap;
import com.gdblab.graph.DefaultGraph2;
import com.gdblab.graph.Graph;
import com.gdblab.graph.interfaces.InterfaceGraph;
import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.Node;

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
            int i = 1;
            String line;
            while ((line = br.readLine()) != null) {
                    if (line.startsWith("@")) continue;

                    String[] data = line.split("\\|");
                    String _id_ = "E" + i++;
                    String _label_ = data[0];
                    String _from_ = data[2];
                    String _to_ = data[3];

                    Edge e = new Edge(
                        _id_,
                        _label_,
                        Graph.getGraph().getNode(_from_),
                        Graph.getGraph().getNode(_to_)
                    );

                    Graph.getGraph().insertEdge(e);
                
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

            if (!LabelMap.containsKey(e.getLabel())) LabelMap.put(e.getLabel());
            
            graph.insertEdge(e);
        }
    }

    public static void showUsageNoArgs() {
        String[] usage = {
            "Welcome to PathDB! A tool to evaluate Regular Path Queries in Graphs.",
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
            "Welcome to PathDB! A tool to evaluate Regular Path Queries in Graphs.",
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
            "Welcome to PathDB! A tool to evaluate Regular Path Queries in Graphs.",
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
            "Welcome to PathDB! A tool to evaluate Regular Path Queries in Graphs.",
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
            "   /h, /help                                           Show this help.",
            // "   /m <1-5>        Select evaluation method.",
            // "                       1 - Algebra (Default).",
            // "                       2 - Regex + DFS.",
            // "                       3 - Regex + BFS.",
            // "                       4 - Automaton + DFS.",
            // "                       5 - Automaton + BFS.",
            "   /ml <#>, /max_length <#>                            Set the fix point of max path length (Default is 10).",
            "   /mr, /max_recursion <#>                             Set the max recursion depth (Default is 6).",
            "   /sem <1-3> or /semantic <1-3>                       Select evaluation semantics.",
            "                                                           1 - Arbitrary.",
            "                                                           2 - Trail (Default).",
            "                                                           3 - Simple Path.",
            "   (X{prop:value})-[RE]->(Y);                          Query to evaluate.",
            "                                                           X{prop:value} = The source node filtered by a property.",
            "                                                           RE = Regular Expression to evaluate.",
            "                                                           Y{prop:value} = The target node filtered by a property.",
            "                                                           Example: (x)-[knows]->(y{name:John});",
            "   /pts <#>, </paths_to_show> <#>                      Set the number of paths to show (Default is 10).",
            "                                                           Note: Only show the first # paths but calculate all.",
            "   /lim <#/all>, /limit <#/all>                        Set the limit of paths to calculate (Default calculate all).",
            // "   /to <#><U>, /timeout<#><U>                          Set timeout for rpq executions.",
            // "                                                           '#' is the amount, and 'U' is the unit (S=seconds, M=minutes and H=hours).",
            // "                                                           Example: /to 30S (30 seconds) or /timeout 2M (2 minutes).",
            "   /opt <true/false>, /optimization <true/false>       Set optimized option (Default is false).",
            "   /i, /information                                    Show the information of the graph.",
            "   /l, /labels                                         Show a sample of each label in the graph.",
            "   /q, /quit                                           Quit the program."
        };
        for (String u : help) {
            System.out.println(u);
        }
        System.out.println();
    }

    public static void showMethods() {
        String[] methods = {
            "List of available methods:",
            "   1 - Algebra",
            "   2 - Regex + DFS",
            "   3 - Regex + BFS",
            "   4 - Automaton + DFS",
            "   5 - Automaton + BFS"
        };
        for (String u : methods) {
            System.out.println(u);
        }
        System.out.println();
    }
    
    public static void showSemantics() {
        String[] semantics = {
            "List of available semantics:",
            "   1 - Arbitrary",
            "   2 - Trail",
            "   3 - Simple Path"
        };
        for (String u : semantics) {
            System.out.println(u);
        }
        System.out.println();
    }

    public static void showSelectedMethod(Integer m) {
        switch (m) {
            case 1:
                System.out.println("Selected method: Algebra\n");
                break;

            case 2:
                System.out.println("Selected method: Regex + DFS\n");
                break;

            case 3:
                System.out.println("Selected method: Regex + BFS\n");
                break;

            case 4:
                System.out.println("Selected method: Automaton + DFS\n");
                break;

            case 5:
                System.out.println("Selected method: Automaton + BFS\n");
                break;
        }
    }

    public static void showSelectedSemantic(Integer e) {
        switch (e) {
            case 1:
                System.out.println("Selected evaluation semantics: Arbitrary\n");
                break;

            case 2:
                System.out.println("Selected evaluation semantics: Trail\n");
                break;

            case 3:
                System.out.println("Selected evaluation semantics: Simple Path\n");
                break;
        }
    }

    public static String getSelectedMethod(Integer m) {
        switch (m) {
            case 1: return "Algebra";
            case 2: return "Regex + DFS";
            case 3: return "Regex + BFS";
            case 4: return "Automaton + DFS";
            case 5: return "Automaton + BFS";
        }
        return "";
    }

    public static String getSelectedSemantic(Integer e) {
        switch (e) {
            case 1: return "Arbitrary";
            case 2: return "Trail";
            case 3: return "Simple Path";
        }
        return "";
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

}
