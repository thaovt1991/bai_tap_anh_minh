package com.codegym.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "withdraws")
public class Withdraw {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idOwner;
    private long amount;
    private boolean isDelete = false;
    private LocalDateTime datetime = LocalDateTime.now();

    public Withdraw() {
    }

    public Withdraw(Long idOwner) {
        this.idOwner = idOwner;
    }

    public Withdraw(Long idOwner, Long amount) {
        this.idOwner = idOwner;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(long idOwner) {
        this.idOwner = idOwner;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean status) {
        this.isDelete = status;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }
}
