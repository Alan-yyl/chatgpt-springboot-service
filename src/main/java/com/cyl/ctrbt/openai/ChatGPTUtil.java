package com.cyl.ctrbt.openai;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cyl.ctrbt.openai.entity.billing.CreditGrantsResponse;
import com.cyl.ctrbt.openai.entity.chat.ChatCompletion;
import com.cyl.ctrbt.openai.entity.chat.ChatCompletionResponse;
import com.cyl.ctrbt.openai.entity.chat.Message;
import com.cyl.ctrbt.util.Proxys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class ChatGPTUtil {
    @Value("${openai.secret_key}")
    private String token;

    private ChatGPT chatGPT;

    @PostConstruct
    public void init(){
        chatGPT = ChatGPT.builder()
                .apiKey(token)
                .timeout(600)
                .apiHost("https://api.openai.com/") //代理地址
                .build()
                .init();
    }
    public Message chat(String userMessage,String user) {
        List<Message> messages=new ArrayList<>();
        for (JSONObject jsonObject: JSONArray.parseArray(userMessage,JSONObject.class)){
            Message message =null;
            if ("ChatGPT".equals(jsonObject.get("name"))){
                message = new Message("assistant", (String) jsonObject.get("msg"));
            }else if ("user".equals(jsonObject.get("name"))){
                message = new Message("user", (String) jsonObject.get("msg"));
            }else {
                message = new Message("system", (String) jsonObject.get("msg"));
            }
            messages.add(message);
        }

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .user(user)
                .messages(messages)
                .maxTokens(3000)
                .temperature(0.9)
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        return response.getChoices().get(0).getMessage();
    }
}
