package com.panda.mojobs.repository;

import com.panda.mojobs.domain.ApplyJobResume;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ApplyJobResume entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplyJobResumeRepository extends JpaRepository<ApplyJobResume, Long> {

}
