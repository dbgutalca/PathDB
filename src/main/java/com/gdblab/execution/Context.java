package com.gdblab.execution;

import java.io.File;
import java.util.Scanner;

import com.gdblab.schema.Edge;
import com.gdblab.schema.Graph;
import com.gdblab.schema.Node;
import com.gdblab.schema.impl.CSRVPMin;

public final class Context {
    private static Context INSTANCE;

    private Graph graph;
    private String data_type;
    private Integer fixedPoint;
    private String nodesFile;
    private String edgesFile;
    private String outputType;
    private String outputFileName;
    private Integer maxShowedPaths;

    private Context() {
        // By default, the data type is set to CSRVPMin
        this.data_type = "csrvpmin";
        
        // By default, the graph is set to CSRVPMin
        this.graph = new CSRVPMin();

        // By default, the fixedPoint is set to 3
        this.fixedPoint = 3;

        // By default, the output type is set to console
        this.outputType = "console";

        // By default, the output file name is set to output.txt
        this.outputFileName = "output.txt";

        // By default, the max showed paths is set to 10
        this.maxShowedPaths = 10;
    }

    public static Context getInstance() {
        if (INSTANCE == null){
            INSTANCE = new Context();
        } 
        
        return INSTANCE;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setDataType(String data_type) {
        this.data_type = data_type;
    }

    public String getDataType() {
        return data_type;
    }

    public void setFixedPoint(Integer fixedPoint) {
        this.fixedPoint = fixedPoint;
    }

    public Integer getFixedPoint() {
        return fixedPoint;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    public String getOutputType() {
        return outputType;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setMaxShowedPaths(Integer maxShowedPaths) {
        this.maxShowedPaths = maxShowedPaths;
    }

    public Integer getMaxShowedPaths() {
        return maxShowedPaths;
    }

    public void uploadNodes(String fileName) {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                Node node = new Node(data[0], data[1]);
                graph.insertNode(node);
            }
            scanner.close();
            nodesFile = fileName;
        } catch (Exception e) {}
    }

    public void uploadEdges(String fileName) {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            int i = 1;
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                Edge edge = new Edge(
                    "E" + i,
                    data[1],
                    this.graph.getNode(data[0]),
                    this.graph.getNode(data[2])
                );
                this.graph.insertEdge(edge);
                i++;
            }
            scanner.close();
            edgesFile = fileName;
        } catch (Exception e) {}
    }

    public String getNodesFileName() {
        return nodesFile;
    }

    public String getEdgesFileName() {
        return edgesFile;
    }
}
