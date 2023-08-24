/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdblab.database;

import com.gdblab.algebra.condition.Not;
import com.gdblab.algebra.condition.First;
import com.gdblab.algebra.Select;
import com.gdblab.algebra.PathAlgebra;
import com.gdblab.algebra.condition.Label;
import com.gdblab.recursion.Recursion;
import com.gdblab.schema.Edge;
import com.gdblab.schema.Graph;
import com.gdblab.schema.GraphObject;
import com.gdblab.schema.Node;
import com.gdblab.schema.Path;
import java.util.ArrayList;

/**
 *
 * @author ramhg
 */
public class Database {
    
    public final Graph graph;
    private ArrayList<Path> pathsWithoutEdges;
    private final PathAlgebra algebra;

    public Database() {
        
        this.graph = new Graph("|---- Graph 1 ----|");
        this.pathsWithoutEdges = new ArrayList<>();

        generateDemoDatabase(this.graph);

        
        System.out.println(graph.getName());
        this.algebra = new PathAlgebra(graph);
        
        // ArrayList<Path> p= algebra.NodeJoin(graph.getPaths(), graph.getPaths());
        // ArrayList<Path> paths2 = algebra.RightSubPaths(p);
        // System.out.println(""); 
        
       //ArrayList<Path> paths = Select.eval(paths2, new Not(new First("n1")));
        
        // ArrayList<Path> paths = Select.eval(graph.getPaths(), new Label("a", 1));
        
        // System.out.println("Paths with label 'a'");
        // printPath(paths);
       
        // System.out.println("Arbitrary");
        // printPath(Recursion.arbitrary(paths, 10)); 
        // System.out.println(".................................................");
        // System.out.println("No repeated nodes");
        // printPath(Recursion.noRepeatedNodes(paths, 10));
        // System.out.println(".................................................");
        // System.out.println("No repeated edges");
        // printPath(Recursion.noRepeatedEdges(paths, 10));
        // System.out.println(".................................................");
        // System.out.println("Shortest Paths");
        // printPath(Recursion.shortestPath(paths, 10));
            
          
    }
    
    private void printPath(ArrayList<Path> paths){
         for(Path pp : paths){
            System.out.print(pp.getId()+" : ");
            for (GraphObject go : pp.getSequence())
              System.out.print(go.getId()+" ");
            System.out.println("");
        }
    }
    
   // generate Get function for pathsWithoutEdges
    public ArrayList<Path> getPathsWithoutEdges(){
         return this.pathsWithoutEdges;
    }
    
    private void generateDemoDatabase(Graph graph){
        
        // Creation of nodes
        Node node1 = new Node("n1", "Node");
        Node node2 = new Node("n2", "Node");
        Node node3 = new Node("n3", "Node");
        Node node4 = new Node("n4", "Node");
        Node node5 = new Node("n5", "Node");

        // Creation of edges
        Edge edge1 = new Edge("e1", "c", node1, node1);
        Edge edge2 = new Edge("e2", "b", node1, node2);
        Edge edge3 = new Edge("e3", "a", node2, node3);
        Edge edge5 = new Edge("e4", "a", node2, node5);
        Edge edge6 = new Edge("e5", "b", node2, node5);
        Edge edge7 = new Edge("e6", "b", node3, node3);
        Edge edge4 = new Edge("e7", "c", node4, node2);
        Edge edge8 = new Edge("e8", "d", node3, node4);
        Edge edge9 = new Edge("e9", "b", node5, node4);
        
        // Insert Nodes and Edges
        graph.insertNode("n1", node1);
        graph.insertNode("n2", node2);
        graph.insertNode("n3", node3);
        graph.insertNode("n4", node4);
        graph.insertNode("n5", node5);
        
        graph.insertEdge("e1", edge1);
        graph.insertEdge("e2", edge2);
        graph.insertEdge("e3", edge3);
        graph.insertEdge("e4", edge4);
        graph.insertEdge("e5", edge5);
        graph.insertEdge("e6", edge6);
        graph.insertEdge("e7", edge7);
        graph.insertEdge("e8", edge8);
        graph.insertEdge("e9", edge9);
        
        // Creation of Paths and insertion of edges to each path
        Path path1 = new Path("p1", "path1");
        path1.insertEdge(edge1);

        Path path2 = new Path("p2", "path2");
        path2.insertEdge(edge2);

        Path path3 = new Path("p3", "path3");
        path3.insertEdge(edge3);
        
        Path path4 = new Path("p4", "path4");
        path4.insertEdge(edge4);
        
        Path path5 = new Path("p5", "path5");
        path5.insertEdge(edge5);
        
        Path path6 = new Path("p6", "path6");
        path6.insertEdge(edge6);
        
        Path path7 = new Path("p7", "path7");
        path7.insertEdge(edge7);
        
        Path path8 = new Path("p8", "path8");
        path8.insertEdge(edge8);
        
        Path path9 = new Path("p9", "path9");
        path9.insertEdge(edge9);
        
        // Insert Paths to the graph
        graph.insertPath("p1", path1);
        graph.insertPath("p2", path2);
        graph.insertPath("p3", path3);
        graph.insertPath("p4", path4);
        graph.insertPath("p5", path5);
        graph.insertPath("p6", path6);
        graph.insertPath("p7", path7);
        graph.insertPath("p8", path8);
        graph.insertPath("p9", path9);
        
        // Create paths without edges to test the algebra
        Path pathwoe1 = new Path("pwoe1", "pathwoe1");
        pathwoe1.insertNode(node1);
        this.pathsWithoutEdges.add(pathwoe1);

        Path pathwoe2 = new Path("pwoe2", "pathwoe2");
        pathwoe2.insertNode(node2);
        this.pathsWithoutEdges.add(pathwoe2);

        Path pathwoe3 = new Path("pwoe3", "pathwoe3");
        pathwoe3.insertNode(node3);
        this.pathsWithoutEdges.add(pathwoe3);

        Path pathwoe4 = new Path("pwoe4", "pathwoe4");
        pathwoe4.insertNode(node4);
        this.pathsWithoutEdges.add(pathwoe4);

        Path pathwoe5 = new Path("pwoe5", "pathwoe5");
        pathwoe5.insertNode(node5);
        this.pathsWithoutEdges.add(pathwoe5);
    }
   
   
   
   
    
}
