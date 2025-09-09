package com.gdblab.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gdblab.execution.Context;
import com.gdblab.execution.Execute;
import com.gdblab.graph.impl.Graph;
import com.gdblab.response.ResponseQuery;

@RestController
public class QueryController {
    
    @PostMapping("/query")
    public ResponseEntity<ResponseQuery> runQuery(@RequestBody String pathDBQuery) {
        Context.getInstance().setCompleteQuery(pathDBQuery);

        if (Graph.getGraph().isDatabaseLoaded()) {
            ResponseQuery responseQuery = Execute.EvalRPQWithAlgebra();
            return ResponseEntity.ok(responseQuery);
        }
        else {
            ResponseQuery responseQuery = new ResponseQuery();
            responseQuery.setMessage("Before running a query, you must select a database.");
            return ResponseEntity.ok(responseQuery);
        }
    }
}
