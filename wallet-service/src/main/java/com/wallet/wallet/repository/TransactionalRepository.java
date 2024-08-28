package com.wallet.wallet.repository;

import com.wallet.wallet.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionalRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.walletSender.ownerLogin = :ownerLogin " +
            "OR t.walletReceiver.ownerLogin = :ownerLogin")
    List<Transaction> findAllByOwnerLogin(@Param("ownerLogin") String ownerLogin);
}
