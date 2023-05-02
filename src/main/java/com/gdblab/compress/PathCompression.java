/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.compress;

import com.gdblab.database.Database;
import com.gdblab.schema.Edge;
import com.gdblab.schema.Graph;
import com.gdblab.schema.GraphObject;
import com.gdblab.schema.Node;
import com.gdblab.schema.Path;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author ramhg
 */
public class PathCompression {
    
    public static Path compressPath (ArrayList<Path> s, Path path, Graph g){
        if (s.isEmpty())
            return path;
        
        for (Path p : s){
            if(p.lenght()< path.lenght()){
                
            }
                
        }
    
        return path;
    }
    
    public static Path comparePath (Path epath, Path npath , Graph g){
        int nodesLenght = npath.getNodeNumber();
        for (int i = 0 ; i< nodesLenght ;i++){
            for (int j = i+1 ; j< nodesLenght ;j++){
                Path sp = subPath(npath,i, j,g);
                if (epath.getSequence().equals(sp.getSequence())){
                    int min = 0;
                    if(i == 0){
                        ArrayList<GraphObject> npathSequence = (ArrayList<GraphObject>) npath.SubPath(j, nodesLenght).getSequence().clone();
                        ArrayList<GraphObject> compPathSequence = new ArrayList<>();
                        compPathSequence.add(epath);
                        compPathSequence.addAll(npathSequence.subList(1, npathSequence.size()-1));
                        npath.setSequence(compPathSequence);
                        return npath;
                    }
                            //Faltan casos intermedios
                }
            }
        }    
        return npath;
    }
    
    public static Path pathReconstruct (Path p, Graph g){
        ArrayList<GraphObject> seq = (ArrayList<GraphObject>) p.getSequence().clone();
        Boolean isReconstructed = false;
        int i =0;
        while (!isReconstructed){
            GraphObject go = seq.get(i);
            if(go.getId().contains("p")){
                ArrayList<GraphObject> seq2 = g.getCPath(go.getId()).getSequence();
                seq = pathSeqUnion(seq,seq2,i);
            }
            else{
                i++;
                if(seq.size() == i)
                    isReconstructed = true;
            }
        }
        p.setSequence(seq);
        return p;
    }
    
    public static ArrayList<GraphObject> pathSeqUnion (ArrayList<GraphObject> seq, ArrayList<GraphObject> seq2, int i){
        if(i==0){
            ArrayList<GraphObject> subSeq = (ArrayList<GraphObject>) seq.subList(i, seq.size()-1);
            seq2.addAll(subSeq);
            return seq2;
        }
        else if (i>0 && i < seq.size()-1){
            ArrayList<GraphObject> subSeqL = (ArrayList<GraphObject>) seq.subList(0, i-1);
            ArrayList<GraphObject> subSeqR = (ArrayList<GraphObject>) seq.subList(i+1, seq.size()-1);
            subSeqL.addAll(seq2);
            seq2.addAll(subSeqR);
            return seq2;
        }
        else{
            ArrayList<GraphObject> subSeqL = (ArrayList<GraphObject>) seq.subList(0, i-1);
            subSeqL.addAll(seq2);
            return subSeqL;
        }
    }
    
             
    public static Path subPath (Path p, int source, int target, Graph g){
        if (p.getSequence().size()<target && source >=0){
             if(source % 2 == 0 &&  target % 2 == 0){
                Node first;
                Node last;
                Path new_path = new Path("p"+UUID.randomUUID().toString(), "path");
                ArrayList<GraphObject> seq = (ArrayList<GraphObject>) p.getSequence().subList(source, target);
                //Se reestablece el primer nodo
                if (seq.get(0).getId().contains("n"))
                    first = g.getNode(seq.get(0).getId());
                else
                    first = g.getCPath(seq.get(0).getId()).First();
                
                //OJO, el primero en un camino podría ser otro camino OJO, quizas hay que recomponer el camino cada vez para obtener el primer nodo?
                
                  if (seq.get(seq.size()-1).getId().contains("n"))
                    last = g.getNode(seq.get(0).getId());
                else
                    last = g.getCPath(seq.get(seq.size()-1).getId()).Last();
                
                new_path.setSource(first);
                new_path.setTarget(last);
                new_path.setSequence(seq);
                return new_path;
             }
        }
        return p;
    }
    
    
}
