package fr.hippo.swimmingchallenge.repository;

import fr.hippo.swimmingchallenge.domain.Timeslot;

import fr.hippo.swimmingchallenge.domain.User;
import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the Timeslot entity.
 */
public interface TimeslotRepository extends JpaRepository<Timeslot,Long> {

    @Query("select timeslot from Timeslot timeslot " +
        "left join fetch timeslot.user user " +
        "where user.login = ?#{principal.username} " +
        "and timeslot.payed = FALSE")
    List<Timeslot> findByUserIsCurrentUserAndIsNotPayed();

    @Query("from Timeslot t " +
        "left join fetch t.user " +
        "where t.id = ?1")
    Timeslot findOneWithUser(Long id);

    List<Timeslot> findByReservedIsTrueAndPayedIsFalseAndReservedDateBefore(LocalDate reservedDate);
}
