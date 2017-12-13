package com.panda.mojobs.repository;

import com.panda.mojobs.domain.ProfessionalDevelopment;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProfessionalDevelopment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfessionalDevelopmentRepository extends JpaRepository<ProfessionalDevelopment, Long>, JpaSpecificationExecutor<ProfessionalDevelopment> {

}
