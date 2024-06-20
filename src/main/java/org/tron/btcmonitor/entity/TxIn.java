package org.tron.btcmonitor.entity;

import lombok.Data;

@Data
public class TxIn {

    private String txId;
    private Integer outIndex;

}
