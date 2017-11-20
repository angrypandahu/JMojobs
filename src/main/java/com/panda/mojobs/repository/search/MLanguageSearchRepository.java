package com.panda.mojobs.repository.search;

import com.panda.mojobs.domain.MLanguage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MLanguage entity.
 */
public interface MLanguageSearchRepository extends ElasticsearchRepository<MLanguage, Long> {
}
