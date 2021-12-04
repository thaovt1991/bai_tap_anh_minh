package com.codegym.controller;


import com.codegym.model.Customer;
import com.codegym.model.Withdraw;
import com.codegym.service.customer.ICustomerService;
import com.codegym.service.withdraw.IWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class WithdrawController {
    @Autowired
    private IWithdrawService withdrawService;

    @Autowired
    private ICustomerService customerService;

    @GetMapping("/withdraw/{id}")
    private ModelAndView viewWithdraw(@PathVariable Long id) {

        Optional<Customer> customer = customerService.findById(id);
        if (customer.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/customer/withdraw");
            modelAndView.addObject("withdraw", new Withdraw());
            modelAndView.addObject("customer", customer.get());
            modelAndView.addObject("success", null);
            modelAndView.addObject("error", null);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/withdraw/{customerId}")
    private ModelAndView saveWithdraw(@PathVariable Long customerId,@ModelAttribute("withdraw") Withdraw withdraw) {
        ModelAndView modelAndView = new ModelAndView("/customer/withdraw");
        Customer customer = customerService.findById(customerId).get();
        long money_withdraw = withdraw.getAmount();
        boolean isMoney = false;
        if (money_withdraw >= 1000 && money_withdraw <= 1000000000 ) {
            isMoney = true;
        }else{
            modelAndView.addObject("error_money", "The amount is greater than the limit 1,000,000,000 and smaller 1,000 !");
        }
        boolean isLimit = false;
        if (money_withdraw <= customer.getBalance()) {
            isLimit = true;
        }else{
            modelAndView.addObject("error_limit", " The amount is greater than the limit balance !");
        }

        boolean isTrue = isMoney && isLimit;

        if (isTrue) {
            withdrawService.save(withdraw);
            customer.setBalance(customer.getBalance() - withdraw.getAmount());
            customerService.save(customer);
            modelAndView.addObject("success", "Withdraw successfully");
        } else {
            modelAndView.addObject("error", "Withdraw error !");
        }
        modelAndView.addObject("withdraw", new Withdraw());
        modelAndView.addObject("customer", customer);

        return modelAndView;
    }
}
