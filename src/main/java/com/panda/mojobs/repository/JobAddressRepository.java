package com.panda.mojobs.repository;

import com.panda.mojobs.domain.JobAddress;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the JobAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobAddressRepository extends JpaRepository<JobAddress, Long> {

}
