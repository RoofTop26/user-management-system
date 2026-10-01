package com.example.user_management_system.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class EmailService {

    private final String apiKey;
    private final String domain;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public EmailService(@Value("${mailgun.api-key}") String apiKey, @Value("${mailgun.domain}") String domain) {
        this.apiKey = apiKey;
        this.domain = domain;
    }

    public void sendResetPasswordEmail(String toEmail, String resetLink) {
        sendEmail(toEmail, "Đặt lại mật khẩu",
                "Nhấn vào link sau để đặt lại mật khẩu: <a href=\"" + resetLink + "\">" + resetLink + "</a>");
    }

    public void sendVerificationEmail(String toEmail, String verifyLink) {
        sendEmail(toEmail, "Xác nhận đăng ký tài khoản",
                "Nhấn vào link sau để xác nhận đăng ký tài khoản: <a href=\"" + verifyLink + "\">" + verifyLink + "</a>");
    }

    @Async
    public void sendEmail(String toEmail, String subject, String htmlBody) {
        String credentials = Base64.getEncoder().encodeToString(("api:" + apiKey).getBytes(StandardCharsets.UTF_8));

        String form = "from=" + encode("User Management System <no-reply@" + domain + ">")
                + "&to=" + encode(toEmail)
                + "&subject=" + encode(subject)
                + "&html=" + encode(htmlBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.mailgun.net/v3/" + domain + "/messages"))
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Mailgun status: " + response.statusCode());
            System.out.println("Mailgun response: " + response.body());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String encode(String value) {
        return java.net.URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
