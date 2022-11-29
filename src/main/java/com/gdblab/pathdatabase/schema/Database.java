/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdblab.pathdatabase.schema;

/**
 *
 * @author ramhg
 */
public class Database {
    private Graph graph;

    public Database() {
        graph = new Graph("test1");
        GenerateDemoDatabase(this.graph);
        System.out.println(graph.getName());
        GetEdgeX(graph.getPath("p1"), 0);
        
        
    }
    
    public Node First (Path p){
        return p.getFirst_next().getSource();
    }
    public Node Last (Path p){
        Next next = p.getFirst_next();
        while (next.getNext() !=null)
            next = next.getNext();
        return next.getTarget();
    }
    
    public Node GetNodeX (Path p, int pos){
        int i=0;
        Node node = null;
        Next next = p.getFirst_next();
        while (i < pos-1){
            if (next != null)
                next = next.getNext();
            else
                i = pos;
            i++;
        }
        if (next !=null){
            if(i == pos)
                node = next.getSource();
            else
                node = next.getTarget();
        }
        return node;
    }
    public Edge GetEdgeX (Path p, int pos){
        int i=0;
        Edge edge = null;
        Next next = p.getFirst_next();
        while (i < pos){
            if (next != null)
                next = next.getNext();
            else
                i = pos;
            i++;
        }
        if (next !=null){
            edge = next.getEdge();
        }
        return edge;
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

        Next next1 = new Next("nxt1", node1, edge1, node2, null);
        Next next2 = new Next("nxt2", node2, edge2, node3, null);
        Next next3 = new Next("nxt4", node4, edge3, node4, null);
        
        graph.insertNode("n1", node1);
        graph.insertNode("n2", node2);
        graph.insertNode("n3", node3);
        graph.insertNode("n4", node4);
        
        graph.insertEdge("e1", edge1);
        graph.insertEdge("e2", edge2);
        graph.insertEdge("e3", edge3);
        graph.insertEdge("e4", edge4);
        
        
        Path path1 = new Path("p1", "path1",next1,null);
        
        Path path2 = new Path("p2", "path2",next2,null);
        
        Path path3 = new Path("p3", "path3",next3,null);
        
        graph.insertPath("p1", path1);
        graph.insertPath("p2", path2);
        graph.insertPath("p3", path3);
        
        
    }
   
   
   
   
    
}
