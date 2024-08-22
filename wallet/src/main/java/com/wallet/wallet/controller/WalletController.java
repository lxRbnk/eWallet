package com.wallet.wallet.controller;

import com.wallet.wallet.dto.*;
import com.wallet.wallet.model.Wallet;
import com.wallet.wallet.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wallet")
@AllArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/create")
    public void createWallet(@RequestBody Wallet wallet) {
        walletService.create(wallet);
    }

    @DeleteMapping("{id}")
    public void deleteWallet(@PathVariable Long id) {
        walletService.delete(id);
    }

    @GetMapping("{id}")
    public WalletDto getWalletInfo(@PathVariable Long id) {
        return walletService.getWalletInfo(id);
    }

    @GetMapping("/all/{ownerId}")
    public List<Wallet> getWalletsByUser(@PathVariable Long ownerId) {
        return walletService.getWallets(ownerId);
    }

    @PostMapping("/transfer")
    public void transfer(@RequestBody TransactionDto transferDto) {
        walletService.transfer(transferDto);
    }

    @PostMapping("/convert")
    public void convert(@RequestBody ConvertDto convertDto) {
        walletService.convert(convertDto);
    }

    @PostMapping("/top-up")
    public void topUp(@RequestBody TopUpDto topUpDto) {
        walletService.topUp(topUpDto);
    }

    @PostMapping("/withdrawal")
    public void withdrawal(@RequestBody WithdrawalDto withdrawalDto) {
        walletService.withdrawal(withdrawalDto);
    }

    //fixme delete
    @GetMapping("/test")
    public String go(HttpServletRequest request){
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.put(headerName, headerValue);
        }
        System.out.println(headers);
        return "test wallet";
    }

}
