package org.tron.btcmonitor.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigInteger;

@Data
@Entity
public class TxOut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long blockNumber;

    private String txId;

    private Integer outIndex;

    private String value;

    private String receiver;

    private boolean spent;

}
