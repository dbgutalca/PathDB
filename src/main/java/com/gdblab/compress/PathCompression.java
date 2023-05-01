/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.compress;

import com.gdblab.schema.GraphObject;
import com.gdblab.schema.Path;
import java.util.ArrayList;

/**
 *
 * @author ramhg
 */
public class PathCompression {
    
    public static Path compressPath (ArrayList<Path> s, Path path){
        if (s.isEmpty())
            return path;
        
        for (Path p : s){
            if(p.lenght()< path.lenght()){
                
            }
                
        }
    
        return path;
    }
    
    public static Path comparePath (Path epath, Path npath){
        int nodesLenght = npath.getNodeNumber();
        for (int i = 0 ; i< nodesLenght ;i++){
            for (int j = i+1 ; j< nodesLenght ;j++){
                Path sp = npath.SubPath(i, j);
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
    
    public static Path pathReconstruct (Path p){
        
    }
    
    
}
