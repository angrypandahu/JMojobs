package com.panda.mojobs.repository.search;

import com.panda.mojobs.domain.ProfessionalDevelopment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProfessionalDevelopment entity.
 */
public interface ProfessionalDevelopmentSearchRepository extends ElasticsearchRepository<ProfessionalDevelopment, Long> {
}
