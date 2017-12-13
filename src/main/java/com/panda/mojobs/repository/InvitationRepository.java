package com.panda.mojobs.repository;

import com.panda.mojobs.domain.Invitation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Invitation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long>, JpaSpecificationExecutor<Invitation> {

    @Query("select invitation from Invitation invitation where invitation.user.login = ?#{principal.username}")
    List<Invitation> findByUserIsCurrentUser();

}
