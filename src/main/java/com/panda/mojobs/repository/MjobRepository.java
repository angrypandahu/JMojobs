package com.panda.mojobs.repository;

import com.panda.mojobs.domain.Mjob;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Mjob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MjobRepository extends JpaRepository<Mjob, Long>, JpaSpecificationExecutor<Mjob> {

}
