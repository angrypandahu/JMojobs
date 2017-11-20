package com.panda.mojobs.repository.search;

import com.panda.mojobs.domain.Province;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Province entity.
 */
public interface ProvinceSearchRepository extends ElasticsearchRepository<Province, Long> {
}
