package com.kkurowska.carrental.repository;

import com.kkurowska.carrental.domain.Car;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Car entity.
 */
@SuppressWarnings("unused")
public interface CarRepository extends JpaRepository<Car,Long> {

}
