package com.gdblab.algebra.queryplan.util;

import com.gdblab.algebra.queryplan.physical.PhysicalOperator;
import com.gdblab.execution.Context;
import com.gdblab.execution.Tools;
import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.GraphObject;
import com.gdblab.graph.schema.Node;
import com.gdblab.graph.schema.Path;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Utils {
    
    public static List<Path> iterToList(final PhysicalOperator physicalOp) {
        List<Path> l = new ArrayList<>();
        while (physicalOp.hasNext()) {
            l.add(physicalOp.next());
        }
        return l;
    }
    
    public static List<Edge> edgesIterToList( Iterator<Edge> edges) {
        List<Edge> l = new ArrayList<>();
        while (edges.hasNext()) {
            l.add(edges.next());
        }
        return l;
    }

    public static List<Node> nodesIterToList( Iterator<Node> nodes) {
        List<Node> l = new ArrayList<>();
        while (nodes.hasNext()) {
            l.add(nodes.next());
        }
        return l;
    }

    public static String getTime(long start, long end){
        long duration = end - start;
        double durationInSeconds = (double) duration / 1_000_000_000.0;
        return String.format("%.3f", durationInSeconds);
    }

    public static Path NodeLink (Path pathA, Path pathB) {
        
        if (pathA.isNodeLinkable(pathB) && pathA.getSumEdges(pathB) <= Context.getInstance().getFixPoint()) {

            switch (Context.getInstance().getSemantic()) {
                case 2:
                    if (!pathA.isTrail(pathB)) return null;
                    break;
                case 3:
                    if (!pathA.isSimplePath(pathB)) return null;
                    break;
            }

            Path join_path = new Path("", (pathA.getEdgeLength() + pathB.getEdgeLength()));

            if (pathA.getNodesAmount() == 1 && pathB.getNodesAmount() == 1) {
                join_path.insertNode(pathA.first());
            } else {
                join_path.setSequence(pathA.getSequence());
                join_path.appendSequence(pathB.getSequence());
            }

            return join_path;
        }

        return null;
    }
    
    public static int printAndCountPaths(PhysicalOperator po){
        Integer counterMS = 1;
        Integer counterLP = 1;

        Integer maxShowPaths = Context.getInstance().getShowPaths();
        Integer limitCalculatePaths = Context.getInstance().getMaxPaths();

        while (counterLP <= limitCalculatePaths && po.hasNext() ) {
            Path p = po.next();

            if (counterMS <= maxShowPaths) {
                System.out.print("Path #" + counterMS + " - ");
                for (GraphObject go : p.getSequence()) {
                    if (go instanceof Edge) {
                        System.out.print( go.getId() + "(" + go.getLabel() + ")" + " ");
                    }
                    else {
                        System.out.print( go.getId() + " ");
                    }
                    
                }
                System.out.println();
            }
            if (counterMS == (maxShowPaths + 1)) {
                System.out.println("...");
            }
            counterMS++;
            counterLP++;
        }

        return counterLP;
    }

    public static void writeAndCountPaths(PhysicalOperator po) {
        int counter = 1;
        // String filename = Context.getInstance().getResultFilename();
        // Writer writer = null;

        try {
            // writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
            
            // writeConfig(writer);

            while (po.hasNext() && Context.getInstance().getMaxPaths() > counter) {
                Path p = po.next();
                // writer.write("Path #" + counter + " - ");
                // for (GraphObject go : p.getSequence()) {
                //     if (go instanceof Edge) {
                //         writer.write(go.getId() + "(" + go.getLabel() + ") ");
                //     }
                //     else {
                //         writer.write(go.getLabel() + " ");
                //     }
                // }
                // writer.write("\n");
                counter++;
            }

        } catch (Exception e) {
        } finally {
            // try {writer.close();} catch (Exception ex) {
            //     ex.printStackTrace();
            // }
            Context.getInstance().setTotalPaths(counter);
        }
    }

    public static void writeConfig(Writer writer) {
        try {
            // writer.write("=====================================\n");
            // writer.write("Method: " + Tools.getSelectedMethod(Context.getInstance().getMethod()) + "\n");
            // writer.write("Fix Point: " + Context.getInstance().getFixPoint() + "\n");
            // writer.write("Start Node: " + Context.getInstance().getStartNode() + "\n");
            // writer.write("End Node: " + Context.getInstance().getEndNode() + "\n");
            writer.write("RPQ: " + Context.getInstance().getRPQ() + "\n");
            // if (Context.getInstance().getMethod() == 1) writer.write("Optimized: " + Context.getInstance().isOptimize() + "\n");
            writer.write("Total: <$TOTAL_PATHS$>\n");
            writer.write("Time: <$TOTAL_TIME$> s\n");
            writer.write("=====================================\n");
        } catch (Exception e) {}
    }

    public static void writeTotalAndTime() {

        writeOnSummary();

        int total = Context.getInstance().getTotalPaths();
        String time = Context.getInstance().getTime();
        String filename = Context.getInstance().getResultFilename();
        String tempFilename = "temp_" + filename;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename));
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(tempFilename), StandardOpenOption.CREATE)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replace("<$TOTAL_PATHS$>", String.valueOf(total));
                line = line.replace("<$TOTAL_TIME$>", time);
                writer.write(line);
                writer.newLine();
            }
        } catch (Exception e) {
            System.err.println("Error al procesar el archivo: " + e.getMessage());
            return;
        }

        try {
            Files.move(Paths.get(tempFilename), Paths.get(filename), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (Exception e) {
            System.err.println("Error al sobrescribir el archivo original: " + e.getMessage());
        }
    }

    public static void writeOnSummary() {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("summary.txt", true), "utf-8"))) {
            writer.write("Method: " + Tools.getSelectedMethod(Context.getInstance().getMethod()) + "\n");
            writer.write("Start Node: " + Context.getInstance().getStartNode() + "\n");
            writer.write("End Node: " + Context.getInstance().getEndNode() + "\n");
            writer.write("RPQ: " + Context.getInstance().getRPQ() + "\n");
            writer.write("Total: " + Context.getInstance().getTotalPaths() + "\n");
            writer.write("Time: " + Context.getInstance().getTime() + " s\n");
            writer.write("=====================================\n");
            writer.close();
        } catch (Exception e) {}
    }
}
