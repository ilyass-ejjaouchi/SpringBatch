package com.tpbatch.batch;

import com.tpbatch.entities.Transaction;
import com.tpbatch.entities.TransactionContainer;
import com.tpbatch.repositories.CompteRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class TransactionProcces implements ItemProcessor<TransactionContainer,Transaction> {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    @Autowired private CompteRepository compteRepository;


    @Override
    public Transaction process(TransactionContainer transactionContainer) throws Exception {
                Transaction t = new Transaction();
                t.setIdTransaction(transactionContainer.getIdTransaction());
                t.setDateTransaction(dateFormat.parse(transactionContainer.getDateTransactionStr()));
                t.setDateDebit(new Date());
                t.setMontant(transactionContainer.getMontant());
                t.setCompte(compteRepository.findById(transactionContainer.getIdCompte()).get());
        return t;
    }
}