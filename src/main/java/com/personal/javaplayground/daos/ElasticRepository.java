package com.personal.javaplayground.daos;

import com.personal.javaplayground.models.ElasticData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticRepository extends ElasticsearchRepository<ElasticData, String> {
}