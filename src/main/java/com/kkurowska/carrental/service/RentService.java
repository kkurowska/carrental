package com.kkurowska.carrental.service;

import com.kkurowska.carrental.domain.Rent;
import com.kkurowska.carrental.repository.RentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Rent.
 */
@Service
@Transactional
public class RentService {

    private final Logger log = LoggerFactory.getLogger(RentService.class);
    
    @Inject
    private RentRepository rentRepository;

    /**
     * Save a rent.
     *
     * @param rent the entity to save
     * @return the persisted entity
     */
    public Rent save(Rent rent) {
        log.debug("Request to save Rent : {}", rent);
        Rent result = rentRepository.save(rent);
        return result;
    }

    /**
     *  Get all the rents.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Rent> findAll(Pageable pageable) {
        log.debug("Request to get all Rents");
        Page<Rent> result = rentRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one rent by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Rent findOne(Long id) {
        log.debug("Request to get Rent : {}", id);
        Rent rent = rentRepository.findOne(id);
        return rent;
    }

    /**
     *  Delete the  rent by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Rent : {}", id);
        rentRepository.delete(id);
    }
}
