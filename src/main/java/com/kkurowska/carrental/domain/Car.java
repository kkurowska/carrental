package com.kkurowska.carrental.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Car.
 */
@Entity
@Table(name = "car")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "brand", nullable = false)
    private String brand;

    @NotNull
    @Column(name = "registration_no", nullable = false)
    private String registration_no;

    @Column(name = "production_year")
    private Integer production_year;

    @NotNull
    @Column(name = "model", nullable = false)
    private String model;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public Car brand(String brand) {
        this.brand = brand;
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public Car registration_no(String registration_no) {
        this.registration_no = registration_no;
        return this;
    }

    public void setRegistration_no(String registration_no) {
        this.registration_no = registration_no;
    }

    public Integer getProduction_year() {
        return production_year;
    }

    public Car production_year(Integer production_year) {
        this.production_year = production_year;
        return this;
    }

    public void setProduction_year(Integer production_year) {
        this.production_year = production_year;
    }

    public String getModel() {
        return model;
    }

    public Car model(String model) {
        this.model = model;
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Car car = (Car) o;
        if (car.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, car.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Car{" +
            "id=" + id +
            ", brand='" + brand + "'" +
            ", registration_no='" + registration_no + "'" +
            ", production_year='" + production_year + "'" +
            ", model='" + model + "'" +
            '}';
    }
}
