package com.tpbatch.batch;

import com.tpbatch.entities.Compte;
import com.tpbatch.entities.Transaction;
import com.tpbatch.repositories.CompteRepository;
import com.tpbatch.repositories.TransactionRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionWriter  implements ItemWriter<Transaction> {

    @Autowired private TransactionRepository transactionRepo;
    @Autowired private CompteRepository compteRepository;

    @Override
    public void write(List<? extends Transaction> transactions) throws Exception {
           transactions.forEach(transaction -> {
               // on enregistre la transaction puis on débite le solde du client
               transactionRepo.save(transaction);
               Compte c = transaction.getCompte();
               c.setSolde(c.getSolde() - transaction.getMontant());
               compteRepository.save(c);
           });
    }
}
