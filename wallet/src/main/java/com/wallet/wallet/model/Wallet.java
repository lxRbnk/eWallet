package com.wallet.wallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "wallets")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column
    private BigDecimal balance;

    @JsonIgnore
    @OneToMany(mappedBy = "walletSender")
    private List<Transaction> sentTransactions;

    @JsonIgnore
    @OneToMany(mappedBy = "walletReceiver")
    private List<Transaction> receivedTransactions;

}
