package com.gdblab.execution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import com.gdblab.algorithm.utils.LabelMap;
import com.gdblab.graph.DefaultGraph;
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
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Node node = new Node(data[0], data[1]);
                Graph.getGraph().insertNode(node);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(edgesFile))) {
            int i = 1;
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Edge edge = new Edge(
                    "E" + i,
                    data[1],
                    Graph.getGraph().getNode(data[0]),
                    Graph.getGraph().getNode(data[2])
                );

                if (!LabelMap.containsKey(edge.getLabel())) LabelMap.put(edge.getLabel());

                Graph.getGraph().insertEdge(edge);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadDefaultGraph() {
        InterfaceGraph graph = Graph.getGraph();

        Node[] nodes = DefaultGraph.getDefaultNodes();
        for (Node n : nodes) { graph.insertNode(n); }

        Edge[] edges = DefaultGraph.getDefaultEdges();
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
            "For help, type /h."
        };

        for (String u : usage) {
            System.out.println(u);
        }
        System.out.println();
    }

    public static void showUsageArgs(String nf, String ef) {
        String[] usage = {
            "Welcome to PathDB! A tool to evaluate Regular Path Queries in Graphs.",
            "",
            "Graph loaded successfully. Using " + nf + " and " + ef + " files.",
            "",
            "For help, type /h."
        };

        for (String u : usage) {
            System.out.println(u);
        }
        System.out.println();
    }

    public static void showHelp() {
        String[] help = {
            "List of available commands:",
            "   /h              Show this help.",
            "   /m <1-5>        Select evaluation method.",
            "                       1 - Algebra (Default).",
            "                       2 - Regex + DFS.",
            "                       3 - Regex + BFS.",
            "                       4 - Automaton + DFS.",
            "                       5 - Automaton + BFS.",
            "   /f #            Set the fix point of recursion loops (Default is 3).",
            "   (S, RPQ, E);    Query to evaluate.",
            "                       S = Start Node.",
            "                       RPQ = Regular Path Query.",
            "                       E = End Node.",
            "                       Example: (N1,(A?.B+)*,N2);",
            "   /i              Show the information of the graph.",
            "   /s              Show a sample of each label in the graph.",
            "   /o              Set optimized option (Default is false).",
            "   /q              Quit the program."
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

    public static ArrayList<String> readRPQsFromFile(String rpqs_file) {
        ArrayList<String> rpqs = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rpqs_file))) {
            String line;
            while ((line = br.readLine()) != null) {
                rpqs.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rpqs;
    }
}
