package fr.hippo.swimmingchallenge.service;

import fr.hippo.swimmingchallenge.domain.Timeslot;
import fr.hippo.swimmingchallenge.domain.User;
import fr.hippo.swimmingchallenge.repository.TimeslotRepository;
import fr.hippo.swimmingchallenge.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Timeslot.
 */
@Service
@Transactional
public class TimeslotService {

    private static final Integer NB_DAYS_BEFORE_REMINDER = 7;

    private final Logger log = LoggerFactory.getLogger(TimeslotService.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private TimeslotRepository timeslotRepository;

    @Inject
    private MailService mailService;

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
        return findOne(result.getId());
    }

    /**
     * Save a timeslot.
     * @return the persisted entity
     */
    public void hasPay(Long userId) {
        log.debug("Request to set as pay {}", userId);
        User user = userRepository.findOne(userId);
        Set<Timeslot> timeslots = user.getTimeslots();
        timeslots.forEach(t -> t.setPayed(true));
        List<Timeslot> sortedTimeslots = timeslots.stream()
            .sorted(Comparator.comparing(Timeslot::getStartTime))
            .collect(Collectors.toList());
        // envoi mail
        mailService.sendPayedEmail(user, timeslots.size() * Timeslot.TIMESLOT_PRICE, sortedTimeslots);
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
     *  get all the user timeslots.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Timeslot> findUserTimeslots() {
        log.debug("Request to get all Timeslots");
        return timeslotRepository.findByUserIsCurrentUserAndIsNotPayed();
    }

    /**
     *  get one timeslot by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Timeslot findOne(Long id) {
        log.debug("Request to get Timeslot : {}", id);
        Timeslot timeslot = timeslotRepository.findOneWithUser(id);
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
        return findOne(timeslot.getId());
    }

    @Scheduled(cron = "${scheduling.reminderEmails}")
    public void sendReminderEmails() {
        LocalDate minReservedDate = LocalDate.now().minusDays(NB_DAYS_BEFORE_REMINDER);
        log.info("Send all reminder emails (not payed since : {})", minReservedDate);
        List<Timeslot> timeslots = timeslotRepository.findByReservedIsTrueAndPayedIsFalseAndReservedDateBefore(minReservedDate);
        timeslots = timeslots.stream().sorted(Comparator.comparing(Timeslot::getStartTime)).collect(Collectors.toList());
        Map<User, List<Timeslot>> timeslotsByUser = timeslots.stream().collect(Collectors.groupingBy(Timeslot::getUser));
        log.info("Send {} reminder emails to {} users", timeslots.size(), timeslotsByUser.size());
        timeslotsByUser.forEach((user, ts) -> mailService.sendReminderEmail(user, ts.size() * Timeslot.TIMESLOT_PRICE, ts));
    }
}
