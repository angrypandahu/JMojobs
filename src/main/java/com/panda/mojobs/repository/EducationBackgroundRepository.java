package com.panda.mojobs.repository;

import com.panda.mojobs.domain.EducationBackground;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the EducationBackground entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EducationBackgroundRepository extends JpaRepository<EducationBackground, Long>, JpaSpecificationExecutor<EducationBackground> {

}
