package com.panda.mojobs.repository.search;

import com.panda.mojobs.domain.JobSubType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the JobSubType entity.
 */
public interface JobSubTypeSearchRepository extends ElasticsearchRepository<JobSubType, Long> {
}
