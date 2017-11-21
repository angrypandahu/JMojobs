package com.panda.mojobs.repository.search;

import com.panda.mojobs.domain.EducationBackground;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the EducationBackground entity.
 */
public interface EducationBackgroundSearchRepository extends ElasticsearchRepository<EducationBackground, Long> {
}
