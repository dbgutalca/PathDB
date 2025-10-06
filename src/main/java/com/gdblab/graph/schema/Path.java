package com.gdblab.graph.schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gdblab.graph.Graph;

/**
 *
 * @author Vicente Rojas Aranda
 * 
 *         This is a re-implementation of the Path class from the original code.
 *         It represents a path in a graph with a source ID, target ID, and an
 *         array of graph objects.
 *         This implementations has been simplified to optimize memory usage and
 *         performance.
 *         The original code had a more complex structure with additional fields
 *         and methods.
 * 
 *         For more information check "GQLLikeGrammar" branch of the repository.
 */
public class Path {

    private final Integer nodesQuantity;        // Nodes quantity for restrictions
    private final Integer edgesQuantity;        // Edges quantity for restrictions
    private final StringBuilder sourceId;       // String for sourceId    \__ For
    private final StringBuilder targetId;       // String for targetId    /   joins
    private final StringBuilder nodesIds;       // Complete String for list of Nodes Ids 
    private final StringBuilder edgesIds;       // Complete String for list of Edges Ids

    // #region Constructors
    public Path(String nodeId) {
        this.sourceId = new StringBuilder(nodeId);
        this.nodesIds = new StringBuilder(nodeId);
        this.edgesIds = new StringBuilder();
        this.targetId = new StringBuilder(nodeId);
        this.nodesQuantity = 1;
        this.edgesQuantity = 0;
    }

    public Path(String sourceNode, String edge, String targetNode) {
        this.sourceId = new StringBuilder(sourceNode);
        this.nodesIds = new StringBuilder(sourceNode + ";" + targetNode);
        this.edgesIds = new StringBuilder(edge);
        this.targetId = new StringBuilder(targetNode);
        this.nodesQuantity = 2;
        this.edgesQuantity = 1;
    }

    public Path(Path pathA, Path pathB) {
        this.sourceId = pathA.getFirst();
        this.targetId = pathB.getLast();
        this.nodesQuantity = pathA.getQuantityOfNodes() + pathB.getQuantityOfNodes() - 1;
        this.edgesQuantity = pathA.getQuantityOfEdges() + pathB.getQuantityOfEdges();

        List<String> nodesPathA = new ArrayList<>(Arrays.asList(pathA.getNodes().toString().split(";")));
        List<String> nodesPathB = new ArrayList<>(Arrays.asList(pathB.getNodes().toString().split(";")));

        nodesPathA.remove(nodesPathA.size() - 1);

        StringBuilder newNodesIds = new StringBuilder(String.join(";", nodesPathA));
        newNodesIds.append(";");
        newNodesIds.append(String.join(";", nodesPathB));
        this.nodesIds = newNodesIds;

        StringBuilder newEdgesIds = new StringBuilder(pathA.getEdges());
        newEdgesIds.append(";");
        newEdgesIds.append(pathB.getEdges());

        this.edgesIds = newEdgesIds;
    }
    // #endregion

    // #region PathUtils
    public StringBuilder getFirst() {
        return this.sourceId;
    }

    public StringBuilder getLast() {
        return this.targetId;
    }

    public Integer getPathLength() {
        return this.getQuantityOfEdges();
    }

    public boolean isLinkeableByNodeWith(Path otherPath) {
        return this.targetId.toString().equals(otherPath.getFirst().toString());
    }

    public Integer getSumOfEdgesWith(Path otherPath) {
        return this.getQuantityOfEdges() + otherPath.getQuantityOfEdges();
    }

    public Integer getQuantityOfNodes() {
        return this.nodesQuantity;
    }

    public Integer getQuantityOfEdges() {
        return this.edgesQuantity;
    }
    // #endregion

    // #region Semantic Checksdaws
    public boolean isTrailWith(Path otherPath) {
        Set<String> edgeSet = new HashSet<>(Arrays.asList(this.getEdges().toString().split(";")));
        for (String edge : otherPath.getEdges().toString().split(";")) {
            if (!edgeSet.add(edge)) return false;
        }
        return true;
    }

    public boolean isSimplePathWith(Path otherPath) {
        Set<String> nodeSet = new HashSet<>(Arrays.asList(this.getNodes().toString().split(";")));
        for(String node : otherPath.getNodes().toString().split(";")) {
            if (!nodeSet.add(node)) return false;
        }
        return true;
    }

    public boolean isAcyclicWith(Path otherPath) {
        Set<String> nodeSet = new HashSet<>(Arrays.asList(this.getNodes().toString().split(";")));
        nodeSet.remove(this.getFirst().toString());
        for(String node : otherPath.getNodes().toString().split(";")) {
            if (!nodeSet.add(node)) return false;
        }
        return true;
    }

    public boolean isSelfSimplePath() {
        List<String> nodesSplit = Arrays.asList(this.getNodes().toString().split(";"));
        if (nodesSplit.size() == 1) return false;

        Set<String> nodeSet = new HashSet<>(nodesSplit);
        return nodesSplit.size() == nodeSet.size();
    }

    public boolean isSelfAcyclic() {
        List<String> nodesSplit = Arrays.asList(this.getNodes().toString().split(";"));
        nodesSplit.remove(this.getFirst().toString());
        if (nodesSplit.size() == 1) return false;

        Set<String> nodeSet = new HashSet<>(nodesSplit);
        return nodesSplit.size() == nodeSet.size();
    }
    // #endregion

    // #region Path Components Crud
    public StringBuilder getEdges() {
        return this.edgesIds;
    }

    public StringBuilder getNodes() {
        return this.nodesIds;
    }
    // #endregion

    // #region Path Return Methods
    public String getEdge(int position) {
        String[] edges = this.getEdges().toString().split(";");
        if (edges.length == 0 || position >= edges.length) return "";
        String edge = Graph.getGraph().getEdge(edges[position]);
        return edge == null ? "" : edge;
    }

    public String getNode(int position) {

        String[] nodes = this.getNodes().toString().split(";");
        if (position == -1) position = nodes.length - 1;
        if (nodes.length == 0 || position >= nodes.length) return "";
        String node = Graph.getGraph().getNode(nodes[position]);
        return node == null ? "" : node;
    }

    public String getPropertyValueOfNodeAtPosition(int position, String propertyToSearch) {
        String node = this.getNode(position);

        if (node.equals("")) return "";

        String[] nodeProperties = node.split("\\|");
        for (String property : nodeProperties) {
            if (property.startsWith(propertyToSearch + ":")) {
                return property.split(":")[1];
            }
        }

        return "";
    }

    public String getPropertyValueOfEdgeAtPosition(int position, String propertyToSearch) {
        String edge = this.getEdge(position);

        if (edge.equals("")) return "";

        String[] edgeProperties = edge.split("\\|");
        for (String property : edgeProperties) {
            if (property.startsWith(propertyToSearch + ":")) {
                return property.split(":")[1];
            }
        }

        return "";

    }

    public String getLabelOfNodeAtPosition(int position) {
        return this.getPropertyValueOfNodeAtPosition(position, "label");
    }

    public String getLabelOfEdgeAtPosition(int position) {
        return this.getPropertyValueOfEdgeAtPosition(position, "label");
    }

    public String getStringPath() {
        ArrayList<String> nodesData = new ArrayList<>();
        ArrayList<String> edgesData = new ArrayList<>();

        for (String string : this.getNodes().toString().split(";")) {
            String nodeData = Graph.getGraph().getNode(string);
            if (nodeData != null)
                nodesData.add(nodeData);
        }

        for (String string : this.getEdges().toString().split(";")) {
            String edgeData = Graph.getGraph().getEdge(string);
            if (edgeData != null)
                edgesData.add(edgeData);
        }

        String res = "";

        for (int i = 0; i < edgesData.size(); i++) {
            res = res + nodesData.get(i) + ", ";
            res = res + edgesData.get(i) + ", ";
        }

        res = res + nodesData.get(nodesData.size() - 1);

        return res;
    }
    // #endregion

}
