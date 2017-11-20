package com.panda.mojobs.repository;

import com.panda.mojobs.domain.MojobImage;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MojobImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MojobImageRepository extends JpaRepository<MojobImage, Long> {

}
