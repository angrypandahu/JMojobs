package com.panda.mojobs.web.rest;

import com.panda.mojobs.JmojobsApp;

import com.panda.mojobs.domain.Address;
import com.panda.mojobs.repository.AddressRepository;
import com.panda.mojobs.service.AddressService;
import com.panda.mojobs.repository.search.AddressSearchRepository;
import com.panda.mojobs.service.dto.AddressDTO;
import com.panda.mojobs.service.mapper.AddressMapper;
import com.panda.mojobs.web.rest.errors.ExceptionTranslator;
import com.panda.mojobs.service.dto.AddressCriteria;
import com.panda.mojobs.service.AddressQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.panda.mojobs.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.panda.mojobs.domain.enumeration.Location;
/**
 * Test class for the AddressResource REST controller.
 *
 * @see AddressResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmojobsApp.class)
public class AddressResourceIntTest {

    private static final Location DEFAULT_NAME = Location.PUXI;
    private static final Location UPDATED_NAME = Location.PUDONG;

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_LINE = "AAAAAAAAAA";
    private static final String UPDATED_LINE = "BBBBBBBBBB";

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressSearchRepository addressSearchRepository;

    @Autowired
    private AddressQueryService addressQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAddressMockMvc;

    private Address address;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AddressResource addressResource = new AddressResource(addressService, addressQueryService);
        this.restAddressMockMvc = MockMvcBuilders.standaloneSetup(addressResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Address createEntity(EntityManager em) {
        Address address = new Address()
            .name(DEFAULT_NAME)
            .mobile(DEFAULT_MOBILE)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .line(DEFAULT_LINE);
        return address;
    }

    @Before
    public void initTest() {
        addressSearchRepository.deleteAll();
        address = createEntity(em);
    }

    @Test
    @Transactional
    public void createAddress() throws Exception {
        int databaseSizeBeforeCreate = addressRepository.findAll().size();

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);
        restAddressMockMvc.perform(post("/api/addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isCreated());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate + 1);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAddress.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testAddress.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAddress.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testAddress.getLine()).isEqualTo(DEFAULT_LINE);

        // Validate the Address in Elasticsearch
        Address addressEs = addressSearchRepository.findOne(testAddress.getId());
        assertThat(addressEs).isEqualToComparingFieldByField(testAddress);
    }

    @Test
    @Transactional
    public void createAddressWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = addressRepository.findAll().size();

        // Create the Address with an existing ID
        address.setId(1L);
        AddressDTO addressDTO = addressMapper.toDto(address);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAddressMockMvc.perform(post("/api/addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setName(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(address);

        restAddressMockMvc.perform(post("/api/addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAddresses() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList
        restAddressMockMvc.perform(get("/api/addresses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].line").value(hasItem(DEFAULT_LINE.toString())));
    }

    @Test
    @Transactional
    public void getAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get the address
        restAddressMockMvc.perform(get("/api/addresses/{id}", address.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(address.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.line").value(DEFAULT_LINE.toString()));
    }

    @Test
    @Transactional
    public void getAllAddressesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where name equals to DEFAULT_NAME
        defaultAddressShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the addressList where name equals to UPDATED_NAME
        defaultAddressShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAddressesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAddressShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the addressList where name equals to UPDATED_NAME
        defaultAddressShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAddressesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where name is not null
        defaultAddressShouldBeFound("name.specified=true");

        // Get all the addressList where name is null
        defaultAddressShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllAddressesByMobileIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where mobile equals to DEFAULT_MOBILE
        defaultAddressShouldBeFound("mobile.equals=" + DEFAULT_MOBILE);

        // Get all the addressList where mobile equals to UPDATED_MOBILE
        defaultAddressShouldNotBeFound("mobile.equals=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllAddressesByMobileIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where mobile in DEFAULT_MOBILE or UPDATED_MOBILE
        defaultAddressShouldBeFound("mobile.in=" + DEFAULT_MOBILE + "," + UPDATED_MOBILE);

        // Get all the addressList where mobile equals to UPDATED_MOBILE
        defaultAddressShouldNotBeFound("mobile.in=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllAddressesByMobileIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where mobile is not null
        defaultAddressShouldBeFound("mobile.specified=true");

        // Get all the addressList where mobile is null
        defaultAddressShouldNotBeFound("mobile.specified=false");
    }

    @Test
    @Transactional
    public void getAllAddressesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where email equals to DEFAULT_EMAIL
        defaultAddressShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the addressList where email equals to UPDATED_EMAIL
        defaultAddressShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAddressesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultAddressShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the addressList where email equals to UPDATED_EMAIL
        defaultAddressShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAddressesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where email is not null
        defaultAddressShouldBeFound("email.specified=true");

        // Get all the addressList where email is null
        defaultAddressShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllAddressesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where phone equals to DEFAULT_PHONE
        defaultAddressShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the addressList where phone equals to UPDATED_PHONE
        defaultAddressShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllAddressesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultAddressShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the addressList where phone equals to UPDATED_PHONE
        defaultAddressShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllAddressesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where phone is not null
        defaultAddressShouldBeFound("phone.specified=true");

        // Get all the addressList where phone is null
        defaultAddressShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    public void getAllAddressesByLineIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where line equals to DEFAULT_LINE
        defaultAddressShouldBeFound("line.equals=" + DEFAULT_LINE);

        // Get all the addressList where line equals to UPDATED_LINE
        defaultAddressShouldNotBeFound("line.equals=" + UPDATED_LINE);
    }

    @Test
    @Transactional
    public void getAllAddressesByLineIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where line in DEFAULT_LINE or UPDATED_LINE
        defaultAddressShouldBeFound("line.in=" + DEFAULT_LINE + "," + UPDATED_LINE);

        // Get all the addressList where line equals to UPDATED_LINE
        defaultAddressShouldNotBeFound("line.in=" + UPDATED_LINE);
    }

    @Test
    @Transactional
    public void getAllAddressesByLineIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where line is not null
        defaultAddressShouldBeFound("line.specified=true");

        // Get all the addressList where line is null
        defaultAddressShouldNotBeFound("line.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAddressShouldBeFound(String filter) throws Exception {
        restAddressMockMvc.perform(get("/api/addresses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].line").value(hasItem(DEFAULT_LINE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAddressShouldNotBeFound(String filter) throws Exception {
        restAddressMockMvc.perform(get("/api/addresses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingAddress() throws Exception {
        // Get the address
        restAddressMockMvc.perform(get("/api/addresses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);
        addressSearchRepository.save(address);
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address
        Address updatedAddress = addressRepository.findOne(address.getId());
        updatedAddress
            .name(UPDATED_NAME)
            .mobile(UPDATED_MOBILE)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .line(UPDATED_LINE);
        AddressDTO addressDTO = addressMapper.toDto(updatedAddress);

        restAddressMockMvc.perform(put("/api/addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAddress.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testAddress.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAddress.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testAddress.getLine()).isEqualTo(UPDATED_LINE);

        // Validate the Address in Elasticsearch
        Address addressEs = addressSearchRepository.findOne(testAddress.getId());
        assertThat(addressEs).isEqualToComparingFieldByField(testAddress);
    }

    @Test
    @Transactional
    public void updateNonExistingAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAddressMockMvc.perform(put("/api/addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isCreated());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);
        addressSearchRepository.save(address);
        int databaseSizeBeforeDelete = addressRepository.findAll().size();

        // Get the address
        restAddressMockMvc.perform(delete("/api/addresses/{id}", address.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean addressExistsInEs = addressSearchRepository.exists(address.getId());
        assertThat(addressExistsInEs).isFalse();

        // Validate the database is empty
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);
        addressSearchRepository.save(address);

        // Search the address
        restAddressMockMvc.perform(get("/api/_search/addresses?query=id:" + address.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].line").value(hasItem(DEFAULT_LINE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Address.class);
        Address address1 = new Address();
        address1.setId(1L);
        Address address2 = new Address();
        address2.setId(address1.getId());
        assertThat(address1).isEqualTo(address2);
        address2.setId(2L);
        assertThat(address1).isNotEqualTo(address2);
        address1.setId(null);
        assertThat(address1).isNotEqualTo(address2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AddressDTO.class);
        AddressDTO addressDTO1 = new AddressDTO();
        addressDTO1.setId(1L);
        AddressDTO addressDTO2 = new AddressDTO();
        assertThat(addressDTO1).isNotEqualTo(addressDTO2);
        addressDTO2.setId(addressDTO1.getId());
        assertThat(addressDTO1).isEqualTo(addressDTO2);
        addressDTO2.setId(2L);
        assertThat(addressDTO1).isNotEqualTo(addressDTO2);
        addressDTO1.setId(null);
        assertThat(addressDTO1).isNotEqualTo(addressDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(addressMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(addressMapper.fromId(null)).isNull();
    }
}
