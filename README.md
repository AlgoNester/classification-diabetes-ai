# Diabetes Risk Classification — Spring Boot + Azure OpenAI

A Spring Boot REST API POC that classifies diabetes risk from patient
health data using Azure OpenAI GPT-4o-mini as the AI reasoning engine.

> **Demonstrates:** Java Spring Boot · Azure OpenAI Integration ·
> Healthcare Domain · REST API Design · AI-Powered Clinical POC

---

## Why This Project

Traditional diabetes classification uses trained ML models (Random Forest,
Logistic Regression) on labeled datasets. This POC explores an alternative
approach — using an Azure OpenAI chat model as the classification engine,
passing structured patient data via a carefully engineered prompt and
returning a structured JSON risk assessment.

**Key insight:** Azure OpenAI can reason over clinical parameters and return
calibrated risk levels when given precise, structured prompts — making it
useful for rapid prototyping of AI-assisted clinical decision support tools.

> ⚠️ **Disclaimer:** This is a proof-of-concept for technical demonstration
> only. It is NOT a medical diagnostic tool and should NOT be used for
> clinical decision-making. Always consult a qualified healthcare professional.

---

## Architecture

```
Client (JSON)
     │
     │  POST /predict
     ▼
Spring Boot REST API
     │
     │  BMI Calculation
     │  Prompt Engineering
     ▼
Azure OpenAI (GPT-4o-mini)
     │
     │  Structured JSON Response
     ▼
DiabetesService
     │  Combines patient profile + AI risk output
     ▼
Client Response (patientProfile + diabetesRisk)
```

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.3.x |
| AI Service | Azure OpenAI (GPT-4o-mini) |
| HTTP Client | Spring RestClient (Spring 6.1+) |
| Build Tool | Maven |

---

## API Reference

### POST `/predict`

Accepts patient health data and returns diabetes risk classification.

**Request Body:**
```json
{
  "age": 50,
  "weight": 180,
  "height": 5.7,
  "glucose": 140,
  "bloodPressure": 85,
  "insulin": 90
}
```

| Field | Type | Unit | Description |
|---|---|---|---|
| `age` | int | years | Patient age |
| `weight` | double | lbs | Body weight |
| `height` | double | feet | Patient height |
| `glucose` | int | mg/dL | Plasma glucose level |
| `bloodPressure` | int | mmHg | Diastolic blood pressure |
| `insulin` | int | µU/mL | 2-hour serum insulin |

**Response:**
```json
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
  },
  "disclaimer": "AI-generated estimate for informational purposes only. Not a medical diagnosis."
}
```

| `riskLevel` | Meaning |
|---|---|
| `Low` | Low clinical indicators for diabetes |
| `Medium` | Moderate risk — monitoring recommended |
| `High` | Elevated risk — clinical consultation advised |

---

## AI Prompt Design

The service constructs a structured clinical prompt sent to Azure OpenAI:

```
You are a clinical decision support assistant.
Given the following patient data:
- Age: {age} years
- BMI: {bmi} (calculated from height/weight)
- Fasting Glucose: {glucose} mg/dL
- Blood Pressure: {bloodPressure} mmHg
- Insulin: {insulin} µU/mL

Assess this patient's diabetes risk.
Return ONLY valid JSON in this exact format, no explanation:
{"riskLevel": "Low|Medium|High", "probability": 0.0-1.0}
```

---

## Key Implementation Details

**BMI Calculation:**
Height input is in feet, converted to meters before BMI calculation:
```java
double heightInMeters = patientData.getHeight() * 0.3048;
double bmi = patientData.getWeight() / (heightInMeters * heightInMeters);
patientData.setBmi(Math.round(bmi * 100.0) / 100.0);
```

**Azure OpenAI Call:**
Uses Spring `RestClient` (Spring 6.1+) injected as a `@Bean`:
```java
String response = restClient.post()
    .uri("/openai/deployments/{model}/chat/completions?api-version=2024-02-01",
         deploymentName)
    .body(requestBody)
    .retrieve()
    .body(String.class);
```

---

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- Azure account with OpenAI resource and GPT-4o-mini deployment

### Configuration

Copy and configure:
```bash
cp src/main/resources/application.properties.example \
   src/main/resources/application.properties
```

```properties
# application.properties
azure.openai.endpoint=https://your-resource.openai.azure.com/
azure.openai.api-key=your-api-key-here
azure.openai.deployment-name=gpt-4o-mini
```

### Run
```bash
./mvnw spring-boot:run
```

### Test
```bash
curl -X POST http://localhost:8080/predict \
  -H "Content-Type: application/json" \
  -d '{
    "age": 50,
    "weight": 180,
    "height": 5.7,
    "glucose": 140,
    "bloodPressure": 85,
    "insulin": 90
  }'
```

---

## Project Status

| Feature | Status |
|---|---|
| POST `/predict` endpoint | ✅ Complete |
| BMI auto-calculation | ✅ Complete |
| Azure OpenAI integration | ✅ Complete |
| Structured prompt engineering | ✅ Complete |
| Input validation | 🚧 In Progress |
| Error handling / fallback | 🚧 In Progress |
| Unit tests | 🔜 Planned |
| Swagger/OpenAPI docs | 🔜 Planned |

---


---

## Author

**Karunakar Bommareddy**
Senior Software Engineer | Healthcare Payer | Azure AI

[GitHub](https://github.com/AlgoNester) •
[LinkedIn](https://linkedin.com/in/yourprofile)
