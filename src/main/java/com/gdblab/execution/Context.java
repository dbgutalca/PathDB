package com.gdblab.execution;

import java.io.BufferedReader;
import java.io.FileReader;

import com.gdblab.schema.Edge;
import com.gdblab.schema.Graph;
import com.gdblab.schema.Node;
import com.gdblab.schema.impl.CSRVPMin;

public final class Context {
    private static Context INSTANCE;

    private Graph graph;
    private String data_type;
    private Integer fixPoint;
    private String nodesFile;
    private String edgesFile;
    private String outputType;
    private String outputFileName;
    private Integer maxShowedPaths;
    private String rpq;
    private String startingNode;
    private String endingNode;

    private Context() {
        // By default, the data type is set to CSRVPMin
        this.data_type = "csrvpmin";

        // By default, the graph is set to CSRVPMin
        this.graph = new CSRVPMin();

        // By default, the fixPoint is set to 3
        this.fixPoint = 3;

        // By default, the output type is set to console
        this.outputType = "console";

        // By default, the output file name is set to output.txt
        this.outputFileName = "output.txt";

        // By default, the max showed paths is set to 10
        this.maxShowedPaths = 10;

        // By default, the RPQ is set to an empty string
        this.rpq = "";

        // By default, the starting node is set to an empty string
        this.startingNode = "";

        // By default, the ending node is set to an empty string
        this.endingNode = "";
    }

    public static Context getInstance() {
        if (INSTANCE == null) {
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

    public void setFixPoint(Integer fixPoint) {
        this.fixPoint = fixPoint;
    }

    public Integer getFixPoint() {
        return fixPoint;
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
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Node node = new Node(data[0], data[1]);
                graph.insertNode(node);
            }
            System.out.println("Nodes uploaded successfully");
            nodesFile = fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadEdges(String fileName) {

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            long i = 1;
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Edge edge = new Edge(
                        "E" + i,
                        data[1],
                        this.graph.getNode(data[0]),
                        this.graph.getNode(data[2]));
                this.graph.insertEdge(edge);
                i++;
            }
            System.out.println("Edges uploaded successfully");
            edgesFile = fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getNodesFileName() {
        return nodesFile;
    }

    public String getEdgesFileName() {
        return edgesFile;
    }

    public void setRPQ(String rpq) {
        this.rpq = rpq;
    }

    public String getRPQ() {
        return rpq;
    }

    public void setStartingNode(String startingNode) {
        this.startingNode = startingNode;
    }

    public String getStartingNode() {
        return startingNode;
    }

    public void setEndingNode(String endingNode) {
        this.endingNode = endingNode;
    }

    public String getEndingNode() {
        return endingNode;
    }

    public boolean optimize() {
        return false; // TODO: dejar como parámetro
    }
}
