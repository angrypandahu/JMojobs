package com.panda.mojobs.repository.search;

import com.panda.mojobs.domain.School;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the School entity.
 */
public interface SchoolSearchRepository extends ElasticsearchRepository<School, Long> {
}
