package com.wallet.wallet.controller;

import com.wallet.wallet.dto.*;
import com.wallet.wallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallet")
@AllArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/create")
    public ResponseEntity<String> createWallet(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid WalletDto walletDto,
            BindingResult bindingResult) {
        if (!walletService.create(walletDto, token) ||
                bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok("created wallet");
        }
    }

    @DeleteMapping("/{id}") //method for admin
    public ResponseEntity<String> deleteWallet(
            @PathVariable Long id) {
        if (walletService.delete(id)) {
            return ResponseEntity.ok("deleted wallet");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletDto> getWalletInfo(
            @PathVariable Long id) {
        WalletDto walletDto = walletService.getWalletInfo(id);
        if (walletDto != null) {
            return ResponseEntity.ok(walletDto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    public List<WalletDto> getWalletsByUser(
            @RequestHeader("Authorization") String token) {
        return walletService.getWallets(token);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(
            @RequestHeader("Authorization") String token,
            @RequestBody TransactionDto transferDto) {
        String transferMessage = walletService.transfer(transferDto, token);
        if (transferMessage != null) {
            return ResponseEntity.ok(transferMessage);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/convert")
    public ResponseEntity<String> convert(
            @RequestBody @Valid ConvertDto convertDto) {
        boolean result = walletService.convert(convertDto);
        if (result) {
            return ResponseEntity.ok("completed");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/top-up")
    public ResponseEntity<String> topUp(
            @RequestBody @Valid TopUpDto topUpDto,
            BindingResult bindingResult) {
        if (!walletService.topUp(topUpDto) || bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok("completed");

        }
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<String> withdrawal(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid WithdrawalDto withdrawalDto,
            BindingResult bindingResult) {
        if (!walletService.withdrawal(withdrawalDto, token) || bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("failed");
        } else {
            return ResponseEntity.ok("completed");
        }
    }

}