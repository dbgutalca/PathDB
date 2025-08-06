package com.gdblab.graph.schema;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
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

    private final LinkedList<String> edges;
    private final ArrayDeque<String> nodes;

    // #region Constructors
    public Path() {
        this.edges = new LinkedList<>();
        this.nodes = new ArrayDeque<>();
    }

    public Path(String nodeId) {
        this.edges = new LinkedList<>();
        this.nodes = new ArrayDeque<>();
        this.nodes.add(nodeId);
    }

    public Path(String sourceNode, String edge, String targetNode) {
        this.edges = new LinkedList<>();
        this.nodes = new ArrayDeque<>();
        this.nodes.add(sourceNode);
        this.edges.add(edge);
        this.nodes.add(targetNode);
    }

    public Path(Path otherPath) {
        this.edges = new LinkedList<>(otherPath.getEdges());
        this.nodes = new ArrayDeque<>(otherPath.getNodes());
    }

    public Path(Path pathA, Path pathB) {
        this.nodes = new ArrayDeque<>(pathA.getNodes());
        pathB.getNodes().removeFirst();
        this.nodes.addAll(pathB.getNodes());
        this.edges = new LinkedList<>(pathA.getEdges());
        this.edges.addAll(pathB.getEdges());
    }
    // #endregion

    // #region PathUtils
    public String getFirst() {
        return this.nodes.getFirst();
    }

    public String getLast() {
        return this.nodes.getLast();
    }

    public Integer getPathLength() {
        return this.getQuantityOfEdges();
    }

    public boolean isLinkeableByNodeWith(Path otherPath) {
        return this.nodes.getLast().equals(otherPath.nodes.getFirst());
    }

    public Integer getSumOfEdgesWith(Path otherPath) {
        return this.getQuantityOfEdges() + otherPath.getQuantityOfEdges();
    }

    public Integer getQuantityOfNodes() {
        if (this.nodes.isEmpty() || this.nodes == null)
            return 0;
        return this.nodes.size();
    }

    public Integer getQuantityOfEdges() {
        if (this.edges.isEmpty() || this.edges == null)
            return 0;
        return this.edges.size();
    }
    // #endregion

    // #region Semantic Checks
    public boolean isTrailWith(Path otherPath) {
        Set<String> edgeSet = new HashSet<>(this.edges);
        for (String edge : otherPath.edges) {
            if (!edgeSet.add(edge))
                return false;
        }
        edgeSet = null;
        return true;
    }

    public boolean isSimplePathWith(Path otherPath) {
        Set<String> nodeSet = new HashSet<>(this.nodes);
        for (String node : otherPath.nodes) {
            if (!nodeSet.add(node))
                return false;
        }
        nodeSet = null;
        return true;
    }

    public boolean isAcyclicWith(Path otherPath) {
        Set<String> nodeSet = new HashSet<>(this.nodes);
        nodeSet.remove(this.nodes.getFirst());
        for (String node : otherPath.nodes) {
            if (!nodeSet.add(node))
                return false;
        }
        nodeSet = null;
        return true;
    }

    public boolean isSelfSimplePath() {
        if (this.nodes.isEmpty() || this.nodes.size() < 2)
            return false;
        Set<String> nodeSet = new HashSet<>(this.nodes);
        return nodeSet.size() == this.nodes.size();
    }

    public boolean isSelfAcyclic() {
        if (this.nodes.isEmpty() || this.nodes.size() < 2)
            return false;
        Set<String> nodeSet = new HashSet<>(this.nodes);
        nodeSet.remove(this.nodes.getFirst());
        return nodeSet.size() == this.nodes.size() - 1;
    }
    // #endregion

    // #region Path Components Crud
    public LinkedList<String> getEdges() {
        return this.edges;
    }

    public ArrayDeque<String> getNodes() {
        return this.nodes;
    }

    public void insertNode(String node) {
        this.nodes.add(node);
    }

    public void insertEdge(String sourceNode, String edge, String targetNode) {
        if (this.nodes.isEmpty()) {
            this.nodes.add(sourceNode);
            this.edges.add(edge);
            this.nodes.add(targetNode);
        } else {
            this.edges.add(edge);
            this.nodes.add(targetNode);
        }

    }
    // #endregion

    // #region Path Return Methods
    public String getEdge(int position) {
        if (this.edges.isEmpty() || this.edges.size() <= position)
            return "";

        String edgeIDToSearch = ((String[]) this.edges.toArray())[position];

        String edge = Graph.getGraph().getEdge(edgeIDToSearch);

        return edge == null ? "" : edge;
    }

    public String getNode(int position) {
        if (this.nodes.isEmpty() || this.nodes.size() <= position)
            return "";

        String nodeIDToSearch = ((String[]) this.nodes.toArray())[position];

        String node = Graph.getGraph().getNode(nodeIDToSearch);

        return node == null ? "" : node;
    }

    public String getPropertyValueOfNodeAtPosition(int position, String propertyToSearch) {
        if (this.nodes.isEmpty() || this.nodes.size() <= position)
            return null;

        String[] nodeArray = (String[]) this.nodes.toArray();

        if (position == -1)
            position = nodeArray.length - 1;

        if (position < 0 || position >= nodeArray.length)
            return null;

        String nodeID = nodeArray[position];

        String nodeData = Graph.getGraph().getNode(nodeID);
        if (nodeData == null)
            return null;

        String[] nodeProperties = nodeData.split("\\|");
        for (String property : nodeProperties) {
            if (property.startsWith(propertyToSearch + ":")) {
                return property.split(":")[1];
            }
        }

        return null;
    }

    public String getPropertyValueOfEdgeAtPosition(int position, String propertyToSearch) {
        if (this.edges.isEmpty() || this.edges.size() <= position)
            return null;

        String[] edgeArray = (String[]) this.edges.toArray();

        if (position < 0 || position >= edgeArray.length)
            return null;

        String edgeID = edgeArray[position];

        String edgeData = Graph.getGraph().getEdge(edgeID);
        if (edgeData == null)
            return null;

        String[] edgeProperties = edgeData.split("\\|");
        for (String property : edgeProperties) {
            if (property.startsWith(propertyToSearch + ":")) {
                return property.split(":")[1];
            }
        }

        return null;

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

        for (String string : this.getNodes()) {
            String nodeData = Graph.getGraph().getNode(string);
            if (nodeData != null)
                nodesData.add(nodeData);
        }

        for (String string : this.getEdges()) {
            String edgeData = Graph.getGraph().getEdge(string);
            if (edgeData != null)
                edgesData.add(edgeData);
        }

        StringBuilder pathString = new StringBuilder();

        for (int i = 0; i < edgesData.size(); i++) {
            pathString.append(nodesData.get(i)).append(", ");
            pathString.append(edgesData.get(i)).append(", ");
        }

        pathString.append(nodesData.get(nodesData.size() - 1));

        return pathString.toString();
    }
    // #endregion

}
