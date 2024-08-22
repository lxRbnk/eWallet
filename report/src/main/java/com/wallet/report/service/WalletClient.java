package com.wallet.report.service;

import com.wallet.report.model.WalletDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WalletClient {

    private final RestTemplate restTemplate;

    @Value("${wallet.service.url}")
    private String walletServiceUrl;

    @Autowired
    public WalletClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WalletDto getWalletById(Long id) {
        String url = walletServiceUrl + "/wallet/" + id;
        return restTemplate.getForObject(url, WalletDto.class);
    }
}