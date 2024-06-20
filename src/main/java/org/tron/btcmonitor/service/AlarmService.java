package org.tron.btcmonitor.service;

import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
public class AlarmService {

    @Resource
    private RestTemplate restTemplate;

    @Value("${slack.url}")
    private String slackUrl;

    private Set<String> errorMsgSet = new HashSet<>();

    private List<String> errorMsgs = new ArrayList<>();

    @PostConstruct
    public void init() {
        sendToSlack("btc monitor start");
    }

    public void sendToSlack(String msg) {
        try {
            HttpHeaders headers = new HttpHeaders();
            MediaType mediaType = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(mediaType);
            Map<String, String> map = new HashMap<>();
            map.put("text", msg);
            HttpEntity<String> strEntity = new HttpEntity<String>(JSONObject.toJSONString(map), headers);

            String res =
                    restTemplate.postForObject(slackUrl, strEntity, String.class);
            if (res ==null || !res.equals("ok")) {
                log.error("Slack send error, msg:{}. errMsg:{}", msg, res);
            }
        } catch (Exception e) {
            log.error("Slack send error, msg:{}. errMsg:{}", msg, e.getMessage());
        }
    }

    public void alarm(String msg) {
        System.out.println(msg);
        if (errorMsgSet.contains(msg)) {
            return;
        }
        errorMsgSet.add(msg);
        errorMsgs.add(msg);
        sendToSlack(msg);
        if (errorMsgs.size() > 10000) {
            for (int i = 0; i<5000; i++) {
                errorMsgSet.remove(errorMsgs.get(i));
            }
            errorMsgs = new ArrayList<>(errorMsgSet);
        }

    }
}
