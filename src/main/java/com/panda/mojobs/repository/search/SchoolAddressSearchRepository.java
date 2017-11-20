package com.panda.mojobs.repository.search;

import com.panda.mojobs.domain.SchoolAddress;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SchoolAddress entity.
 */
public interface SchoolAddressSearchRepository extends ElasticsearchRepository<SchoolAddress, Long> {
}
