package org.tron.btcmonitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tron.btcmonitor.entity.TxOut;
import org.tron.btcmonitor.repository.TxOutRepository;

import java.util.Collections;
import java.util.List;

@Service
public class TxOutService {

    @Autowired
    private TxOutRepository txOutRepository;


    public List<TxOut> getUnspentTxs() {
        return txOutRepository.getAllUnspentTxs();
    }

    public void spent(String txId, Integer outIndex) {
        txOutRepository.spent(txId, outIndex);
    }

    public void saveAll(List<TxOut> outs) {
        txOutRepository.saveAll(outs);
    }

    public TxOut find(TxOut txOut) {
        return txOutRepository.findByTxId(txOut.getTxId(), txOut.getOutIndex());
    }
}
