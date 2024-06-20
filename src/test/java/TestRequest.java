
import org.junit.jupiter.api.Test;
import org.tron.btcmonitor.entity.TransactionInfo;
import org.tron.btcmonitor.utils.CurlUtils;

import java.io.*;
import java.util.List;


public class TestRequest {

    @Test
    public void testRequest() throws IOException {
        Long h = 1000000L;
        String url = "https://docs-demo.btc.quiknode.pro/";
        String blockHash = CurlUtils.getBlockHash(url, h);
        System.out.println(blockHash);
        if (blockHash == null) {
            return;
        }

//        String blockHash = "000000000000000000024f4a89aee120b9eff84f336d923a014f094e6d0c5b45";

        List<String> txs = CurlUtils.getBlockTxs(url, blockHash);
        System.out.println(txs);

        for (String tx : txs) {
            TransactionInfo transaction = CurlUtils.getTransactionInfo(url, tx);
            System.out.println(tx  + " : " + transaction);
        }
    }

    @Test
    public void testGetTransaction() throws IOException {
        String url = "https://docs-demo.btc.quiknode.pro";
        String tx = "f09de186285a5c77e7424b8f08a0a8cf59f057ddf8fd322a0e73675c486b7068";
        TransactionInfo transaction = CurlUtils.getTransactionInfo(url, tx);
        System.out.println(tx  + " : " + transaction);
    }

    @Test
    public void testSpent() throws IOException {
        String url = "https://docs-demo.btc.quiknode.pro";
        String tx = "5add3a8deb43b4267da764315ae6354fceae28751be64ea4274ddb92d06042fa";
        System.out.println(CurlUtils.outTxIsSpent(url, tx, 0));
    }
}
