package com.panda.mojobs.repository.search;

import com.panda.mojobs.domain.Image;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Image entity.
 */
public interface ImageSearchRepository extends ElasticsearchRepository<Image, Long> {
}
