package com.gdblab.graph;

public final class DefaultGraph {

    public static String[] getDefaultStringNodes() {
        String[] nodes = new String[7];

        nodes[0] = "id:m1|label:Message|txt:Msg1";
        nodes[1] = "id:m2|label:Message|txt:Msg2";
        nodes[2] = "id:m3|label:Message|txt:Msg3";
        nodes[3] = "id:p1|label:Person|name:Moe";
        nodes[4] = "id:p2|label:Person|name:Bart";
        nodes[5] = "id:p3|label:Person|name:Lisa";
        nodes[6] = "id:p4|label:Person|name:Apu";

        return nodes;
    }

    public static String[] getDefaultStringEdges() {
        String[] edges = new String[11];

        edges[0] = "id:E1|label:Knows|dir:T|source:p1|target:p2";
        edges[1] = "id:E2|label:Knows|dir:T|source:p2|target:p3";
        edges[2] = "id:E3|label:Knows|dir:T|source:p3|target:p2";
        edges[3] = "id:E4|label:Knows|dir:T|source:p2|target:p4";
        edges[4] = "id:E5|label:Likes|dir:T|source:p2|target:m3";
        edges[5] = "id:E6|label:Likes|dir:T|source:p4|target:m3";
        edges[6] = "id:E7|label:Likes|dir:T|source:p3|target:m2";
        edges[7] = "id:E8|label:Likes|dir:T|source:p1|target:m1";
        edges[8] = "id:E9|label:HasCreator|dir:T|source:m3|target:p1";
        edges[9] = "id:E10|label:HasCreator|dir:T|source:m2|target:p4";
        edges[10] = "id:E11|label:HasCreator|dir:T|source:m1|target:p3";

        return edges;
    }

}
