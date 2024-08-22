package com.wallet.wallet.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "wallet_sender_id", nullable = false)
    private Wallet walletSender;

    @ManyToOne
    @JoinColumn(name = "wallet_receiver_id", nullable = false)
    private Wallet walletReceiver;

    @Column
    private BigDecimal amount;

    @Column
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column
    @Enumerated(EnumType.STRING)
    private Currency currencyTo;

    @Column
    @CreationTimestamp
    private LocalDateTime timeStamp;

    @Column
    private boolean status;
}
