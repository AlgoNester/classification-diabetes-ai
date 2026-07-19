# Diabetes Risk Classification — Spring Boot + Azure OpenAI

A Spring Boot REST API POC that classifies diabetes risk from patient
health data using Azure OpenAI GPT-5-mini as the AI reasoning engine.

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
Azure OpenAI (GPT-5-mini)
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
| Framework | Spring Boot 3.5.6 |
| AI Service | Azure OpenAI (GPT-5-mini) |
| HTTP Client | Spring RestTemplate |
| Build Tool | Maven |

---

## API Reference

### POST `/api/diabetes/predict`

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
  }
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
Classify diabetes risk based on patient data: {patientDataMap}.
Return JSON: {"riskLevel": "Low|Medium|High", "probability": 0.0-1.0}
```

The patient data (including auto-calculated BMI) is serialized as a map and
embedded directly in the prompt. The model returns a structured JSON risk
assessment that is parsed and combined with the patient profile.

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
Uses Spring `RestTemplate` to call the Azure OpenAI Chat Completions API:
```java
RestTemplate restTemplate = new RestTemplate();
String url = endpoint + "/openai/deployments/" + deploymentName
    + "/chat/completions?api-version=2023-05-15";
String rawResponse = restTemplate.postForObject(url, entity, String.class);
```

---

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- Azure account with OpenAI resource and GPT-5-mini deployment

### Configuration

Copy and configure:
```bash
cp src/main/resources/application.properties.example \
   src/main/resources/application.properties
```

```properties
# application.properties
azure.openai.endpoint=https://your-resource.openai.azure.com/
azure.openai.key=your-api-key-here
azure.openai.deployment=gpt-5-mini
```

### Run
```bash
./mvnw spring-boot:run
```

### Test
```bash
curl -X POST http://localhost:8080/api/diabetes/predict \
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
| POST `/api/diabetes/predict` endpoint | ✅ Complete |
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
[LinkedIn](linkedin.com/in/karunakar-b-reddy-437556413)
