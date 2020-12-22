package com.tpbatch.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data @Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idTransaction;
    private double montant;
    private Date dateTransaction;
    private Date dateDebit;
    @ManyToOne
    private Compte compte;
}
