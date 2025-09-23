package com.kr.ai.diabetes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiabetesService {

    @Value("${azure.openai.key}")
    private String apiKey;

    @Value("${azure.openai.endpoint}")
    private String endpoint;

    @Value("${azure.openai.deployment}")
    private String deploymentName;

    private final ObjectMapper mapper = new ObjectMapper();

    public Map<String, Object> classifyDiabetes(PatientData patientData) {
        try {
            // Calculate BMI if not already set
            if (patientData.getBmi() == 0 && patientData.getHeight() > 0) {
                // Convert height from feet to meters
                double heightInMeters = patientData.getHeight() * 0.3048; // 1 ft = 0.3048 m
                double bmi = patientData.getWeight() / (heightInMeters * heightInMeters);
                patientData.setBmi(Math.round(bmi * 100.0) / 100.0);
            }

            // Convert patient profile to Map
            Map<String, Object> patientMap = mapper.convertValue(patientData, Map.class);

            // Build prompt for chat model
            String prompt = "Classify diabetes risk based on patient data: " + patientMap +
                    ". Return JSON: {\"riskLevel\": \"Low|Medium|High\", \"probability\": 0.0-1.0}";

            // Prepare REST headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", apiKey);

            // Prepare chat messages
            Map<String, Object> message = Map.of(
                    "role", "user",
                    "content", prompt
            );

            Map<String, Object> requestBody = Map.of(
                    "messages", List.of(message)
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Call Azure OpenAI Chat Completions endpoint
            RestTemplate restTemplate = new RestTemplate();
            String url = endpoint + "/openai/deployments/" + deploymentName + "/chat/completions?api-version=2023-05-15";
            String rawResponse = restTemplate.postForObject(url, entity, String.class);

            // Parse the chat model response
            Map<String, Object> responseMap = mapper.readValue(rawResponse, Map.class);
            List choices = (List) responseMap.get("choices");
            Map choice0 = (Map) choices.get(0);
            Map messageMap = (Map) choice0.get("message");
            String modelContent = (String) messageMap.get("content");

            // Parse the JSON returned by the model
            Map<String, Object> riskResult = mapper.readValue(modelContent, Map.class);

            // Combine patient profile + risk
            Map<String, Object> finalResult = new HashMap<>();
            finalResult.put("patientProfile", patientMap);
            finalResult.put("diabetesRisk", riskResult);

            return finalResult;

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", "Failed to classify diabetes");
        }
    }
}
