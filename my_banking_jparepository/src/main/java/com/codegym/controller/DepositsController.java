package com.codegym.controller;

import com.codegym.model.Customer;
import com.codegym.model.Deposit;
import com.codegym.model.Withdraw;
import com.codegym.service.customer.ICustomerService;
import com.codegym.service.deposit.IDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class DepositsController {
    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IDepositService depositService;

    @GetMapping("/deposit/{id}")
    private ModelAndView viewDeposits(@PathVariable Long id) {
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/customer/deposit");
            modelAndView.addObject("deposit", new Deposit());
            modelAndView.addObject("customer", customer.get());
            modelAndView.addObject("success", null);
            modelAndView.addObject("error", null);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/deposit/{customerId}")
    private ModelAndView saveDeposits(@PathVariable Long customerId, @ModelAttribute("deposits") Deposit deposits) {
        ModelAndView modelAndView = new ModelAndView("/customer/deposit");
        Customer customer = customerService.findById(customerId).get();

        long money_deposits = deposits.getAmount();

        boolean isMoney = false;

        if (money_deposits >= 1000 && money_deposits <= 1000000000) {
            isMoney = true;
        } else {
            modelAndView.addObject("error_limit", "The amount is greater than the limit 1,000,000,000 and smaller 1,000");
        }

        if (isMoney) {
            try {
                deposits.setCustomer(customer);
                depositService.save(deposits);
                customer.setBalance(customer.getBalance() + deposits.getAmount());
                customerService.save(customer);
                modelAndView.addObject("message", "Deposits successfully");
            } catch (Exception e) {
                modelAndView.addObject("error", "Error system ! Please contact the system administrator to solve the problem ! ");
            }
        } else {
            modelAndView.addObject("error", "Deposits error !  ");
        }

        modelAndView.addObject("deposit", new Deposit());
        modelAndView.addObject("customer", customer);

        return modelAndView;
    }
}
