package com.suraj.template;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class Connector {
    private static final Logger logger = LogManager.getLogger(Connector.class);

    private static Connector singleInstance = null;
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private static final String AUTH = "<TOKEN>";

    private Connector() {
    }

    public static Connector getInstance() {
        if (singleInstance == null) {
            singleInstance = new Connector();
        }
        return singleInstance;
    }

    public static String getResponse(String id) {
        String url = "http://<endpoint>/";
        String urlPart = "/breadcrumbs";
        final HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + id + urlPart))
                .setHeader("accept", "application/json")
                .setHeader("authorization", "JWT-Bearer " + AUTH)
                .build();
        final HttpResponse<String> response;
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        StringBuilder builder = new StringBuilder();
        try {
            response = HTTP_CLIENT
                    .send(request, HttpResponse.BodyHandlers.ofString());
            logger.info(response.body());
            builder.append(mapper.readValue(response.body(), String.class));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
