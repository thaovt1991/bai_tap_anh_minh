package com.codegym.service;

import com.codegym.model.Transfer;
import com.codegym.repository.ITransferRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TransferService implements ITransferService {

    @Autowired
    private ITransferRepository transferRepository;

    @Override
    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }

    @Override
    public Transfer findById(Long id) {
        return transferRepository.findById(id);
    }

    @Override
    public void save(Transfer transfer) {
        transferRepository.save(transfer);
    }

    @Override
    public void remove(Long id) {
        transferRepository.remove(id);
    }
}
