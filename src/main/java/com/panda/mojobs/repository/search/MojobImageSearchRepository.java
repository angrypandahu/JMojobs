package com.panda.mojobs.repository.search;

import com.panda.mojobs.domain.MojobImage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MojobImage entity.
 */
public interface MojobImageSearchRepository extends ElasticsearchRepository<MojobImage, Long> {
}
