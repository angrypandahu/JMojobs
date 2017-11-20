package com.panda.mojobs.repository.search;

import com.panda.mojobs.domain.ApplyJobResume;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ApplyJobResume entity.
 */
public interface ApplyJobResumeSearchRepository extends ElasticsearchRepository<ApplyJobResume, Long> {
}
