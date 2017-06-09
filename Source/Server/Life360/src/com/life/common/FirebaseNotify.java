/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.life.common;

import com.life.api.SApiMessage;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;

/**
 *
 * @author truongnguyen
 */
public class FirebaseNotify {

    public static FirebaseNotify instance = new FirebaseNotify();
    private final String serverKey;
    private final String url;
    private final Charset charset;

    private FirebaseNotify() {
        url = "https://fcm.googleapis.com/fcm/send";
        serverKey = "key=" + "AAAA2wUwyfY:APA91bE14jsF-qauUYoe-dy-JPQBRC9ZR1z4ttzlQKAMvcZiQ4eSRvn0Ta8kiP8WKKvZtK_yEUbBoS5gzbVi_E4olspwgof3zDSPKfDGAIHVVtRKnunbG_-TYAaMEJ0qB9I-JdJQy5XK";

        charset = Charset.forName("UTF-8");
    }

    public void notify(List<String> tokenNotify, SApiMessage data) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("registration_ids", tokenNotify);
            jSONObject.put("data", data);
            System.out.println(jSONObject);

            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Authorization", serverKey);

            StringEntity stringEntity = new StringEntity(jSONObject.toJSONString(), charset);

            httpPost.setEntity(stringEntity);

            HttpClient client = HttpClientBuilder.create().build();

            HttpResponse execute = client.execute(httpPost);

            System.out.println("kq: " + execute);
        } catch (IOException ex) {
            Logger.getLogger(FirebaseNotify.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {
        JSONObject data = new JSONObject();
        data.put("content", "hello 123!!!");
        List<String> token = new ArrayList<>();
        token.add("eppA2O86Hmw:APA91bEyWGhY0BivivXJatEyZKDYsxmMmxBWRTfK3fauIem6nZK33IIM88VIY6bAiw_CPGFcWn78oMw8A3t59bMZE8tEUyeA7wERJkJrFdro_UMI244HGNKu92DUyxs-DiJzLojIYNye");
        token.add("dx-hkpHyv-A:APA91bF7NG7FX2zTryxy1oJtGc6vHLwJ6mKlmJuqkpSK0KhGlBjUcOGzU9je7yAIUBIGR2z2hkwc_s7DlUn6fm5D3ytcxSq2oy_U7ELXnDixs6MsA3Shv8DIJKGREC0c2yc7JJX0Q4Mn");
        // instance.notify(data, token);
    }

}
