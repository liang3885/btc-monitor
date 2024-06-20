package org.tron.btcmonitor.entity;

import lombok.Data;

import java.util.List;

@Data
public class TransactionInfo {

    private String txId;

    private List<TxIn> ins;

    private List<TxOut> outs;

}
