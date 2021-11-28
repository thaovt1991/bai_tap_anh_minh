package com.codegym.controller;

import com.codegym.model.Customer;
import com.codegym.model.Transfer;
import com.codegym.service.ICustomerService;
import com.codegym.service.ITransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TransferController {

    @Autowired
    private ITransferService transferService;

    @Autowired
    private ICustomerService customerService;

    @GetMapping("/transfer/{id}")
    private ModelAndView viewDeposits(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("/customer/transfer");
        Customer customer_sender = customerService.findById(id);
        List<Customer> customersListAll = customerService.findAll();
        List<Customer> customerListRecipient = new ArrayList<>() ;
       for (Customer c : customersListAll){
           if(c.getId()!=customer_sender.getId()){
               customerListRecipient.add(c);
           }
       }
       modelAndView.addObject("success",null);
        modelAndView.addObject("error",null);
        modelAndView.addObject("transfer",new Transfer(id));
        modelAndView.addObject("customer_sender",customer_sender);
        modelAndView.addObject("customersList",customerListRecipient);
        return modelAndView ;
    }
    @PostMapping("/transfer")
    private ModelAndView saveDeposits(@ModelAttribute("transfer") Transfer transfer){

        Long transaction_amount = transfer.getTransferAmount()/transfer.getFees() ;
        transfer.setTransaction_amount(transaction_amount);
        Customer customer_sender = customerService.findById(transfer.getIdSender());
        Long id_sender = customer_sender.getId();
        Customer customer_recipient = customerService.findById(transfer.getIdRecipient());
        long totalMoneyTrasfer = transfer.getTransferAmount()+transfer.getTransaction_amount() ;
        long balance_sender = customer_sender.getBalance() ;
        List<Customer> customersListAll = customerService.findAll();
        List<Customer> customerListRecipient = new ArrayList<>() ;
        for (Customer c : customersListAll){
            if(c.getId()!=customer_sender.getId()){
                customerListRecipient.add(c);
            }
        }
        ModelAndView modelAndView = new ModelAndView("/customer/transfer");

        boolean isTrue = false ;
        boolean isLimit = false;
        if(balance_sender>= totalMoneyTrasfer){
            isLimit = true ;
        }
        boolean isMoney = false;
         if(transfer.getTransferAmount() >= 1000 && transfer.getTransferAmount()<100000*100000){
             isMoney= true ;
         }
         boolean isIdRecipient = false ;
        if(customer_recipient!=null){
            isIdRecipient= true ;
        }
        boolean isNotnull = false ;
        if(transfer.getTransferAmount() != 0){
            isNotnull = true;
        }


        isTrue = isLimit && isMoney && isIdRecipient && isNotnull;

        if(isTrue){
        transferService.save(transfer);
        customer_sender.setBalance(customer_sender.getBalance() - transfer.getTransaction_amount() -transfer.getTransferAmount());
        customer_recipient.setBalance(customer_recipient.getBalance()+ transfer.getTransferAmount());
        customerService.save(customer_sender);
        customerService.save(customer_recipient);
        modelAndView.addObject("success", "Transfer successfully");}
        else {
            modelAndView.addObject("error", "Transfer error ! ");
        }

        modelAndView.addObject("transfer",new Transfer(id_sender));
        modelAndView.addObject("customer_sender",customer_sender);
        modelAndView.addObject("customersList",customerListRecipient);
        return modelAndView ;
    }
    @GetMapping("/history-transfers")
    public ModelAndView showListTransfers(){
        ModelAndView modelAndView = new ModelAndView("/customer/transfers_list") ;
        List<Customer> customersList = customerService.findAll() ;
        List<Transfer> transfers = transferService.findAll() ;
        long total = 0 ;
        for(Transfer t : transfers){
            total += t.getTransaction_amount() ;
        }
        modelAndView.addObject("transfers",transfers) ;
        modelAndView.addObject("total",total) ;
        modelAndView.addObject("customersList",customersList);
        return modelAndView ;
    }
}
