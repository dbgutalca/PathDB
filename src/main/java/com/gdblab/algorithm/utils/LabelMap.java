package com.gdblab.algorithm.utils;

import java.util.HashMap;

/**
 * This class is used to map labels to their respective ASCII characters.
 */
public final class LabelMap {

    private static Integer initAscii = 97;
    
    /**
     * This map is used to store the labels and their respective ASCII characters.
     */
    private static HashMap<String, String> labelMap = new HashMap<>(); // <label, ascii>
    
    /**
     * This method is used to put a label and its respective ASCII character in the map.
     * 
     * @param label The label to be put in the map.
     * @param ascii The ASCII character to be put in the map.
     */
    public static void put(String label){
        if (!labelMap.containsKey(label)) {
            labelMap.put(label, String.valueOf((char) initAscii.intValue()));
            increaseAscii();
        }
    }

    /**
     * This method is used to get the ASCII character of a label.
     * 
     * @param label The label to get the ASCII character.
     * @return The ASCII character of the label.
     */
    public static String getByLabel(String label){
        return labelMap.get(label);
    }

    /**
     * This method is used to get the label of an ASCII character.
     * 
     * @param ascii The ASCII character to get the label.
     * @return The label of the ASCII character if it exists, otherwise null.
     */
    public static String getByAscii(String ascii){
        for (String key : labelMap.keySet()) {
            if (labelMap.get(key).equals(ascii)) {
                return key;
            }
        }
        return null;
    }

    public static boolean containsKey(String key){
        return labelMap.containsKey(key);
    }

    public static boolean containsValue(String value){
        return labelMap.containsValue(value);
    }

    private static void increaseAscii(){
        initAscii++;
    }
}
