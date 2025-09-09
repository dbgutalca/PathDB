package com.gdblab.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gdblab.graph.base.Default;
import com.gdblab.graph.impl.Graph;
import com.gdblab.graph.interfaces.InterfaceGraph;
import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.Node;
import com.gdblab.response.ResponseDatabase;

@RestController
public class DatabaseController {

    @PostMapping("/database/upload/graph/{dbName}")
    public ResponseDatabase uploadNodesAndEdges(
        @PathVariable("dbName") String dbName,
        @RequestParam("nodeFile") MultipartFile nodeFile,
        @RequestParam("edgeFile") MultipartFile edgeFile
    ) {
        
        String nodesFileName = nodeFile.getOriginalFilename();
        String edgesFileName = edgeFile.getOriginalFilename();

        if ( nodesFileName == null || edgesFileName == null ) {
            ResponseDatabase response = new ResponseDatabase(false, "Must upload both files of nodes and edges.", null);
            return response;
        }

        if (!nodesFileName.endsWith(".pgdf") || !edgesFileName.endsWith(".pgdf")) {
            ResponseDatabase response = new ResponseDatabase(false, "Only files with the .pgdf extension are allowed.", null);
            return response;
        }

        try (
            BufferedReader readerNodes = new BufferedReader(
                new InputStreamReader(nodeFile.getInputStream(), StandardCharsets.UTF_8)
            )
        ) {
            ArrayList<String> schemaNodes = new ArrayList<>();
            String lineNodes;

            while ((lineNodes = readerNodes.readLine()) != null) {
                if (lineNodes.startsWith("@")) {
                    schemaNodes = new ArrayList<>(Arrays.asList(lineNodes.split("\\|")));
                } else {
                    ArrayList<String> data = new ArrayList<>(Arrays.asList(lineNodes.split("\\|")));
                    HashMap<String, String> properties = new HashMap<>();

                    for (int i = 2; i < data.size() && i < schemaNodes.size(); i++) {
                        properties.put(schemaNodes.get(i), data.get(i));
                    }

                    Node node = new Node(
                        data.get(0),
                        data.get(1),
                        properties
                    );
                    Graph.getGraph().insertNode(node);
                }
            }
        }
        catch (IOException e) {
            ResponseDatabase response = new ResponseDatabase(false, "There was an error uploading the node file. Please try again.", null);
            return response;
        }

        try (
            BufferedReader readerEdges = new BufferedReader(
                new InputStreamReader(edgeFile.getInputStream(), StandardCharsets.UTF_8)
            )
        ) {
            ArrayList<String> schemaEdges = new ArrayList<>();
            String lineNodes;

            while ((lineNodes = readerEdges.readLine()) != null) {
                if (lineNodes.startsWith("@")) {
                    schemaEdges = new ArrayList<>(Arrays.asList(lineNodes.split("\\|")));
                } else {
                    ArrayList<String> data = new ArrayList<>(Arrays.asList(lineNodes.split("\\|")));
                    HashMap<String, String> properties = new HashMap<>();

                    for (int i = 5; i < data.size() && i < schemaEdges.size(); i++) {
                        properties.put(schemaEdges.get(i), data.get(i));
                    }

                    Node source = Graph.getGraph().getNode(data.get(3));
                    Node target = Graph.getGraph().getNode(data.get(4));

                    Edge edge = new Edge (
                        data.get(0),
                        data.get(1),
                        source,
                        target,
                        properties
                    );
                    Graph.getGraph().insertEdge(edge);
                }
            }
        }
        catch (IOException e) {
            ResponseDatabase response = new ResponseDatabase(false, "There was an error uploading the edge file. Please try again.", null);
            return response;
        }

        ResponseDatabase response = new ResponseDatabase(true, "Graph successfully loaded", null);
        return response;
    }

    @PostMapping("/database/use/default")
    public ResponseDatabase useDefaultDatabase() {

        InterfaceGraph graph = Graph.getGraph();
        graph.cleanDatabase();

        Node[] nodes = Default.getDefaultNodes();
        for (Node n : nodes) { graph.insertNode(n); }

        Edge[] edges = Default.getDefaultEdges();
        for (Edge e : edges) { graph.insertEdge(e); }

        ResponseDatabase response = new ResponseDatabase(true, "Default graph successfully loaded", null);

        Graph.getGraph().setDatabaseLoaded(true);

        return response;
    }

    @PostMapping("/database/use/{dbName}")
    public void useLoadedDatabase() {
        
    }

    @GetMapping("/database/get/all")
    public void getAllDatabases() {

    }

    @GetMapping("/database/get/sample")
    public ResponseDatabase getSampleCurrentDatabase() {
        ArrayList<Edge> sample = Graph.getGraph().getSampleOfEachlabel();

        if (sample.isEmpty()) {
            ResponseDatabase response = new ResponseDatabase(false, "You have not selected a database, or the one you selected does not contain edges.", null);
            return response;
        }
        else {
            HashMap<String, Object> dataResponse = new HashMap<>();
            dataResponse.put("sample", sample);

            ResponseDatabase response = new ResponseDatabase(true, "Graph sample retrieved successfully", dataResponse);
            return response;
        }
    }

    @GetMapping("/database/get/information")
    public void getInformationCurrentDatabase() {

    }



}
