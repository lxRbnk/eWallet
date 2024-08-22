package com.wallet.wallet.repository;

import com.wallet.wallet.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionalRepository extends JpaRepository<Transaction, Long> {
}
