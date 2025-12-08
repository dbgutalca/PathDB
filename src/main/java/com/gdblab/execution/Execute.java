package com.gdblab.execution;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import com.gdblab.algorithm.translator.RPQtoER;
import com.gdblab.algorithm.utils.Time;
import com.gdblab.algorithm.versions.utils.Algorithm;
import com.gdblab.algorithm.versions.v1.BFSRegex;
import com.gdblab.algorithm.versions.v1.DFSRegex;
import com.gdblab.algorithm.versions.v2.BFSAutomaton;
import com.gdblab.algorithm.versions.v2.DFSAutomaton;
import com.gdblab.graph.Graph;
import com.gdblab.graph.schema.Edge;

public final class Execute {

    private static final String prefix = "/";

    public static void evalRPQ() {
        long start = System.nanoTime();

        String er = RPQtoER.Translate(Context.getInstance().getRPQ());

        int method = Context.getInstance().getMethod();
        Algorithm algorithm = null;

        switch (method) {
            case 1:
                algorithm = new BFSRegex(er);
                break;
            case 2:
                algorithm = new DFSRegex(er);
                break;
            case 3:
                algorithm = new BFSAutomaton(er);
                break;
            case 4:
                algorithm = new DFSAutomaton(er);
                break;
        }

        algorithm.execute();

        long end = System.nanoTime();
        System.out.println("\nTotal paths: " + algorithm.getTotalPaths() + " paths");
        System.out.println("Execution time: " + Time.getTimeBetween(start, end) + " seconds");
        System.out.println("");
    }

    public static void interactive(String[] args) {

        try {
            Terminal terminal = TerminalBuilder.terminal();
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

            Tools.clearConsole();

            if (args.length == 0) {
                Tools.showUsageNoArgs();
                Tools.loadDefaultGraph();
            } else {
                Tools.showUsageArgsLoadingCustomGraph(args[0], args[1]);
                Tools.loadCustomGraphFiles(args[0], args[1]);
            }

            String prompt = "PathDB> ";

            while (true) {

                String line = reader.readLine(prompt);

                if (line == null || line.equalsIgnoreCase(prefix + "q") || line.equalsIgnoreCase(prefix + "quit")) {
                    System.out.println("Terminated execution.");
                    break;
                }

                reader.getHistory().add(line);

                if (line.equals(prefix + "h") || line.equals(prefix + "help")) {
                    // clearConsole();
                    Tools.showHelp();
                    System.out.println();
                    continue;
                } else if (line.startsWith(prefix + "me ") || line.startsWith(prefix + "method ")) {
                    String[] parts = line.split(" ");
                    if (parts.length != 2) {
                        // clearConsole();
                        System.out.println("Invalid command. Use /me # to select a method.\n");
                        continue;
                    }
                    try {
                        Integer.parseInt(parts[1]);
                    } catch (NumberFormatException e) {
                        // clearConsole();
                        System.out.println("Invalid command. # must be a positive number greater than 0.\n");
                        continue;
                    }
                    if (Integer.parseInt(parts[1]) < 1 || Integer.parseInt(parts[1]) > 5) {
                        // clearConsole();
                        System.out.println("Invalid command. # must be a number between 1 and 5.\n");
                        continue;
                    }
                    Context.getInstance().setMethod(Integer.parseInt(parts[1]));
                    Tools.showSelectedMethod(Integer.parseInt(parts[1]));
                    continue;
                } else if (line.startsWith(prefix + "ml ") || line.startsWith(prefix + "max_length ")) {
                    String[] parts = line.split(" ");

                    if (parts.length != 2) {
                        // clearConsole();
                        System.out.println("Invalid command. Use /ml <#> or /max_length <#> to set max the length of generated paths.\n");
                        continue;
                    }

                    try {
                        Integer.parseInt(parts[1]);
                    } catch (NumberFormatException e) {
                        // clearConsole();
                        System.out.println("Invalid command. # must be a positive number greater than 0.\n");
                        continue;
                    }

                    Context.getInstance().setFixPoint(Integer.parseInt(parts[1]));
                    System.out.println("Set max length of paths to: " + parts[1] + "\n");
                    continue;
                } else if (line.startsWith(prefix + "mr ") || line.startsWith(prefix + "max_recursion ")) {
                    String[] parts = line.split(" ");

                    if (parts.length != 2) {
                        // clearConsole();
                        System.out.println("Invalid command. Use /mr <#> or /max_recursion <#> to set max recursion of each recursive operation.\n");
                        continue;
                    }

                    try {
                        Integer.parseInt(parts[1]);
                    } catch (NumberFormatException e) {
                        // clearConsole();
                        System.out.println("Invalid command. # must be a positive number greater or equals to 0.\n");
                        continue;
                    }

                    if (Integer.parseInt(parts[1]) < 0) {
                        // clearConsole();
                        System.out.println("Invalid command. # must be a positive number greater or equals to 0.\n");
                        continue;
                    }

                    Context.getInstance().setMaxRecursion(Integer.parseInt(parts[1]));
                    System.out.println("Set max recursion to: " + parts[1] + "\n");
                    continue;
                } else if (line.startsWith(prefix + "sem ") || line.startsWith(prefix + "semantic ")) {
                    String[] parts = line.split(" ");

                    if (parts.length != 2) {
                        System.out.println("Invalid command. Use /sem <1-3> or /semantic <1-3> to set a semantic.\n");
                        continue;
                    }

                    try {
                        Integer.parseInt(parts[1]);
                    } catch (Exception e) {
                        System.out.println("Invalid command. Use /sem <1-3> or /semantic <1-3> to set a semantic.\n");
                        continue;
                    }

                    if (Integer.parseInt(parts[1]) < 1 || Integer.parseInt(parts[1]) > 3) {
                        System.out.println("Invalid command. Use /sem <1-3> or /semantic <1-3> to set a semantic.\n");
                        continue;
                    }

                    Context.getInstance().setSemantic(Integer.parseInt(parts[1]));
                    Tools.showSelectedSemantic(Integer.parseInt(parts[1]));
                    continue;
                } else if (line.startsWith(prefix + "pts ") || line.startsWith(prefix + "paths_to_show ")) {
                    String[] parts = line.split(" ");

                    if (parts.length != 2) {
                        System.out.println("Invalid command. Use /pts <#> or /paths_to_show <#> to set the number of paths to show.\n");
                        continue;
                    }

                    try {
                        Integer.parseInt(parts[1]);
                    } catch (Exception e) {
                        System.out.println("Invalid command. Use /p <#> or /paths_to_show <#> to set the number of paths to show.\n");
                        continue;
                    }

                    if (Integer.parseInt(parts[1]) < 0) {
                        System.out.println("Invalid command. <#> must be a positive number greater than 0.\n");
                        continue;
                    }

                    Context.getInstance().setShowPaths(Integer.parseInt(parts[1]));
                    System.out.println("Set show paths configuration to: " + parts[1] + "\n");
                    continue;
                } else if (line.startsWith(prefix + "lim ") || line.startsWith(prefix + "limit ")) {
                    String[] parts = line.split(" ");

                    if (parts.length != 2) {
                        System.out.println("Invalid command. Use /lim <#> or /limit <#> to set the number of paths to calculate.\n");
                        continue;
                    }

                    if (parts[1].equalsIgnoreCase("all")) {
                        Context.getInstance().setMaxPaths(Integer.MAX_VALUE);
                        System.out.println("Set to calculate all possible paths.\n");
                        continue;
                    }

                    try {
                        Integer.parseInt(parts[1]);
                    } catch (Exception e) {
                        System.out.println("Invalid command. Use /lim <#> or /limit <#> to set the number of paths to calculate.\n");
                        continue;
                    }

                    if (Integer.parseInt(parts[1]) < 0) {
                        System.out.println("Invalid command. <#> must be a positive number greater than 0.\n");
                        continue;
                    }

                    Context.getInstance().setLimit(Integer.parseInt(parts[1]));
                    System.out.println("Set show paths configuration to: " + parts[1] + "\n");
                    continue;
                } else if (line.startsWith(prefix + "opt ") || line.startsWith(prefix + "optimize ")) {
                    String[] parts = line.split(" ");

                    if (parts.length != 2) {
                        // clearConsole();
                        System.out.println("Invalid command. Use /opt <true/false> or /optimize <true/false> to set optimization.\n");
                        continue;
                    }

                    if (parts[1].equalsIgnoreCase("true")) {
                        Context.getInstance().setOptimize(true);
                        System.out.println("Optimization set to: true.\n");
                    } else if (parts[1].equalsIgnoreCase("false")) {
                        Context.getInstance().setOptimize(false);
                        System.out.println("Optimization set to: false.\n");
                    } else {
                        // clearConsole();
                        System.out.println("Invalid command. Use /opt <true/false> or /optimization <true/false> to set optimization.\n");
                    }
                    continue;
                } else if (line.endsWith(";")) {

                    line = line.trim();
                    String[] parts = line.split("-");

                    if (parts.length != 3) {
                        System.out.println("Invalid command. Use '(X{prop:value})-[RPQ]->(Y);'' to execute a RPQ.\n");
                        continue;
                    }

                    String regex = "\\{([^:]+):([^}]+)\\}";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(parts[0]);

                    if (matcher.find()) {
                        Context.getInstance().setStartNodeProp(matcher.group(1));
                        Context.getInstance().setStartNodeValue(matcher.group(2));
                    } else {
                        Context.getInstance().setStartNodeProp("");
                        Context.getInstance().setStartNodeValue("");
                    }

                    matcher = pattern.matcher(parts[2]);

                    if (matcher.find()) {
                        Context.getInstance().setEndNodeProp(matcher.group(1));
                        Context.getInstance().setEndNodeValue(matcher.group(2));
                    } else {
                        Context.getInstance().setEndNodeProp("");
                        Context.getInstance().setEndNodeValue("");
                    }

                    Context.getInstance().setRPQ(parts[1].substring(1, parts[1].length() - 1));

                    evalRPQ();
                } else if (line.equals(prefix + "i") || line.equals(prefix + "information")) {
                    // clearConsole();
                    System.out.println("Graph Information:");
                    System.out.println("Total nodes: " + Graph.getGraph().getNodesQuantity());
                    System.out.println("Total edges: " + Graph.getGraph().getEdgesQuantity());
                    System.out.println("Total label: " + Graph.getGraph().getDifferetEdgesQuantity());
                    System.out.println("Edges per label: " + Graph.getGraph().getEdgesByLabelQuantity().toString());
                    System.out.println("\n");
                    continue;
                } else if (line.equals(prefix + "l") || line.equals(prefix + "labels")) {
                    System.out.println("Samples: ");
                    ArrayList<Edge> edges = Graph.getGraph().getSampleOfEachlabel();
                    for (Edge e : edges) {
                        System.out.println(e.getId() + ": " + e.getSource().getId() + "," + e.getLabel() + "," + e.getTarget().getId());
                    }
                    System.out.println("\n");
                    continue;
                } else {
                    System.out.println("Invalid command. For help, type /h.\n\n");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
