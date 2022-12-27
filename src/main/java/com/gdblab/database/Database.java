/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdblab.database;

import com.gdblab.algebra.PathAlgebra;
import com.gdblab.algebra.select.*;
import com.gdblab.schema.Edge;
import com.gdblab.schema.Graph;
import com.gdblab.schema.Node;
import com.gdblab.schema.Path;
import java.util.ArrayList;

/**
 *
 * @author ramhg
 */
public class Database {
    private final Graph graph;
    private final PathAlgebra algebra;
    public Database() {
        graph = new Graph("test1");
        GenerateDemoDatabase(this.graph);
        System.out.println(graph.getName());
        this.algebra = new PathAlgebra(graph);
        
        ArrayList<Path> p= algebra.NodeJoin(graph.getPaths(), graph.getPaths());
        ArrayList<Path> paths = algebra.RightSubPaths(p);
        System.out.println(""); 
        
        paths = Select.eval(graph.getPaths(), new Not(new First("n1")));
        
        for(Path pp : paths)
            System.out.println(pp.getLabel());
          
    }
    
   
        
    
    private void GenerateDemoDatabase(Graph graph){
        Node node1 = new Node("n1", "Node");
        Node node2 = new Node("n2", "Node");
        Node node3 = new Node("n3", "Node");
        Node node4 = new Node("n4", "Node");
        
        Edge edge1 = new Edge("e1", "a", node1, node2);
        Edge edge2 = new Edge("e2", "a", node2, node3);
        Edge edge3 = new Edge("e3", "b", node3, node4);
        Edge edge4 = new Edge("e4", "c", node1, node4);
        
        graph.insertNode("n1", node1);
        graph.insertNode("n2", node2);
        graph.insertNode("n3", node3);
        graph.insertNode("n4", node4);
        
        graph.insertEdge("e1", edge1);
        graph.insertEdge("e2", edge2);
        graph.insertEdge("e3", edge3);
        graph.insertEdge("e4", edge4);
        
        
        Path path1 = new Path("p1", "path1");
        path1.insertEdge(edge1);
        
        Path path2 = new Path("p2", "path2");
        path2.insertEdge(edge2);
        
        Path path3 = new Path("p3", "path3");
        path3.insertEdge(edge3);
        
        Path path4 = new Path("p4", "path4");
        path4.insertEdge(edge4);
        
        graph.insertPath("p1", path1);
        graph.insertPath("p2", path2);
        graph.insertPath("p3", path3);
        graph.insertPath("p4", path4);
        
    }
   
   
   
   
    
}
