package com.panda.mojobs.repository.search;

import com.panda.mojobs.domain.Invitation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Invitation entity.
 */
public interface InvitationSearchRepository extends ElasticsearchRepository<Invitation, Long> {
}
