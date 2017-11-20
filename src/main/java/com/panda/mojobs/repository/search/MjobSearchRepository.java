package com.panda.mojobs.repository.search;

import com.panda.mojobs.domain.Mjob;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Mjob entity.
 */
public interface MjobSearchRepository extends ElasticsearchRepository<Mjob, Long> {
}
