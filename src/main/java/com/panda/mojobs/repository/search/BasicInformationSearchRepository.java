package com.panda.mojobs.repository.search;

import com.panda.mojobs.domain.BasicInformation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BasicInformation entity.
 */
public interface BasicInformationSearchRepository extends ElasticsearchRepository<BasicInformation, Long> {
}
