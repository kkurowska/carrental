package com.kkurowska.carrental.web.rest;

import com.kkurowska.carrental.CarrentalApp;

import com.kkurowska.carrental.domain.Rent;
import com.kkurowska.carrental.domain.Car;
import com.kkurowska.carrental.domain.Customer;
import com.kkurowska.carrental.repository.RentRepository;
import com.kkurowska.carrental.service.RentService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RentResource REST controller.
 *
 * @see RentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarrentalApp.class)
public class RentResourceIntTest {

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_DEPOSIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEPOSIT = new BigDecimal(2);

    private static final LocalDate DEFAULT_RENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RENT_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private RentRepository rentRepository;

    @Inject
    private RentService rentService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRentMockMvc;

    private Rent rent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RentResource rentResource = new RentResource();
        ReflectionTestUtils.setField(rentResource, "rentService", rentService);
        this.restRentMockMvc = MockMvcBuilders.standaloneSetup(rentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rent createEntity(EntityManager em) {
        Rent rent = new Rent()
                .price(DEFAULT_PRICE)
                .deposit(DEFAULT_DEPOSIT)
                .rent_date(DEFAULT_RENT_DATE);
        // Add required entity
        Car car = CarResourceIntTest.createEntity(em);
        em.persist(car);
        em.flush();
        rent.setCar(car);
        // Add required entity
        Customer customer = CustomerResourceIntTest.createEntity(em);
        em.persist(customer);
        em.flush();
        rent.setCustomer(customer);
        return rent;
    }

    @Before
    public void initTest() {
        rent = createEntity(em);
    }

    @Test
    @Transactional
    public void createRent() throws Exception {
        int databaseSizeBeforeCreate = rentRepository.findAll().size();

        // Create the Rent

        restRentMockMvc.perform(post("/api/rents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rent)))
            .andExpect(status().isCreated());

        // Validate the Rent in the database
        List<Rent> rentList = rentRepository.findAll();
        assertThat(rentList).hasSize(databaseSizeBeforeCreate + 1);
        Rent testRent = rentList.get(rentList.size() - 1);
        assertThat(testRent.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testRent.getDeposit()).isEqualTo(DEFAULT_DEPOSIT);
        assertThat(testRent.getRent_date()).isEqualTo(DEFAULT_RENT_DATE);
    }

    @Test
    @Transactional
    public void createRentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rentRepository.findAll().size();

        // Create the Rent with an existing ID
        Rent existingRent = new Rent();
        existingRent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRentMockMvc.perform(post("/api/rents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingRent)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Rent> rentList = rentRepository.findAll();
        assertThat(rentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentRepository.findAll().size();
        // set the field null
        rent.setPrice(null);

        // Create the Rent, which fails.

        restRentMockMvc.perform(post("/api/rents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rent)))
            .andExpect(status().isBadRequest());

        List<Rent> rentList = rentRepository.findAll();
        assertThat(rentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDepositIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentRepository.findAll().size();
        // set the field null
        rent.setDeposit(null);

        // Create the Rent, which fails.

        restRentMockMvc.perform(post("/api/rents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rent)))
            .andExpect(status().isBadRequest());

        List<Rent> rentList = rentRepository.findAll();
        assertThat(rentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRent_dateIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentRepository.findAll().size();
        // set the field null
        rent.setRent_date(null);

        // Create the Rent, which fails.

        restRentMockMvc.perform(post("/api/rents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rent)))
            .andExpect(status().isBadRequest());

        List<Rent> rentList = rentRepository.findAll();
        assertThat(rentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRents() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get all the rentList
        restRentMockMvc.perform(get("/api/rents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rent.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].deposit").value(hasItem(DEFAULT_DEPOSIT.intValue())))
            .andExpect(jsonPath("$.[*].rent_date").value(hasItem(DEFAULT_RENT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getRent() throws Exception {
        // Initialize the database
        rentRepository.saveAndFlush(rent);

        // Get the rent
        restRentMockMvc.perform(get("/api/rents/{id}", rent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rent.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.deposit").value(DEFAULT_DEPOSIT.intValue()))
            .andExpect(jsonPath("$.rent_date").value(DEFAULT_RENT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRent() throws Exception {
        // Get the rent
        restRentMockMvc.perform(get("/api/rents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRent() throws Exception {
        // Initialize the database
        rentService.save(rent);

        int databaseSizeBeforeUpdate = rentRepository.findAll().size();

        // Update the rent
        Rent updatedRent = rentRepository.findOne(rent.getId());
        updatedRent
                .price(UPDATED_PRICE)
                .deposit(UPDATED_DEPOSIT)
                .rent_date(UPDATED_RENT_DATE);

        restRentMockMvc.perform(put("/api/rents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRent)))
            .andExpect(status().isOk());

        // Validate the Rent in the database
        List<Rent> rentList = rentRepository.findAll();
        assertThat(rentList).hasSize(databaseSizeBeforeUpdate);
        Rent testRent = rentList.get(rentList.size() - 1);
        assertThat(testRent.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testRent.getDeposit()).isEqualTo(UPDATED_DEPOSIT);
        assertThat(testRent.getRent_date()).isEqualTo(UPDATED_RENT_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingRent() throws Exception {
        int databaseSizeBeforeUpdate = rentRepository.findAll().size();

        // Create the Rent

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRentMockMvc.perform(put("/api/rents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rent)))
            .andExpect(status().isCreated());

        // Validate the Rent in the database
        List<Rent> rentList = rentRepository.findAll();
        assertThat(rentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRent() throws Exception {
        // Initialize the database
        rentService.save(rent);

        int databaseSizeBeforeDelete = rentRepository.findAll().size();

        // Get the rent
        restRentMockMvc.perform(delete("/api/rents/{id}", rent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Rent> rentList = rentRepository.findAll();
        assertThat(rentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
