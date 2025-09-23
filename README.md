**Diabetes Classification POC using Spring Boot and Azure OpenAI**

---

**Objective:**
Develop a Spring Boot REST API that classifies diabetes risk based on patient profile using Azure OpenAI GPT-5-mini (Chat Model).

**Technology Stack:**
- Java 17
- Spring Boot 3.5.6
- Azure OpenAI (gpt-5-mini)
- Maven

**Endpoints:**
- POST `/predict` - Accepts patient data and returns diabetes risk with patient profile.

---

### 1. Sample Request JSON

json
{
  "age": 50,
  "weight": 180,
  "height": 5.7,
  "glucose": 140,
  "bloodPressure": 85,
  "insulin": 90
}


### 2. Sample Response JSON

json
{
  "patientProfile": {
    "age": 50,
    "weight": 180,
    "height": 5.7,
    "bmi": 29.39,
    "glucose": 140,
    "bloodPressure": 85,
    "insulin": 90
  },
  "diabetesRisk": {
    "riskLevel": "High",
    "probability": 0.78
  }
}


### 3. Flow Diagram


+--------------------+        +------------------------+        +--------------------+
|  Patient sends     | POST   |  Spring Boot REST API  |  -->   |  DiabetesService   |
|  JSON /predict     |------->|  /predict endpoint     |        |  processes data,   |
+--------------------+        +------------------------+        |  calculates BMI,   |
                                                                  |  calls Azure OpenAI |
                                                                  |  Chat model         |
                                                                  +--------------------+
                                                                             |
                                                                             V
                                                                  +--------------------+
                                                                  |  Azure OpenAI      |
                                                                  |  GPT-5-mini Chat   |
                                                                  |  Model returns     |
                                                                  |  risk JSON         |
                                                                  +--------------------+
                                                                             |
                                                                             V
                                                                  +--------------------+
                                                                  |  Service combines  |
                                                                  |  patient profile & |
                                                                  |  risk and returns  |
                                                                  +--------------------+
                                                                             |
                                                                             V
                                                                  +--------------------+
                                                                  |  Response to Client|
                                                                  +--------------------+


### 4. Key Implementation Points

- Height input in feet, converted to meters for BMI calculation.
- Patient profile returned with BMI, glucose, blood pressure, insulin.
- Diabetes risk returned as JSON with `riskLevel` and `probability`.
- Uses **Azure OpenAI GPT-5-mini chat model** via `/chat/completions` endpoint.

---

### 5. Sample Spring Boot Service Snippet

java
// inside DiabetesService
if (patientData.getBmi() == 0 && patientData.getHeight() > 0) {
    double heightInMeters = patientData.getHeight() * 0.3048;
    double bmi = patientData.getWeight() / (heightInMeters * heightInMeters);
    patientData.setBmi(Math.round(bmi * 100.0) / 100.0);
}


Map<String, Object> message = Map.of("role", "user", "content", prompt);
Map<String, Object> requestBody = Map.of("messages", List.of(message), "max_tokens", 256);
RestTemplate restTemplate = new RestTemplate();
String rawResponse = restTemplate.postForObject(url, entity, String.class);

---

**This POC demonstrates:**
- How to integrate Spring Boot with Azure OpenAI chat models.
- How to process patient data and calculate BMI.
- How to return structured risk prediction with patient profile.
