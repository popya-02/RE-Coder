package com.heartlink.chatbot.Controller;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

@Controller
public class ChatbotController {

    private static String secretKey = "bHBkTEJiVm9lT3pjSlBPTVVzbnd1QXRHSFFCVHhsUEo=";
    private static String apiUrl = "https://0ix8fo6vl4.apigw.ntruss.com/custom/v1/15283/37526a18b31b19e83c524f1590276586ccb10b41e946731080d54eb1d7253d6f";

    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public String sendMessage(@Payload String chatMessage) throws IOException {

        System.out.println("Received chat message: " + chatMessage); // 디버깅 로그 추가

        URL url = new URL(apiUrl);
        String message = getReqMessage(chatMessage);
        String encodeBase64String = makeSignature(message, secretKey);

        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json;UTF-8");
        con.setRequestProperty("X-NCP-CHATBOT_SIGNATURE", encodeBase64String);

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(message.getBytes("UTF-8"));
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        String chatResponse = "";

        System.out.println("Response Code: " + responseCode); // 디버깅 로그 추가

        if(responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            StringBuilder jsonString = new StringBuilder();
            String decodedString;
            while ((decodedString = in.readLine()) != null) {
                jsonString.append(decodedString);
            }

            System.out.println("API Response: " + jsonString.toString()); // 디버깅 로그 추가

            JSONParser jsonparser = new JSONParser();
            try {
                JSONObject json = (JSONObject)jsonparser.parse(jsonString.toString());
                JSONArray bubblesArray = (JSONArray)json.get("bubbles");

                if (bubblesArray != null && !bubblesArray.isEmpty()) {
                    JSONObject bubbles = (JSONObject) bubblesArray.get(0);
                    String type = (String) bubbles.get("type");

                    if ("template".equals(type)) {
                        JSONObject data = (JSONObject) bubbles.get("data");
                        JSONObject cover = (JSONObject) data.get("cover");
                        JSONObject coverData = (JSONObject) cover.get("data");
                        chatResponse = coverData.get("description").toString(); // 인코딩 처리 생략
                    } else if ("text".equals(type)) {
                        JSONObject data = (JSONObject) bubbles.get("data");
                        chatResponse = data.get("description").toString(); // 인코딩 처리 생략
                    }
                } else {
                    chatResponse = "Bubbles array is empty or null.";
                }
            } catch (Exception e) {
                System.out.println("Error parsing JSON response");
                e.printStackTrace();
                chatResponse = "Error parsing response.";
            }

            in.close();
        } else {
            chatResponse = con.getResponseMessage();
        }

        return chatResponse;
    }


    public static String makeSignature(String message, String secretKey) {
        String encodeBase64String = "";
        try {
            byte[] secrete_key_bytes = secretKey.getBytes("UTF-8");
            SecretKeySpec signingKey = new SecretKeySpec(secrete_key_bytes, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
            encodeBase64String = Base64.encodeBase64String(rawHmac);
            return encodeBase64String;

        } catch (Exception e){
            System.out.println(e);
        }
        return encodeBase64String;
    }

    public static String getReqMessage(String messageContent) {
        String requestBody = "";
        try {
            JSONObject obj = new JSONObject();
            long timestamp = new Date().getTime();
            obj.put("version", "v2");
            obj.put("userId", "abc@gmail.com");
            obj.put("timestamp", timestamp);

            JSONObject bubbles_obj = new JSONObject();
            JSONObject data_obj = new JSONObject();

            if ("open".equals(messageContent)) {
                bubbles_obj.put("type", "text");
                data_obj.put("description", "Welcome to our chatbot! How can I assist you today?");
            } else {
                bubbles_obj.put("type", "text");
                data_obj.put("description", messageContent);
            }
            bubbles_obj.put("data", data_obj);
            JSONArray bubbles_array = new JSONArray();
            bubbles_array.add(bubbles_obj);

            obj.put("bubbles", bubbles_array);
            obj.put("event", "open".equals(messageContent) ? "open" : "send");

            requestBody = obj.toString();
            System.out.println("Request Body: " + requestBody); // 디버깅 로그 추가

        } catch (Exception e){
            System.out.println("## Exception : " + e);
        }

        return requestBody;
    }


}