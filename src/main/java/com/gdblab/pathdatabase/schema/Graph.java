/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdblab.pathdatabase.schema;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author ramhg
 */
public class Graph {
    private String name;
    private HashMap<String,GraphObject> nodes;
    private HashMap<String,GraphObject> edges;
    private HashMap<String,GraphObject> paths;

    public Graph(String name) {
        this.name = name;
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
        this.paths = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getNode(String id) {
        return (Node) nodes.get(id);
    }

    public void setNode(String id, GraphObject node) {
        nodes.remove(id);
        insertNode(id, node);
    }
    
    public void insertNode(String id, GraphObject node) {
        nodes.put(id,node);
    }
    
    public Edge getEdge(String id) {
        return (Edge) edges.get(id);
    }
    
     public Edge getEdge(String source_id, String target_id) {
        for (GraphObject go : edges.values()){
            Edge edge = (Edge)go;
            if (edge.getSource().getId().equals(source_id) && edge.getTarget().getId().equals(target_id))
                return edge;
        }
         
        return null;
    }

    public void setEdge(String id, GraphObject edge) {
        edges.remove(id);
        insertEdge(id, edge);
    }
    
    public void insertEdge(String id, GraphObject edge) {
        edges.put(id,edge);
    }
    
    public Path getPath(String id) {
        return (Path) paths.get(id);
    }
    public ArrayList<GraphObject> getPaths() {
        return new ArrayList<>(paths.values());
    }

    public void setPath(String id, GraphObject path) {
        paths.remove(id);
        insertPath(id, path);
    }
    
    public void insertPath(String id, GraphObject path) {
        paths.put(id,path);
    }

   
    
    
    
    
}
