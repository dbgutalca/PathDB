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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.HashSet;

/**
 *
 * @author ramhg
 */
public class Database {
    
    public final Graph graph;
    private ArrayList<Path> pathsWithoutEdges;
    private final PathAlgebra algebra;

    public Database(String url) {
        
        this.graph = new Graph("|---- Graph 1 ----|");
        this.pathsWithoutEdges = new ArrayList<>();

        generateDemoDatabase(this.graph, url);

        
        // System.out.println(graph.getName());
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
    
    private void generateDemoDatabase(Graph graph, String url){
        ArrayList<String> lines = new ArrayList<>();

        try {
            File myObj = new File(url);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                lines.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        Set<String> nodes = new HashSet<String>();
        nodes = getUniqueNodes(lines);

        int j = 1;
        for (String n : nodes) {
            Node node = new Node(n, "Node");
            graph.insertNode(n, node);

            Path p = new Path(UUID.randomUUID().toString(), "pathwoe" + j);
            p.insertNode(node);
            this.pathsWithoutEdges.add(p);
            j++;
        }

        int i = 1;
        for (String line : lines) {
            String[] parts = line.split(",");
            String source = parts[0];
            String label = parts[1];
            String target = parts[2];
            Edge edge = new Edge("e" + i, label, graph.getNode(source), graph.getNode(target));
            graph.insertEdge("e" + i, edge);
            Path p = new Path(UUID.randomUUID().toString(), "path" + i);
            p.insertEdge(edge);
            graph.insertPath(p.getId(), p);
            i++;
        }
    }
   
    public Set<String> getUniqueNodes(ArrayList<String> lines){
        Set<String> nodes = new HashSet<String>();
        for (String line : lines) {
            String[] parts = line.split(",");
            String source = parts[0];
            String label = parts[1];
            String target = parts[2];
            nodes.add(source);
            nodes.add(target);
        }
        return nodes;
    }
   
   
    
}
