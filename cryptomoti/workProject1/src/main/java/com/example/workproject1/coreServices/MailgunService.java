package com.example.workproject1.coreServices;

import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class MailgunService {
    
    @Value("${MAILGUN_DOMAIN:sandbox5ecb525ad76f413da37987ee9792bec2.mailgun.org}")
    private String domain;
    
    @Value("${MAILGUN_API_KEY:xxx}")
    private String apiKey;
    
    @Value("${MAILGUN_FROM_EMAIL:vladislav.petrow01@gmail.com}")
    private String fromEmail;
    public void sendMail(String subject, String body, String recipients) {
        try {
            List<NameValuePair> form = new ArrayList<>();
            form.add(new BasicNameValuePair("from", fromEmail));
            form.add(new BasicNameValuePair("to", recipients));
            form.add(new BasicNameValuePair("subject", subject));
            form.add(new BasicNameValuePair("text", body));
            
            System.out.println("=== MAILGUN REQUEST DETAILS ===");
            System.out.println("From: " + fromEmail);
            System.out.println("To: " + recipients);
            System.out.println("Subject: " + subject);
            System.out.println("Domain: " + domain);
            System.out.println("API Key: " + (apiKey != null ? apiKey.substring(0, Math.min(8, apiKey.length())) + "..." : "NULL"));
            
            postMessage(String.format("https://api.mailgun.net/v3/%s/messages", domain), form);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    public void sendWelcomeEmail(String recipientEmail, String firstName, String lastName) {
        System.out.println("=== SENDING WELCOME EMAIL ===");
        System.out.println("Recipient: " + recipientEmail);
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Domain: " + domain);
        System.out.println("From Email: " + fromEmail);
        
        String subject = "Добре дошли в CryptoMoti!";
        String body = String.format(
            "Здравейте %s %s,\n\n" +
            "Добре дошли в CryptoMoti - вашата платформа за недвижими имоти!\n\n" +
            "Благодарим ви, че се регистрирахте в нашата платформа. Сега можете да:\n" +
            "• Създавате обяви за недвижими имоти\n" +
            "• Търсите имоти според вашите критерии\n" +
            "• Използвате нашите разширени функции за търсене\n\n" +
            "Ако имате въпроси, не се колебайте да се свържете с нас.\n\n" +
            "С уважение,\n" +
            "Екипът на CryptoMoti",
            firstName, lastName
        );
        
        System.out.println("Subject: " + subject);
        System.out.println("Body length: " + body.length());
        
        sendMail(subject, body, recipientEmail);
        System.out.println("Welcome email sent successfully!");
    }
    
    public void sendInvoiceEmail(String recipientEmail, String firstName, String lastName, 
                                double amount, String currency, String subscriptionType) {
        String subject = "Потвърждение за плащане - CryptoMoti абонамент";
        String body = String.format(
            "Здравейте %s %s,\n\n" +
            "Благодарим ви за вашия абонамент в CryptoMoti!\n\n" +
            "Детайли за плащането:\n" +
            "• Тип абонамент: %s\n" +
            "• Сума: %.2f %s\n" +
            "• Дата на плащане: %s\n" +
            "• Статус: Потвърдено\n\n" +
            "Вашият абонамент е активен и ще изтече на %s.\n\n" +
            "Сега можете да:\n" +
            "• Създавате неограничен брой обяви\n" +
            "• Вашите обяви ще се показват на първо място\n" +
            "• Получавате PROMO етикет за вашите обяви\n\n" +
            "Ако имате въпроси, не се колебайте да се свържете с нас.\n\n" +
            "С уважение,\n" +
            "Екипът на CryptoMoti",
            firstName, lastName, subscriptionType, amount, currency, 
            new java.text.SimpleDateFormat("dd.MM.yyyy").format(new java.util.Date()),
            new java.text.SimpleDateFormat("dd.MM.yyyy").format(new java.util.Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000))
        );
        sendMail(subject, body, recipientEmail);
    }
    
    public void sendExpirationReminderEmail(String recipientEmail, String firstName, String lastName, 
                                          String subscriptionType, int daysLeft) {
        String subject = "Напомняне за изтичане на абонамента - CryptoMoti";
        String body = String.format(
            "Здравейте %s %s,\n\n" +
            "Напомняме ви, че вашият %s абонамент ще изтече след %d дни.\n\n" +
            "За да продължите да използвате нашите премиум услуги, моля обновете вашия абонамент:\n" +
            "• Създаване на неограничен брой обяви\n" +
            "• Приоритетно показване на вашите обяви\n" +
            "• PROMO етикет за вашите обяви\n\n" +
            "Можете да обновите вашия абонамент в профила си или да се свържете с нас за помощ.\n\n" +
            "Ако имате въпроси, не се колебайте да се свържете с нас.\n\n" +
            "С уважение,\n" +
            "Екипът на CryptoMoti",
            firstName, lastName, subscriptionType, daysLeft
        );
        sendMail(subject, body, recipientEmail);
    }

    private void postMessage(String URL, List<NameValuePair> form) throws Exception {
        if (form == null || URL == null || URL.trim().isEmpty()) {
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        org.apache.http.client.HttpClient client = buildHttpClient();
        HttpPost httpPost = new HttpPost(URL);
        HttpResponse response = null;
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            response = client.execute(httpPost);
            stringBuilder.append(getBody(response.getEntity().getContent()));
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("post mailgun messages ok:" + URL);
                System.out.println("Mailgun Response: " + stringBuilder);
            } else {
                System.out.println("Failed to post mailgun,status=" + statusCode);
                System.out.println("Response body: " + stringBuilder);
                throw new RuntimeException("post failed:" + URL + " Status: " + statusCode + " Response: " + stringBuilder.toString());
            }
        } finally {
            if (response != null && response.getEntity() != null) {
                response.getEntity().getContent();
            }
        }
    }

    private String getBody(InputStream inputStream) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    inputStream));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                result.append(inputLine);
                result.append("\n");
            }
            in.close();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return result.toString();
    }

    private HttpClient buildHttpClient() {
        return HttpClientBuilder
                .create()
                .setDefaultCredentialsProvider(getAuthProvider())
                .build();
    }

    private CredentialsProvider getAuthProvider() {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials("api",
                apiKey);
        provider.setCredentials(AuthScope.ANY, credentials);
        return provider;
    }
}
