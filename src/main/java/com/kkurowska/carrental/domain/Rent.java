package com.kkurowska.carrental.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Rent.
 */
@Entity
@Table(name = "rent")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Rent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "price", precision=10, scale=2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Column(name = "deposit", precision=10, scale=2, nullable = false)
    private BigDecimal deposit;

    @ManyToOne
    @NotNull
    private Car car;

    @ManyToOne
    @NotNull
    private Customer customer;

    @ManyToOne
    @NotNull
    private User employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Rent price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public Rent deposit(BigDecimal deposit) {
        this.deposit = deposit;
        return this;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public Car getCar() {
        return car;
    }

    public Rent car(Car car) {
        this.car = car;
        return this;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Rent customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getEmployee() {
        return employee;
    }

    public Rent employee(User user) {
        this.employee = user;
        return this;
    }

    public void setEmployee(User user) {
        this.employee = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rent rent = (Rent) o;
        if (rent.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, rent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Rent{" +
            "id=" + id +
            ", price='" + price + "'" +
            ", deposit='" + deposit + "'" +
            '}';
    }
}
