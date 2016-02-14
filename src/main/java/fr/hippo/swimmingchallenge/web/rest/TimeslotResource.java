package fr.hippo.swimmingchallenge.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.hippo.swimmingchallenge.domain.Timeslot;
import fr.hippo.swimmingchallenge.service.TimeslotService;
import fr.hippo.swimmingchallenge.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
public class TimeslotResource {

    private final Logger log = LoggerFactory.getLogger(TimeslotResource.class);
        
    @Inject
    private TimeslotService timeslotService;
    
    /**
     * POST  /timeslots -> Create a new timeslot.
     */
    @RequestMapping(value = "/timeslots",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Timeslot> createTimeslot(@Valid @RequestBody Timeslot timeslot) throws URISyntaxException {
        log.debug("REST request to save Timeslot : {}", timeslot);
        if (timeslot.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("timeslot", "idexists", "A new timeslot cannot already have an ID")).body(null);
        }
        Timeslot result = timeslotService.save(timeslot);
        return ResponseEntity.created(new URI("/api/timeslots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("timeslot", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /timeslots -> Updates an existing timeslot.
     */
    @RequestMapping(value = "/timeslots",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Timeslot> updateTimeslot(@Valid @RequestBody Timeslot timeslot) throws URISyntaxException {
        log.debug("REST request to update Timeslot : {}", timeslot);
        if (timeslot.getId() == null) {
            return createTimeslot(timeslot);
        }
        Timeslot result = timeslotService.save(timeslot);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("timeslot", timeslot.getId().toString()))
            .body(result);
    }

    /**
     * GET  /timeslots -> get all the timeslots.
     */
    @RequestMapping(value = "/timeslots",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
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
     * DELETE  /timeslots/:id -> delete the "id" timeslot.
     */
    @RequestMapping(value = "/timeslots/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTimeslot(@PathVariable Long id) {
        log.debug("REST request to delete Timeslot : {}", id);
        timeslotService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("timeslot", id.toString())).build();
    }
}
