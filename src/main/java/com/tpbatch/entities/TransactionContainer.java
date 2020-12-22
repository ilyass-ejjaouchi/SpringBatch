package com.tpbatch.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data @Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class TransactionContainer {
    // this class has not the dateDebit property
    private long idTransaction;
    private long idCompte;
    private double montant;
    private String dateTransactionStr;
}
