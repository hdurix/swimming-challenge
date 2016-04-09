package fr.hippo.swimmingchallenge.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.hippo.swimmingchallenge.domain.Timeslot;
import fr.hippo.swimmingchallenge.security.AuthoritiesConstants;
import fr.hippo.swimmingchallenge.service.TimeslotService;
import fr.hippo.swimmingchallenge.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Timeslot.
 */
@RestController
@RequestMapping("/api")
@DenyAll
public class TimeslotResource {

    private final Logger log = LoggerFactory.getLogger(TimeslotResource.class);

    @Inject
    private TimeslotService timeslotService;

    /**
     * PUT  /timeslots -> Updates an existing timeslot.
     */
    @RequestMapping(value = "/timeslots",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.USER)
    public ResponseEntity<Timeslot> updateTimeslot(@Valid @RequestBody Timeslot timeslot) throws URISyntaxException {
        log.debug("REST request to update Timeslot : {}", timeslot);
        if (timeslot.getId() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("timeslot", "idinexists", "A timeslot needs to have an ID")).body(null);
        }
        Timeslot result = timeslotService.save(timeslot);
        String user = result.getUser().getDisplayName();
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("Le créneau a été réservé pour " + user, user))
            .body(result);
    }

    /**
     * GET  /timeslots -> get all the timeslots.
     */
    @RequestMapping(value = "/timeslots",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @PermitAll
    public List<Timeslot> getAllTimeslots() {
        log.debug("REST request to get all Timeslots");
        return timeslotService.findAll();
    }

    /**
     * GET  /timeslots/:id -> get the "id" timeslot.
     */
    @RequestMapping(value = "/timeslots/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.USER)
    public ResponseEntity<Timeslot> getTimeslot(@PathVariable Long id) {
        log.debug("REST request to get Timeslot : {}", id);
        Timeslot timeslot = timeslotService.findOne(id);
        return Optional.ofNullable(timeslot)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /timeslots/:id -> get the "id" timeslot.
     */
    @RequestMapping(value = "/timeslots/{id}",
        method = RequestMethod.PUT,
        params = "erase",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Timeslot> eraseTimeslot(@PathVariable Long id) {
        log.debug("REST request to get Timeslot : {}", id);
        Timeslot timeslot = timeslotService.eraseTimeslot(id);
        return Optional.ofNullable(timeslot)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /timeslots/:id -> get the "id" timeslot.
     */
    @RequestMapping(value = "/timeslots",
        method = RequestMethod.GET,
        params = {"hasPay"},
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.USER)
    public ResponseEntity<Void> hasPay(@RequestParam Long userId) {
        log.debug("Request to set as pay {}", userId);
        timeslotService.hasPay(userId);
        return ResponseEntity.ok().build();
    }
}
