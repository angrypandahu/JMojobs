package com.panda.mojobs.repository;

import com.panda.mojobs.domain.Town;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Town entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TownRepository extends JpaRepository<Town, Long> {

}
