package com.panda.mojobs.repository.search;

import com.panda.mojobs.domain.JobAddress;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the JobAddress entity.
 */
public interface JobAddressSearchRepository extends ElasticsearchRepository<JobAddress, Long> {
}
