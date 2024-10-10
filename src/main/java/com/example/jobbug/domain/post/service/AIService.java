package com.example.jobbug.domain.post.service;

import com.example.jobbug.domain.post.enums.EmoticonEnum;
import com.example.jobbug.global.s3.S3Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIService {

    @Value("${roboflow.apiKey}")
    private String apiKey;

    @Value("${roboflow.modelName}")
    private String modelName;

    @Value("${roboflow.version}")
    private String version;

    private final S3Service s3Service;

    public String generateEditedImage(String imageUrl, int emoticonNum) throws IOException {
        String detectionData = getAIData(imageUrl);
        BufferedImage originalImage = ImageIO.read(new URL(imageUrl));
        File updatedImage = addEmoticonsToImage(originalImage, detectionData, emoticonNum);
        return s3Service.upload(updatedImage, "ai");
    }

    private String getAIData(String imageUrl) throws IOException {
        String roboflowApiUrl = String.format("https://detect.roboflow.com/%s/%s?api_key=%s&image=%s", modelName, version, apiKey, imageUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>("", headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.postForEntity(roboflowApiUrl, entity, String.class);
        return response.getBody();
    }

    private File addEmoticonsToImage(BufferedImage originalImage, String detectionData, int emoticonNum) throws IOException {
        List<Map<String, Object>> detections = parseResponse(detectionData);
        String emoticonUrl = EmoticonEnum.getUrlByNumber(emoticonNum);
        BufferedImage emoticonImage = ImageIO.read(new URL(emoticonUrl));

        Graphics graphics = originalImage.getGraphics();
        for (Map<String, Object> detection : detections) {
            int centerX = (int) detection.get("x");
            int centerY = (int) detection.get("y");
            int width = (int) detection.get("width");
            int height = (int) detection.get("height");

            int x = centerX - (width / 2);
            int y = centerY - (height / 2);

            graphics.drawImage(emoticonImage, x, y, width, height, null);
        }
        graphics.dispose();

        String fileName = "ai_" + System.currentTimeMillis() + ".png";
        return convertImageToFile(originalImage, fileName);
    }

    private File convertImageToFile(BufferedImage image, String fileName) throws IOException {
        File outputFile = new File(System.getProperty("java.io.tmpdir") + File.separator + fileName);
        ImageIO.write(image, "png", outputFile);
        return outputFile;
    }

    private List<Map<String, Object>> parseResponse(String detectionData) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(detectionData);
        JsonNode predictions = rootNode.path("predictions");

        List<Map<String, Object>> detectionResults = new ArrayList<>();
        if (predictions.isArray()) {
            for (JsonNode prediction : predictions) {
                int x = prediction.path("x").asInt();
                int y = prediction.path("y").asInt();
                int width = prediction.path("width").asInt();
                int height = prediction.path("height").asInt();

                detectionResults.add(Map.of("x", x, "y", y, "width", width, "height", height));
            }
        }
        return detectionResults;
    }
}
