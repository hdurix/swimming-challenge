package fr.hippo.swimmingchallenge.repository;

import fr.hippo.swimmingchallenge.domain.Timeslot;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Timeslot entity.
 */
public interface TimeslotRepository extends JpaRepository<Timeslot,Long> {

    @Query("select timeslot from Timeslot timeslot where timeslot.user.login = ?#{principal.username}")
    List<Timeslot> findByUserIsCurrentUser();

    List<Timeslot> findByUserId(Long id);

}
