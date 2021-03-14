package com.rtejada.logs.processor.logprocessor.repository;

import com.rtejada.logs.processor.logprocessor.model.Log;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface LogRepository extends ElasticsearchRepository<Log, String>, LogCustomRepository {

}
