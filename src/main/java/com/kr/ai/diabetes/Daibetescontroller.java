package com.kr.ai.diabetes;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/diabetes")
public class Daibetescontroller {
    private DiabetesService diabetesService;

    public void DiabetesController(DiabetesService diabetesService) {
        this.diabetesService = diabetesService;
    }

    public Daibetescontroller(DiabetesService diabetesService) {
        this.diabetesService = diabetesService;
    }
    @PostMapping("/predict")
    public ResponseEntity<?> predict(@RequestBody PatientData patientData) {
        Map<String, Object> result = diabetesService.classifyDiabetes(patientData);
        return ResponseEntity.ok(result);
    }
}
