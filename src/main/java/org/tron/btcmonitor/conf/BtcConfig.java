package org.tron.btcmonitor.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.tron.btcmonitor.entity.TxOut;

import java.util.List;

@ConfigurationProperties(prefix = "btc.monitor")
@Component
@Data
public class BtcConfig {

    private Long beginBlock;

    private List<TxOut> outs;
}
