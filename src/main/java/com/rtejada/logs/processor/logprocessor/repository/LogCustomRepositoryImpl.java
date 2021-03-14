package com.rtejada.logs.processor.logprocessor.repository;

import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

import com.rtejada.logs.processor.logprocessor.model.Log;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

public class LogCustomRepositoryImpl implements LogCustomRepository {

  private static final String INDEX = "logs";
  private static final String PATH_FIELD = "UrlPath";
  private static final String DATE_FIELD = "Timestamp";
  private static final String CALLER_COUNTRY_FIELD = "CallerCountry";
  private static final Integer AGGREGATION_LIMIT = 5;
  private static final Integer MOST_HITS_ONLY = 1;

  private ElasticsearchOperations elasticsearchOperations;

  public LogCustomRepositoryImpl(ElasticsearchOperations elasticsearchOperations) {
    this.elasticsearchOperations = elasticsearchOperations;
  }

  @Override
  public Map<String, Long> getTopPagesByCountry(String country) {
    Query query = new NativeSearchQueryBuilder()
        .addAggregation(terms(PATH_FIELD).field(PATH_FIELD + ".keyword").size(AGGREGATION_LIMIT))
        .withQuery(QueryBuilders.matchQuery(CALLER_COUNTRY_FIELD, country))
        .build();

    SearchHits<Log> searchHits = elasticsearchOperations.search(query, Log.class,
                                                                IndexCoordinates.of(INDEX));

    ParsedStringTerms topTags = (ParsedStringTerms) Objects
        .requireNonNull(searchHits.getAggregations()).asMap().get(PATH_FIELD);

    return topTags.getBuckets()
        .stream()
        .collect(Collectors.toMap(Bucket::getKeyAsString,
                                  Bucket::getDocCount,
                                  (v1, v2) -> v1,
                                  LinkedHashMap::new));
  }

  @Override
  public Map<String, Long> getTopPages() {
    Query query = new NativeSearchQueryBuilder()
        .addAggregation(terms(PATH_FIELD).field(PATH_FIELD + ".keyword").size(AGGREGATION_LIMIT))
        .build();

    SearchHits<Log> searchHits = elasticsearchOperations.search(query, Log.class,
                                                                IndexCoordinates.of(INDEX));

    ParsedStringTerms topTags = (ParsedStringTerms) Objects
        .requireNonNull(searchHits.getAggregations()).asMap().get(PATH_FIELD);

    return topTags.getBuckets()
        .stream()
        .collect(Collectors.toMap(Bucket::getKeyAsString,
                                  Bucket::getDocCount,
                                  (v1, v2) -> v1,
                                  LinkedHashMap::new));
  }

  @Override
  public Long getTopTimestamp() {
    Query query = new NativeSearchQueryBuilder()
        .addAggregation(terms(DATE_FIELD).field(DATE_FIELD).size(MOST_HITS_ONLY))
        .build();

    SearchHits<Log> searchHits = elasticsearchOperations.search(query, Log.class,
                                                                IndexCoordinates.of(INDEX));

    ParsedLongTerms topTags = (ParsedLongTerms) Objects
        .requireNonNull(searchHits.getAggregations()).asMap().get(DATE_FIELD);

    return (Long) topTags.getBuckets()
        .stream()
        .map(Bucket::getKey)
        .collect(Collectors.toList()).get(0);
  }

  @Override
  public List<String> getPagesBetweenDates(long millisecondsStart, long millisecondsEnd) {

    Criteria criteria = new Criteria(DATE_FIELD)
        .greaterThan(millisecondsStart)
        .lessThan(millisecondsEnd);

    Query searchQuery = new CriteriaQuery(criteria);

    return elasticsearchOperations
        .search(searchQuery,
                Log.class,
                IndexCoordinates.of(INDEX)).getSearchHits().stream()
        .map(logSearchHit -> logSearchHit.getContent().getUrlPath())
        .collect(Collectors.toList());
  }
}
