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

    private static final Integer DEFAULT_LINE = 1;
    private static final Integer UPDATED_LINE = 2;

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
        timeslot.setLine(DEFAULT_LINE);
        timeslot.setVersion(DEFAULT_VERSION);
    }

    @Test
    @Transactional
    public void getNonExistingTimeslot() throws Exception {
        // Get the timeslot
        restTimeslotMockMvc.perform(get("/api/timeslots/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }
}
