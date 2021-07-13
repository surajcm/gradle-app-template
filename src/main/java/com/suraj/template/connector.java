package com.suraj.template;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class connector {
    private static connector single_instance = null;
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private static final String AUTH = "<TOKEN>";

    private connector() {
    }

    public static connector getInstance() {
        if (single_instance == null) {
            single_instance = new connector();
        }
        return single_instance;
    }

    public static String getResponse(String id) {
        String url = "http://<endpoint>/";
        String urlPart = "/breadcrumbs";
        final HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url+id+urlPart))
                .setHeader("accept", "application/json")
                .setHeader("authorization", "JWT-Bearer "+AUTH)
                .build();
        final HttpResponse<String> response;
        ObjectMapper objectMapper = new ObjectMapper();
        StringBuilder builder = new StringBuilder();
        try {
            response = HTTP_CLIENT
                    .send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
