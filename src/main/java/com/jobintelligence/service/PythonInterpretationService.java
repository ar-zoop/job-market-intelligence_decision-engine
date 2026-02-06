package com.jobintelligence.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Calls a local Python service to interpret raw resume text and job description text
 * into JSON (ResumeProfile / JobProfile). Expects POST /analyze-resume and POST /analyze-job
 * with raw text in the body; returns response body as JSON string.
 */
public class PythonInterpretationService {

    private static final String DEFAULT_BASE_URL = "http://localhost:8000";
    private final String baseUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public PythonInterpretationService() {
        this(DEFAULT_BASE_URL);
    }

    public PythonInterpretationService(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Sends raw resume text to the Python app for interpretation.
     * Returns the response body as JSON string for the caller to parse into ResumeProfile.
     *
     * @param resumeText raw resume text
     * @return Optional with JSON string on success; empty on server-down or timeout
     */
    public Optional<String> interpretResumeText(String resumeText) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("resume_text", resumeText);
            String jsonBody = objectMapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/analyze-resume"))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(30))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return Optional.of(response.body());
            } else {
                return Optional.empty();
            }
        } catch (HttpTimeoutException e) {
            return Optional.empty();
        } catch (ConnectException e) {
            return Optional.empty();
        } catch (Exception e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            if (cause instanceof HttpTimeoutException || cause instanceof ConnectException) {
                return Optional.empty();
            }
            throw new RuntimeException("Failed to call Python resume interpret endpoint: " + e.getMessage(), e);
        }
    }

    /**
     * Sends raw job description text to the Python app for interpretation.
     * Returns the response body as JSON string for the caller to parse into JobProfile.
     *
     * @param jobText raw job description text
     * @return Optional with JSON string on success; empty on server-down or timeout
     */
    public Optional<String> interpretJobText(String jobText) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("job_description", jobText);
            String jsonBody = objectMapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/analyze-job"))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(30))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return Optional.of(response.body());
            } else {
                return Optional.empty();
            }
        } catch (HttpTimeoutException e) {
            return Optional.empty();
        } catch (ConnectException e) {
            return Optional.empty();
        } catch (Exception e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            if (cause instanceof HttpTimeoutException || cause instanceof ConnectException) {
                return Optional.empty();
            }
            throw new RuntimeException("Failed to call Python job interpret endpoint: " + e.getMessage(), e);
        }
    }
}
