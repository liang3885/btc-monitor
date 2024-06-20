package org.tron.btcmonitor.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BtcUrlGenerator {

    private String[] urls = new String[] {
            "https://docs-demo.btc.quiknode.pro/"
    };

    private int index;

    public String getUrl() {
        index += 1;
        if (index >= urls.length) {
            index = 0;
        }
        return urls[index];
    }
}
