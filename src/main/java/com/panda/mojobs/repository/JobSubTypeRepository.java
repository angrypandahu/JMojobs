package com.panda.mojobs.repository;

import com.panda.mojobs.domain.JobSubType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the JobSubType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobSubTypeRepository extends JpaRepository<JobSubType, Long>, JpaSpecificationExecutor<JobSubType> {

}
