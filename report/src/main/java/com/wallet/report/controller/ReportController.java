package com.wallet.report.controller;

import com.wallet.report.model.WalletDto;
import com.wallet.report.service.WalletClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
public class ReportController {

//    @GetMapping("/transactions")
//    public void getTransactionReport(@RequestParam("from") LocalDate from, @RequestParam("to") LocalDate to){
//    }
//
//    @GetMapping("/balance")
//    public void getBalanceReport(@RequestParam("date") LocalDate date) {
//    }
//
//    @GetMapping("/income-expense")
//    public void getIncomeExpenseReport(@RequestParam("from") LocalDate from, @RequestParam("to") LocalDate to) {
//    }
//
//    @GetMapping("/currency-exchange")
//    public void getCurrencyExchangeReport(@RequestParam("from") LocalDate from, @RequestParam("to") LocalDate to) {
//    }



//fixme delete
    private final WalletClient walletClient;

    @Autowired
    public ReportController(WalletClient walletClient) {
        this.walletClient = walletClient;
    }

    @GetMapping("/wallet/{id}")
    public ResponseEntity<WalletDto> getWallet(@PathVariable Long id) {
        WalletDto wallet = walletClient.getWalletById(id);
        if (wallet != null) {
            return ResponseEntity.ok(wallet);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
