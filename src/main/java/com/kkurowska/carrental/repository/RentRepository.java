package com.kkurowska.carrental.repository;

import com.kkurowska.carrental.domain.Rent;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Rent entity.
 */
@SuppressWarnings("unused")
public interface RentRepository extends JpaRepository<Rent,Long> {

    @Query("select rent from Rent rent where rent.employee.login = ?#{principal.username}")
    List<Rent> findByEmployeeIsCurrentUser();

}
