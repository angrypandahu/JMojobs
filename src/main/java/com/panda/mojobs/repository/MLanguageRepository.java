package com.panda.mojobs.repository;

import com.panda.mojobs.domain.MLanguage;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MLanguage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MLanguageRepository extends JpaRepository<MLanguage, Long>, JpaSpecificationExecutor<MLanguage> {

}
