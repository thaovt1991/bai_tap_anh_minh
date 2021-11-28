package com.codegym.service;

import com.codegym.model.Deposits;
import com.codegym.repository.IDepositsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class DepositsService implements IDepositsService {
    @Autowired
    private IDepositsRepository depositsRepository;

    @Override
    public List<Deposits> findAll() {
        return depositsRepository.findAll();
    }

    @Override
    public Deposits findById(Long id) {
        return depositsRepository.findById(id);
    }

    @Override
    public void save(Deposits deposits) {
        depositsRepository.save(deposits);
    }

    @Override
    public void remove(Long id) {
        depositsRepository.remove(id);
    }
}
