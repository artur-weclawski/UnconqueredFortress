package com.game.Manager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ConnectionManager {
    private static final String urlHeader = "https://projektgame.azurewebsites.net/";
    public ConnectionManager() {
    }

    public JSONObject requestSend(JSONObject dataToSend, String Url) {
        try {
            URL url = new URL(urlHeader + Url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");

            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");
            for (String keyStr : dataToSend.keySet()) {
                Object keyValue = dataToSend.get(keyStr);
                con.setRequestProperty(keyStr, keyValue.toString());
            }

            StringBuilder response = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            } catch (Exception e) {
                return new JSONObject().put("status", 110).put("message", "ResponseErrorConnectionWithServer");
            }

            return new JSONObject(response.toString().replaceAll("\"", "\\\""));
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONObject().put("status", 406).put("message", "ResponseClientInternalError");
        }
    }
}
