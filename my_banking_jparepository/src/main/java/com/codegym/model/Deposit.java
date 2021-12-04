package com.codegym.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "deposits")
public class Deposit {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    private long amount;
    private boolean isDelete = false ;
    private LocalDateTime created_at = LocalDateTime.now();


    public Deposit(Long id, Customer customer, long amount) {
        this.id = id;
        this.customer = customer;
        this.amount = amount;
    }

    public Deposit(Customer customer, long amount) {
        this.customer = customer;
        this.amount = amount;
    }

}
