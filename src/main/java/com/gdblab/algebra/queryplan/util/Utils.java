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
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Utils {
    
    public static List<Path> iterToList(final PhysicalOperator physicalOp) {
        List<Path> l = new LinkedList<>();
        while (physicalOp.hasNext()) {
            l.add(physicalOp.next());
        }
        return l;
    }
    
    public static List<Edge> edgesIterToList( Iterator<Edge> edges) {
        List<Edge> l = new LinkedList<>();
        while (edges.hasNext()) {
            l.add(edges.next());
        }
        return l;
    }

    public static List<Node> nodesIterToList( Iterator<Node> nodes) {
        List<Node> l = new LinkedList<>();
        while (nodes.hasNext()) {
            l.add(nodes.next());
        }
        return l;
    }

    public static String getTime(long start, long end){
        long duration = end - start;
        double durationInSeconds = (double) duration / 1_000_000_000.0;
        return String.format("%.5f", durationInSeconds);
    }

    public static Path NodeLink (Path pathA, Path pathB) {
        if (pathA.isNodeLinkable(pathB)) {
            Path join_path = new Path("");

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
        int counter = 1;

        Integer ms = 10;
        // if (ms <= 0) {
        //     while (po.hasNext()) {
        //         Path p = po.next();
        //         System.out.print("Path #" + counter + " - ");
        //         for (GraphObject go : p.getSequence()) {
        //             System.out.print( go.getLabel() + " ");
        //         }
        //         System.out.println();
        //         counter++;
        //     }
        // }

        // else {

            while ( po.hasNext() ) {
                Path p = po.next();
    
                if (counter <= ms) {
                    System.out.print("Path #" + counter + " - ");
                    for (GraphObject go : p.getSequence()) {
                        System.out.print( go.getLabel() + " ");
                    }
                    System.out.println();
                }
                if (counter == (ms + 1)) {
                    System.out.println("...");
                }
                counter++;
            }

        //}
        
        return counter;
    }

    public static void writeAndCountPaths(PhysicalOperator po) {
        int counter = 1;
        String filename = Context.getInstance().getResultFilename();
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
            
            writeConfig(writer);

            while (po.hasNext()) {
                Path p = po.next();
                writer.write("Path #" + counter + " - ");
                for (GraphObject go : p.getSequence()) {
                    writer.write( go.getLabel() + " ");
                }
                writer.write("\n");
                counter++;
            }

        } catch (Exception e) {
        } finally {
            try {writer.close();} catch (Exception ex) {}
            Context.getInstance().setTotalPaths(counter - 1);
        }
    }

    public static void writeConfig(Writer writer) {
        try {
            writer.write("=====================================\n");
            writer.write("Method: " + Tools.getSelectedMethod(Context.getInstance().getMethod()) + "\n");
            writer.write("Fix Point: " + Context.getInstance().getFixPoint() + "\n");
            writer.write("Start Node: " + Context.getInstance().getStartNode() + "\n");
            writer.write("End Node: " + Context.getInstance().getEndNode() + "\n");
            writer.write("RPQ: " + Context.getInstance().getRPQ() + "\n");
            writer.write("Total: <$TOTAL_PATHS$>\n");
            writer.write("Time: <$TOTAL_TIME$> s\n");
            writer.write("=====================================\n");
        } catch (Exception e) {}
    }

    public static void writeTotalAndTime() {
        int total = Context.getInstance().getTotalPaths();
        String time = Context.getInstance().getTime();
        String filename = Context.getInstance().getResultFilename();
        String tempFilename = "temp_" + filename;

        try (
            BufferedReader reader = Files.newBufferedReader(Paths.get(filename));
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(tempFilename));
        ) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("<$TOTAL_PATHS$>")) {
                    line = line.replace( "<$TOTAL_PATHS$>", String.valueOf(total) );
                }
                if (line.contains("<$TOTAL_TIME$>")) {
                    line = line.replace( "<$TOTAL_TIME$>", time );
                }
                writer.write(line + "\n");
            }
        } catch (Exception e) {}

        try {
            Files.move(Paths.get(tempFilename), Paths.get(filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {}
    }
}
