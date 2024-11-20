package com.personal.javaplayground.services;

import com.personal.javaplayground.daos.ElasticRepository;
import com.personal.javaplayground.models.ElasticData;
import org.springframework.stereotype.Service;


@Service
public class ElasticsearchService {
    private final ElasticRepository elasticRepository;
    private final S3Service s3Service;

    public ElasticsearchService(ElasticRepository elasticRepository, S3Service s3Service) {
        this.elasticRepository = elasticRepository;
        this.s3Service = s3Service;
    }

    public ElasticData saveElasticData(String id, String rawFileString) throws Exception {
        var fileName = s3Service.uploadJsonToS3(rawFileString);
        ElasticData elasticData = new ElasticData();
        elasticData.setId(id);
        elasticData.setPath(fileName);
        return this.elasticRepository.save(elasticData);
    }

    public ElasticData getElasticData(String id) {
        var result = this.elasticRepository.findById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("Data with id " + id + " does not exist");// Could have used a custom exception
        }
        return result.get();
    }

}
