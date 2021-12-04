package com.codegym.controller;


import com.codegym.model.Customer;
import com.codegym.model.Transfer;
import com.codegym.service.customer.ICustomerService;
import com.codegym.service.transfer.ITransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class TransferController {

    @Autowired
    private ITransferService transferService;

    @Autowired
    private ICustomerService customerService;

    @GetMapping("/transfers/{id}")
    private ModelAndView viewDeposits(@PathVariable Long id) {

        ModelAndView modelAndView = new ModelAndView();

        Optional<Customer> sender = customerService.findById(id);

        List<Customer> recipients = customerService.findAllNotId(id);

        if (sender.isPresent()) {
            modelAndView.setViewName("/customer/transfer");
            modelAndView.addObject("success", null);
            modelAndView.addObject("error", null);
            modelAndView.addObject("transfer", new Transfer());
            modelAndView.addObject("sender", sender.get());
            modelAndView.addObject("recipients", recipients);
        } else {
            modelAndView.setViewName("/error.404");
        }

        return modelAndView;
    }

    @PostMapping("/transfers/{senderId}")
    private ModelAndView doTransfer(@PathVariable Long senderId, @ModelAttribute Transfer transfer) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/customer/transfer");

        Optional<Customer> sender = customerService.findById(senderId);
        if (sender.isPresent()) {
            List<Customer> recipients = customerService.findAllNotId(senderId);
            if (transfer.getRecipient().getId() != sender.get().getId()) {
                Optional<Customer> recipient = customerService.findById(transfer.getRecipient().getId());
                if (recipient.isPresent()) {
                    long senderBalance = sender.get().getBalance();
                    long recipientBalance = recipient.get().getBalance();
                    long transferAmount = transfer.getTransferAmount();
                    long fees = 10;
                    long feesAmount = transferAmount / fees;
                    long transactionAmount = transferAmount + feesAmount;

                    boolean isMoney = false;
                    if (transferAmount >= 1000 && transferAmount <= 1000000000) {
                        isMoney = true;
                    } else {
                        modelAndView.addObject("error_money", "The amount is greater than the limit 1,000,000,000 and smaller 1,000 !");
                    }

                    boolean isLimit = false;
                    if (senderBalance - transactionAmount > 0) {
                        isLimit = true;
                    } else {
                        modelAndView.addObject("error_limit", " The amount is greater than the limit balance !");
                    }

                    boolean isTrue = isLimit && isMoney;
                    if (isTrue) {
                        try {
                            sender.get().setBalance(senderBalance - transactionAmount);
                            customerService.save(sender.get());

                            recipient.get().setBalance(recipientBalance + transferAmount);
                            customerService.save(recipient.get());

                            transfer.setFees(fees);
                            transfer.setFeesAmount(feesAmount);
                            transfer.setTransactionAmount(transactionAmount);
                            transferService.save(transfer);
                            modelAndView.addObject("success", "Transfer successfully");
                        } catch (Exception e) {
                            modelAndView.addObject("error", "Error system ! Please contact the system administrator to solve the problem !  ");
                        }
                    } else {
                        modelAndView.addObject("error", "Transfer error ! ");
                    }
                }
            } else {
                modelAndView.addObject("error", "Transfer error ! ");
                modelAndView.addObject("error_recipient", "Recipient not exist !");
            }
            modelAndView.addObject("transfer", new Transfer());
            modelAndView.addObject("sender", sender.get());
            modelAndView.addObject("recipients", recipients);
        } else {
            modelAndView.setViewName("/error.404");
        }
        return modelAndView;
    }

    @GetMapping("/history-transfers")
    public ModelAndView showListTransfers() {
        ModelAndView modelAndView = new ModelAndView("/customer/transfers_list");
        Iterable<Transfer> transfers = transferService.findAll();
        long total = 0;
        for (Transfer t : transfers) {
            total += t.getFeesAmount();
        }
        modelAndView.addObject("transfers", transfers);
        modelAndView.addObject("total", total);

        return modelAndView;
    }
}

//    @PostMapping("/{senderId}")
//    private ModelAndView saveDeposits(@ModelAttribute("transfer") Transfer transfer) {
//
////        Long transaction_amount = transfer.getTransferAmount() / transfer.getFees();
////        transfer.setTransaction_amount(transaction_amount);
////        Customer customer_sender = customerService.findById(transfer.getIdSender());
////        Long id_sender = customer_sender.getId();
////        Customer customer_recipient = customerService.findById(transfer.getIdRecipient());
////        long totalMoneyTrasfer = transfer.getTransferAmount() + transfer.getTransaction_amount();
////        long balance_sender = customer_sender.getBalance();
////        List<Customer> customersListAll = customerService.findAll();
////        List<Customer> customerListRecipient = new ArrayList<>();
////        for (Customer c : customersListAll) {
////            if (c.getId() != customer_sender.getId()) {
////                customerListRecipient.add(c);
////            }
////        }
////        ModelAndView modelAndView = new ModelAndView("/customer/transfer");
////
////        boolean isTrue = false;
////        boolean isLimit = false;
////        if (balance_sender >= totalMoneyTrasfer) {
////            isLimit = true;
////        }
////        boolean isMoney = false;
////        if (transfer.getTransferAmount() >= 1000 && transfer.getTransferAmount() < 100000 * 100000) {
////            isMoney = true;
////        }
////        boolean isIdRecipient = false;
////        if (customer_recipient != null) {
////            isIdRecipient = true;
////        }
////        boolean isNotnull = false;
////        if (transfer.getTransferAmount() != 0) {
////            isNotnull = true;
////        }
////
////
////        isTrue = isLimit && isMoney && isIdRecipient && isNotnull;
////
////        if (isTrue) {
////            transferService.save(transfer);
////            customer_sender.setBalance(customer_sender.getBalance() - transfer.getTransaction_amount() - transfer.getTransferAmount());
////            customer_recipient.setBalance(customer_recipient.getBalance() + transfer.getTransferAmount());
////            customerService.save(customer_sender);
////            customerService.save(customer_recipient);
////            modelAndView.addObject("success", "Transfer successfully");
////        } else {
////            modelAndView.addObject("error", "Transfer error ! ");
////        }
////
////        modelAndView.addObject("transfer", new Transfer(id_sender));
////        modelAndView.addObject("customer_sender", customer_sender);
////        modelAndView.addObject("customersList", customerListRecipient);
////        return modelAndView;
//    }

