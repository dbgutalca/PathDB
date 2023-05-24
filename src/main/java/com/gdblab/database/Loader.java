/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.database;

import com.gdblab.schema.Edge;
import com.gdblab.schema.Graph;
import com.gdblab.schema.Node;
import com.gdblab.schema.Path;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author ramhg
 */
public class Loader {
    
    public static Graph LoadGraphFromFile(String rmatFile, String graphName){
        Graph g = new Graph(graphName);
        int edgeId =0;
        try {
            File myObj = new File(rmatFile);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String[] data = myReader.nextLine().split(" ");
                String sourceId = "n"+data[0];
                String targetId = "n"+data[1];
                Node source = g.getNode(sourceId);
                Node target = g.getNode(targetId);
                if(source == null){
                    source = new Node(sourceId,sourceId);
                    g.insertNode(sourceId, source);
                }
                if(target == null){
                    target = new Node(targetId,targetId);
                    g.insertNode(targetId, target);
                }
                if(g.getEdge(sourceId, targetId) == null){
                    g.insertEdge("e"+edgeId, new Edge("e"+edgeId,"e"+edgeId, source, target));
                    edgeId++;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return g;
    }
    
    public static Graph LoadEdgesAsPaths (Graph g){
        int pid= g.getCPaths().size();
        for (Edge e : g.getEdges()){
            String pathId = "p"+pid;
            Path p = new Path(pathId,pathId);
            p.insertEdge(e);
            g.insertPath(pathId, p);
            pid++;
        }
        return g;
    }
    
     
}
