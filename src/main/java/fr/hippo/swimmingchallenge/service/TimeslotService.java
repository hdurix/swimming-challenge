package fr.hippo.swimmingchallenge.service;

import fr.hippo.swimmingchallenge.domain.Timeslot;
import fr.hippo.swimmingchallenge.repository.TimeslotRepository;
import fr.hippo.swimmingchallenge.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Timeslot.
 */
@Service
@Transactional
public class TimeslotService {

    private final Logger log = LoggerFactory.getLogger(TimeslotService.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private TimeslotRepository timeslotRepository;

    /**
     * Save a timeslot.
     * @return the persisted entity
     */
    public Timeslot save(Timeslot timeslot) {
        log.debug("Request to save Timeslot : {}", timeslot);
        if (timeslot.getUser().getId() != null) {
            timeslot.setUser(userRepository.findOne(timeslot.getUser().getId()));
        }
        Timeslot result = timeslotRepository.save(timeslot);
        return result;
    }

    /**
     * Save a timeslot.
     * @return the persisted entity
     */
    public void hasPay(Long userId) {
        log.debug("Request to set as pay {}", userId);
        List<Timeslot> timeslots = timeslotRepository.findByUserId(userId);
        timeslots.forEach(t -> t.setPayed(true));

        // envoi mail
    }

    /**
     *  get all the timeslots.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Timeslot> findAll() {
        log.debug("Request to get all Timeslots");
        List<Timeslot> result = timeslotRepository.findAll();
        return result;
    }

    /**
     *  get one timeslot by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Timeslot findOne(Long id) {
        log.debug("Request to get Timeslot : {}", id);
        Timeslot timeslot = timeslotRepository.findOne(id);
        return timeslot;
    }

    /**
     *  erase one timeslot by id.
     *  @return the entity
     */
    @Transactional
    public Timeslot eraseTimeslot(Long id) {
        log.debug("Request to erase Timeslot : {}", id);
        Timeslot timeslot = timeslotRepository.findOne(id);
        if (timeslot == null) {
            return null;
        }
        timeslot.erase();
        return timeslot;
    }
}
