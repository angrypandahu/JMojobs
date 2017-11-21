package com.panda.mojobs.repository.search;

import com.panda.mojobs.domain.Experience;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Experience entity.
 */
public interface ExperienceSearchRepository extends ElasticsearchRepository<Experience, Long> {
}
