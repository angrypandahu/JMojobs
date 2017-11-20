package com.panda.mojobs.repository;

import com.panda.mojobs.domain.SchoolAddress;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SchoolAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchoolAddressRepository extends JpaRepository<SchoolAddress, Long> {

}
