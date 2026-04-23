package services;

import models.MediaFile;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;

// Handles advanced multimodal AI interactions using Google Gemini API.
// Features automatic retry logic and strict character limits to ensure TTS compatibility.
public class GeminiImplementation implements AIService {
    private final String apiKey;
    private final HttpClient client;

    public GeminiImplementation(String apiKey) {
        this.apiKey = apiKey;
        this.client = HttpClient.newHttpClient();
    }

    @Override
    public String generateIntroImage(String theme) {
        try {
            String prompt = "Vertical photorealistic epic journey adventure landscape " + theme;
            String encodedPrompt = prompt.replace(" ", "%20");
            String urlString = "https://image.pollinations.ai/prompt/" + encodedPrompt + "?width=1080&height=1920&nologo=true";
            
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlString)).GET().build();
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            
            Path outputPath = Paths.get("process/intro_image.jpg");
            Files.copy(response.body(), outputPath, StandardCopyOption.REPLACE_EXISTING);
            return outputPath.toAbsolutePath().toString();
        } catch (Exception e) {
            System.out.println("Intro Image Error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String generateDescription(MediaFile file, int current, int total) {
        // Strict character limit imposed to prevent Google TTS HTTP 400 Bad Request errors.
        String prompt = "Act as a documentary narrator. Look at this exact image and describe precisely what you see visually (terrain, elements, colors). DO NOT use lists. CRITICAL RULE: Your entire response MUST be strictly under 150 characters.";
        
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(file.getFilePath()));
            String base64Image = Base64.getEncoder().encodeToString(fileBytes);
            String mimeType = file.getFilePath().toLowerCase().endsWith(".png") ? "image/png" : "image/jpeg";
            
            String safePrompt = prompt.replace("\"", "\\\"");
            String jsonBody = "{" +
                "\"contents\": [{" +
                    "\"parts\": [" +
                        "{\"text\": \"" + safePrompt + "\"}," +
                        "{\"inline_data\": {\"mime_type\": \"" + mimeType + "\", \"data\": \"" + base64Image + "\"}}" +
                    "]" +
                "}]" +
            "}";

            return sendWithRetry(jsonBody, "A breathtaking visual chapter of our journey, captured at this unique geographical point.");
            
        } catch (Exception e) {
            System.out.println("Error processing image data: " + file.getFilePath());
            return "error processing image data.";
        }
    }

    @Override
    public String generateInspirationalText(List<MediaFile> files) {
        // Strict character limit imposed to prevent Google TTS HTTP 400 Bad Request errors.
        String prompt = "Write a cinematic travel summary concluding our journey. End with a reflection about human exploration. CRITICAL RULE: Your entire response MUST be strictly under 150 characters.";
        String safePrompt = prompt.replace("\"", "\\\"");
        String jsonBody = "{ \"contents\": [{ \"parts\": [{\"text\": \"" + safePrompt + "\"}] }] }";
        
        return sendWithRetry(jsonBody, "Exploration transforms us. We return home with a new perspective, ready for the next adventure.");
    }

    private String sendWithRetry(String jsonBody, String fallbackText) {
        String url = "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=" + apiKey;
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        int maxRetries = 3;
        int waitTimeMs = 5000;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                
                if (response.statusCode() == 200) {
                    return extractTextFromJson(response.body(), fallbackText);
                } 
                else if (response.statusCode() == 429) {
                    System.out.println("API Rate Limit Hit (429). Attempt " + attempt + " of " + maxRetries + ". Waiting 5s...");
                    Thread.sleep(waitTimeMs);
                } 
                else {
                    System.out.println("Google API Status: " + response.statusCode() + " - " + response.body());
                    return fallbackText;
                }
            } catch (Exception e) {
                System.out.println("Network failure: " + e.getMessage());
            }
        }
        return fallbackText;
    }

    private String extractTextFromJson(String json, String fallback) {
        try {
            java.util.regex.Matcher m = java.util.regex.Pattern.compile("\"text\":\\s*\"(.*?)\"").matcher(json);
            if (m.find()) {
                return m.group(1).replace("\\n", " ").replaceAll("[*#]", "").trim();
            }
            return fallback;
        } catch (Exception e) {
            return fallback;
        }
    }
}