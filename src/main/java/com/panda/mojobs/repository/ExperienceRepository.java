package com.panda.mojobs.repository;

import com.panda.mojobs.domain.Experience;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Experience entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {

}
