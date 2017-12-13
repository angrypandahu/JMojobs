package com.panda.mojobs.repository;

import com.panda.mojobs.domain.Resume;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Resume entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long>, JpaSpecificationExecutor<Resume> {

    @Query("select resume from Resume resume where resume.user.login = ?#{principal.username}")
    List<Resume> findByUserIsCurrentUser();

}
