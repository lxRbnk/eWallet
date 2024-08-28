package com.wallet.report.controller;

import com.wallet.report.dto.TransactionDto;
import com.wallet.report.service.ExcelService;
import com.wallet.report.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;
    private final ExcelService excelService;

    @GetMapping("/transactions")
    public List<TransactionDto> getCurrentUserTransactions(
            @RequestHeader("Authorization") String token) {
        return reportService.getUserTransactions(token);
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadCurrentUserTransactions(
            @RequestHeader("Authorization") String token) throws IOException {
        List<TransactionDto> transactions = reportService.getUserTransactions(token);
        ByteArrayInputStream excelFile = excelService.createExcelFile(transactions);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=report.xlsx");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(excelFile));
    }

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


}
