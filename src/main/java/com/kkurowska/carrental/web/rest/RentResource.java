package com.kkurowska.carrental.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kkurowska.carrental.domain.Rent;
import com.kkurowska.carrental.service.RentService;
import com.kkurowska.carrental.web.rest.util.HeaderUtil;
import com.kkurowska.carrental.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Rent.
 */
@RestController
@RequestMapping("/api")
public class RentResource {

    private final Logger log = LoggerFactory.getLogger(RentResource.class);
        
    @Inject
    private RentService rentService;

    /**
     * POST  /rents : Create a new rent.
     *
     * @param rent the rent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rent, or with status 400 (Bad Request) if the rent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rents")
    @Timed
    public ResponseEntity<Rent> createRent(@Valid @RequestBody Rent rent) throws URISyntaxException {
        log.debug("REST request to save Rent : {}", rent);
        if (rent.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("rent", "idexists", "A new rent cannot already have an ID")).body(null);
        }
        Rent result = rentService.save(rent);
        return ResponseEntity.created(new URI("/api/rents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("rent", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rents : Updates an existing rent.
     *
     * @param rent the rent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rent,
     * or with status 400 (Bad Request) if the rent is not valid,
     * or with status 500 (Internal Server Error) if the rent couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rents")
    @Timed
    public ResponseEntity<Rent> updateRent(@Valid @RequestBody Rent rent) throws URISyntaxException {
        log.debug("REST request to update Rent : {}", rent);
        if (rent.getId() == null) {
            return createRent(rent);
        }
        Rent result = rentService.save(rent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("rent", rent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rents : get all the rents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rents in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/rents")
    @Timed
    public ResponseEntity<List<Rent>> getAllRents(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Rents");
        Page<Rent> page = rentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /rents/:id : get the "id" rent.
     *
     * @param id the id of the rent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rent, or with status 404 (Not Found)
     */
    @GetMapping("/rents/{id}")
    @Timed
    public ResponseEntity<Rent> getRent(@PathVariable Long id) {
        log.debug("REST request to get Rent : {}", id);
        Rent rent = rentService.findOne(id);
        return Optional.ofNullable(rent)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rents/:id : delete the "id" rent.
     *
     * @param id the id of the rent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rents/{id}")
    @Timed
    public ResponseEntity<Void> deleteRent(@PathVariable Long id) {
        log.debug("REST request to delete Rent : {}", id);
        rentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("rent", id.toString())).build();
    }

}
