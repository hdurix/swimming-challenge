package fr.hippo.swimmingchallenge.web.rest;

import fr.hippo.swimmingchallenge.Application;
import fr.hippo.swimmingchallenge.domain.Timeslot;
import fr.hippo.swimmingchallenge.repository.TimeslotRepository;
import fr.hippo.swimmingchallenge.service.TimeslotService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TimeslotResource REST controller.
 *
 * @see TimeslotResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TimeslotResourceIntTest {

    private static final String DEFAULT_START_TIME = "AAAAA";
    private static final String UPDATED_START_TIME = "BBBBB";
    private static final String DEFAULT_END_TIME = "AAAAA";
    private static final String UPDATED_END_TIME = "BBBBB";

    private static final Boolean DEFAULT_PAYED = false;
    private static final Boolean UPDATED_PAYED = true;

    private static final Boolean DEFAULT_RESERVED = false;
    private static final Boolean UPDATED_RESERVED = true;
    private static final String DEFAULT_TEAM_NAME = "AAAAA";
    private static final String UPDATED_TEAM_NAME = "BBBBB";
    private static final String DEFAULT_SWIMMER1 = "AAAAA";
    private static final String UPDATED_SWIMMER1 = "BBBBB";
    private static final String DEFAULT_SWIMMER2 = "AAAAA";
    private static final String UPDATED_SWIMMER2 = "BBBBB";
    private static final String DEFAULT_SWIMMER3 = "AAAAA";
    private static final String UPDATED_SWIMMER3 = "BBBBB";
    private static final String DEFAULT_SWIMMER4 = "AAAAA";
    private static final String UPDATED_SWIMMER4 = "BBBBB";

    private static final Long DEFAULT_VERSION = 1L;
    private static final Long UPDATED_VERSION = 2L;

    @Inject
    private TimeslotRepository timeslotRepository;

    @Inject
    private TimeslotService timeslotService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTimeslotMockMvc;

    private Timeslot timeslot;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TimeslotResource timeslotResource = new TimeslotResource();
        ReflectionTestUtils.setField(timeslotResource, "timeslotService", timeslotService);
        this.restTimeslotMockMvc = MockMvcBuilders.standaloneSetup(timeslotResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        timeslot = new Timeslot();
        timeslot.setStartTime(DEFAULT_START_TIME);
        timeslot.setEndTime(DEFAULT_END_TIME);
        timeslot.setPayed(DEFAULT_PAYED);
        timeslot.setReserved(DEFAULT_RESERVED);
        timeslot.setTeamName(DEFAULT_TEAM_NAME);
        timeslot.setSwimmer1(DEFAULT_SWIMMER1);
        timeslot.setSwimmer2(DEFAULT_SWIMMER2);
        timeslot.setSwimmer3(DEFAULT_SWIMMER3);
        timeslot.setSwimmer4(DEFAULT_SWIMMER4);
        timeslot.setVersion(DEFAULT_VERSION);
    }

    @Test
    @Transactional
    public void createTimeslot() throws Exception {
        int databaseSizeBeforeCreate = timeslotRepository.findAll().size();

        // Create the Timeslot

        restTimeslotMockMvc.perform(post("/api/timeslots")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(timeslot)))
                .andExpect(status().isCreated());

        // Validate the Timeslot in the database
        List<Timeslot> timeslots = timeslotRepository.findAll();
        assertThat(timeslots).hasSize(databaseSizeBeforeCreate + 1);
        Timeslot testTimeslot = timeslots.get(timeslots.size() - 1);
        assertThat(testTimeslot.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testTimeslot.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testTimeslot.getPayed()).isEqualTo(DEFAULT_PAYED);
        assertThat(testTimeslot.getReserved()).isEqualTo(DEFAULT_RESERVED);
        assertThat(testTimeslot.getTeamName()).isEqualTo(DEFAULT_TEAM_NAME);
        assertThat(testTimeslot.getSwimmer1()).isEqualTo(DEFAULT_SWIMMER1);
        assertThat(testTimeslot.getSwimmer2()).isEqualTo(DEFAULT_SWIMMER2);
        assertThat(testTimeslot.getSwimmer3()).isEqualTo(DEFAULT_SWIMMER3);
        assertThat(testTimeslot.getSwimmer4()).isEqualTo(DEFAULT_SWIMMER4);
        assertThat(testTimeslot.getVersion()).isEqualTo(DEFAULT_VERSION);
    }

    @Test
    @Transactional
    public void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = timeslotRepository.findAll().size();
        // set the field null
        timeslot.setStartTime(null);

        // Create the Timeslot, which fails.

        restTimeslotMockMvc.perform(post("/api/timeslots")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(timeslot)))
                .andExpect(status().isBadRequest());

        List<Timeslot> timeslots = timeslotRepository.findAll();
        assertThat(timeslots).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = timeslotRepository.findAll().size();
        // set the field null
        timeslot.setEndTime(null);

        // Create the Timeslot, which fails.

        restTimeslotMockMvc.perform(post("/api/timeslots")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(timeslot)))
                .andExpect(status().isBadRequest());

        List<Timeslot> timeslots = timeslotRepository.findAll();
        assertThat(timeslots).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPayedIsRequired() throws Exception {
        int databaseSizeBeforeTest = timeslotRepository.findAll().size();
        // set the field null
        timeslot.setPayed(null);

        // Create the Timeslot, which fails.

        restTimeslotMockMvc.perform(post("/api/timeslots")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(timeslot)))
                .andExpect(status().isBadRequest());

        List<Timeslot> timeslots = timeslotRepository.findAll();
        assertThat(timeslots).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReservedIsRequired() throws Exception {
        int databaseSizeBeforeTest = timeslotRepository.findAll().size();
        // set the field null
        timeslot.setReserved(null);

        // Create the Timeslot, which fails.

        restTimeslotMockMvc.perform(post("/api/timeslots")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(timeslot)))
                .andExpect(status().isBadRequest());

        List<Timeslot> timeslots = timeslotRepository.findAll();
        assertThat(timeslots).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = timeslotRepository.findAll().size();
        // set the field null
        timeslot.setVersion(null);

        // Create the Timeslot, which fails.

        restTimeslotMockMvc.perform(post("/api/timeslots")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(timeslot)))
                .andExpect(status().isBadRequest());

        List<Timeslot> timeslots = timeslotRepository.findAll();
        assertThat(timeslots).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTimeslots() throws Exception {
        // Initialize the database
        timeslotRepository.saveAndFlush(timeslot);

        // Get all the timeslots
        restTimeslotMockMvc.perform(get("/api/timeslots?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(timeslot.getId().intValue())))
                .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
                .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
                .andExpect(jsonPath("$.[*].payed").value(hasItem(DEFAULT_PAYED.booleanValue())))
                .andExpect(jsonPath("$.[*].reserved").value(hasItem(DEFAULT_RESERVED.booleanValue())))
                .andExpect(jsonPath("$.[*].teamName").value(hasItem(DEFAULT_TEAM_NAME.toString())))
                .andExpect(jsonPath("$.[*].swimmer1").value(hasItem(DEFAULT_SWIMMER1.toString())))
                .andExpect(jsonPath("$.[*].swimmer2").value(hasItem(DEFAULT_SWIMMER2.toString())))
                .andExpect(jsonPath("$.[*].swimmer3").value(hasItem(DEFAULT_SWIMMER3.toString())))
                .andExpect(jsonPath("$.[*].swimmer4").value(hasItem(DEFAULT_SWIMMER4.toString())))
                .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.intValue())));
    }

    @Test
    @Transactional
    public void getTimeslot() throws Exception {
        // Initialize the database
        timeslotRepository.saveAndFlush(timeslot);

        // Get the timeslot
        restTimeslotMockMvc.perform(get("/api/timeslots/{id}", timeslot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(timeslot.getId().intValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.payed").value(DEFAULT_PAYED.booleanValue()))
            .andExpect(jsonPath("$.reserved").value(DEFAULT_RESERVED.booleanValue()))
            .andExpect(jsonPath("$.teamName").value(DEFAULT_TEAM_NAME.toString()))
            .andExpect(jsonPath("$.swimmer1").value(DEFAULT_SWIMMER1.toString()))
            .andExpect(jsonPath("$.swimmer2").value(DEFAULT_SWIMMER2.toString()))
            .andExpect(jsonPath("$.swimmer3").value(DEFAULT_SWIMMER3.toString()))
            .andExpect(jsonPath("$.swimmer4").value(DEFAULT_SWIMMER4.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTimeslot() throws Exception {
        // Get the timeslot
        restTimeslotMockMvc.perform(get("/api/timeslots/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTimeslot() throws Exception {
        // Initialize the database
        timeslotRepository.saveAndFlush(timeslot);

		int databaseSizeBeforeUpdate = timeslotRepository.findAll().size();

        // Update the timeslot
        timeslot.setStartTime(UPDATED_START_TIME);
        timeslot.setEndTime(UPDATED_END_TIME);
        timeslot.setPayed(UPDATED_PAYED);
        timeslot.setReserved(UPDATED_RESERVED);
        timeslot.setTeamName(UPDATED_TEAM_NAME);
        timeslot.setSwimmer1(UPDATED_SWIMMER1);
        timeslot.setSwimmer2(UPDATED_SWIMMER2);
        timeslot.setSwimmer3(UPDATED_SWIMMER3);
        timeslot.setSwimmer4(UPDATED_SWIMMER4);
        timeslot.setVersion(UPDATED_VERSION);

        restTimeslotMockMvc.perform(put("/api/timeslots")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(timeslot)))
                .andExpect(status().isOk());

        // Validate the Timeslot in the database
        List<Timeslot> timeslots = timeslotRepository.findAll();
        assertThat(timeslots).hasSize(databaseSizeBeforeUpdate);
        Timeslot testTimeslot = timeslots.get(timeslots.size() - 1);
        assertThat(testTimeslot.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testTimeslot.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testTimeslot.getPayed()).isEqualTo(UPDATED_PAYED);
        assertThat(testTimeslot.getReserved()).isEqualTo(UPDATED_RESERVED);
        assertThat(testTimeslot.getTeamName()).isEqualTo(UPDATED_TEAM_NAME);
        assertThat(testTimeslot.getSwimmer1()).isEqualTo(UPDATED_SWIMMER1);
        assertThat(testTimeslot.getSwimmer2()).isEqualTo(UPDATED_SWIMMER2);
        assertThat(testTimeslot.getSwimmer3()).isEqualTo(UPDATED_SWIMMER3);
        assertThat(testTimeslot.getSwimmer4()).isEqualTo(UPDATED_SWIMMER4);
        assertThat(testTimeslot.getVersion()).isEqualTo(UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void deleteTimeslot() throws Exception {
        // Initialize the database
        timeslotRepository.saveAndFlush(timeslot);

		int databaseSizeBeforeDelete = timeslotRepository.findAll().size();

        // Get the timeslot
        restTimeslotMockMvc.perform(delete("/api/timeslots/{id}", timeslot.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Timeslot> timeslots = timeslotRepository.findAll();
        assertThat(timeslots).hasSize(databaseSizeBeforeDelete - 1);
    }
}
