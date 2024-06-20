package org.tron.btcmonitor.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.util.StringUtils;
import org.tron.btcmonitor.entity.TransactionInfo;
import org.tron.btcmonitor.entity.TxIn;
import org.tron.btcmonitor.entity.TxOut;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CurlUtils {

    public static String getBlockHash(String url, Long h) throws IOException {
        String data = "{\"method\": \"getblockhash\", \"params\": [" + h + "]}";
        String res =  post(url, data);
        if (StringUtils.isEmpty(res)) {
            return null;
        }
        return extractHash(res);
    }

    private static String extractHash(String res) {
        Map map = (Map)JSON.parse(res);
        return map.get("result").toString();
    }

    public static String post(String urlPath, String data) throws IOException {
// 建立连接
        URL url = new URL(urlPath);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
// 设置参数
        httpConn.setDoOutput(true); // 需要输出
        httpConn.setDoInput(true); // 需要输入
        httpConn.setUseCaches(false); // 不允许缓存
        httpConn.setRequestMethod("POST"); // 设置POST方式连接

//设置发送数据的格式
        httpConn.setRequestProperty("Content-Type", "application/json");

// 设置接收数据的格式
        httpConn.setRequestProperty("Accept", "application/json");
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        httpConn.setRequestProperty("Charset", "UTF-8");
        httpConn.connect();
// 连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect



// 建立输入流，向指向的URL传入参数
        DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream());
        dos.writeBytes(data);
        dos.flush();
        dos.close();
// 获得响应状态

        int resultCode = httpConn.getResponseCode();

        StringBuffer sb = new StringBuffer();

        if (HttpURLConnection.HTTP_OK == resultCode) {

            String readLine = new String();

            BufferedReader responseReader = new BufferedReader(

                    new InputStreamReader(httpConn.getInputStream(), "UTF-8"));

            while ((readLine = responseReader.readLine()) != null) {

                sb.append(readLine).append("\n");

            }

            responseReader.close();

        }

        return sb.toString();

    }

    public static List<String> getBlockTxs(String url, String blockHash) throws IOException {
        String data = "{\"method\": \"getblock\", \"params\": [\"" + blockHash + "\"]}";
        String res = post(url, data);
        System.out.println(res);
        return convertResToBlocks(res);
    }

    private static List<String> convertResToBlocks(String res) {
        Map map = (Map)JSON.parse(res);
        map = (Map) map.get("result");
        return  (List)map.get("tx");
    }

    public static TransactionInfo getTransactionInfo(String url, String tx) throws IOException {
        String data = "{\"method\": \"getrawtransaction\", \"params\": [\"" + tx  + "\", 1]}";
        String r = post(url, data);
        return convertResToTransactionInfo(r);
    }

    private static TransactionInfo convertResToTransactionInfo(String r) {
        Map map = (Map)JSON.parse(r);
        map = (Map) map.get("result");

        List<Map> vin = (List)map.get("vin");

        TransactionInfo transactionInfo = new TransactionInfo();
        transactionInfo.setIns(convertVin(vin));

        List<Map> vout = (List)map.get("vout");
        transactionInfo.setOuts(convertOut(vout));
        return transactionInfo;

    }

    private static List<TxOut> convertOut(List<Map> vout) {
        List<TxOut> list = new ArrayList<>();
        int idx = 0;
        for (Map map : vout) {
            TxOut out = new TxOut();
            out.setValue(map.get("value").toString());

            Map map2 = (Map)map.get("scriptPubKey");
            if (!map2.containsKey("address")) {
                continue;
            }
            out.setReceiver(map2.get("address").toString());
            out.setOutIndex(idx);
            idx ++;
            list.add(out);
        }
        return list;
    }

    private static List<TxIn> convertVin(List<Map> vin) {

        List<TxIn> list = new ArrayList<>();
        for (Map map : vin) {
            TxIn in = new TxIn();
            if (!map.containsKey("txid") || !map.containsKey("vout")) {
                continue;
            }
            in.setTxId(map.get("txid").toString());
            in.setOutIndex((Integer)map.get("vout"));
            list.add(in);
        }
        return list;
    }

    public static boolean outTxIsSpent(String url, String txId, Integer index) throws IOException {
        String data = "{\"method\": \"gettxout\", \"params\": [\"" + txId  + "\", " + index + "]}";
        String res = post(url, data);
        Map map = (Map)JSON.parse(res);
        if (map.get("result") != null) {
            return false;
        }
        return true;
    }
}
