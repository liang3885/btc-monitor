package org.tron.btcmonitor.task;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tron.btcmonitor.conf.BtcConfig;
import org.tron.btcmonitor.entity.TxOut;
import org.tron.btcmonitor.service.AlarmService;
import org.tron.btcmonitor.service.BtcUrlGenerator;
import org.tron.btcmonitor.service.ProgressService;
import org.tron.btcmonitor.service.TxOutService;
import org.tron.btcmonitor.utils.CurlUtils;

import java.io.IOException;
import java.util.List;

@Component
public class BtcTransactionTask {

    private String  progressKey = "last.height";

    @Autowired
    private TxOutService txOutService;

    @Autowired
    private ProgressService progressService;

    @Autowired
    private BtcConfig config;

    @Autowired
    private BtcUrlGenerator btcUrlGenerator;

    @Autowired
    private AlarmService alarmService;

    @PostConstruct
    public void init() {
        List<TxOut> txOuts = config.getOuts();
        TxOut txOut = txOuts.get(0);
        TxOut out2 =  txOutService.find(txOut);
        if (out2 == null) {
            txOutService.saveAll(txOuts);
        }

        Long lastHeight = progressService.getValue(progressKey);
        if (lastHeight == null) {
            progressService.save(progressKey, config.getBeginBlock());
        }
    }

    @Scheduled(fixedDelay = 600000)
    @Transactional
    public void checkTxs() throws IOException {

        List<TxOut> unspentTxs = txOutService.getUnspentTxs();
        for (TxOut unspentTx : unspentTxs) {
            boolean spent = CurlUtils.outTxIsSpent(btcUrlGenerator.getUrl(),
                    unspentTx.getTxId(), unspentTx.getOutIndex());
            if (spent) {
                txOutService.spent(unspentTx.getTxId(), unspentTx.getOutIndex());
                String alarmMsg = String.format("txID : %s, out Index %s, amount %s is spent",
                        unspentTx.getTxId(), unspentTx.getOutIndex(), unspentTx.getValue());
                System.out.println(alarmMsg);
                alarmService.sendToSlack(alarmMsg);
            }
        }
    }

}
