package com.codegym.service;

import com.codegym.model.Withdraw;
import com.codegym.repository.IWithdrawRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class WithdrawService implements IWithdrawService {
    @Autowired
    private IWithdrawRepository withdrawRepository;

    @Override
    public List<Withdraw> findAll() {
        return withdrawRepository.findAll();
    }

    @Override
    public Withdraw findById(Long id) {
        return withdrawRepository.findById(id);
    }

    @Override
    public void save(Withdraw withdraw) {
        withdrawRepository.save(withdraw);
    }

    @Override
    public void remove(Long id) {
        withdrawRepository.remove(id);
    }
}
