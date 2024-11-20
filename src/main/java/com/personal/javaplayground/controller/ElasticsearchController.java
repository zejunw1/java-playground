package com.personal.javaplayground.controller;

import com.personal.javaplayground.models.ElasticData;
import com.personal.javaplayground.models.ElasticInsertRequest;
import com.personal.javaplayground.services.ElasticsearchService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AutoConfiguration
public class ElasticsearchController {
    private final ElasticsearchService elasticsearchService;

    public ElasticsearchController(ElasticsearchService elasticsearchService){
        this.elasticsearchService = elasticsearchService;

    }

    @RequestMapping(value = "/insert", produces = "application/json", method = RequestMethod.POST)
    public ElasticData insertData(@RequestBody ElasticInsertRequest request) throws Exception {
        return elasticsearchService.saveElasticData(request.id, request.path);
    }

    @PostMapping("/search")
    public ElasticData searchById(@RequestParam String id) {
        return elasticsearchService.getElasticData(id);
    }

}
