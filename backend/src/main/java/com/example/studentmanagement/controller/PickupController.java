package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.PickupPersonDTO;
import com.example.studentmanagement.dto.PickupRecordDTO;
import com.example.studentmanagement.service.PickupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pickup")
@RequiredArgsConstructor
public class PickupController {

    private final PickupService pickupService;

    @GetMapping("/students/{studentId}/persons")
    public Map<String, Object> getPickupPersons(@PathVariable Long studentId) {
        List<PickupPersonDTO> persons = pickupService.getPickupPersons(studentId);
        return Map.of("success", true, "data", persons);
    }

    @PostMapping("/students/{studentId}/persons")
    public Map<String, Object> addPickupPerson(@PathVariable Long studentId, @RequestBody Map<String, String> request) {
        String name = request.get("name");
        String phone = request.get("phone");
        String relation = request.get("relation");
        String idCard = request.get("idCard");

        PickupPersonDTO person = pickupService.addPickupPerson(studentId, name, phone, relation, idCard);
        return Map.of("success", true, "data", person);
    }

    @DeleteMapping("/persons/{personId}")
    public Map<String, Object> deletePickupPerson(@PathVariable Long personId) {
        pickupService.deletePickupPerson(personId);
        return Map.of("success", true);
    }

    @GetMapping("/students/{studentId}/records")
    public Map<String, Object> getRecordsByStudent(@PathVariable Long studentId) {
        List<PickupRecordDTO> records = pickupService.getRecordsByStudent(studentId);
        return Map.of("success", true, "data", records);
    }

    @GetMapping("/records/today")
    public Map<String, Object> getTodayRecords() {
        List<PickupRecordDTO> records = pickupService.getTodayRecords();
        return Map.of("success", true, "data", records);
    }

    @PostMapping("/records")
    public Map<String, Object> createRecord(@RequestBody Map<String, Object> request, @RequestHeader("X-User-Id") Long operatorId) {
        Long studentId = Long.valueOf(request.get("studentId").toString());
        Long pickupPersonId = request.get("pickupPersonId") != null ? Long.valueOf(request.get("pickupPersonId").toString()) : null;
        String pickupType = (String) request.get("pickupType");
        String pickupMethod = (String) request.getOrDefault("pickupMethod", "manual");

        PickupRecordDTO record = pickupService.createPickupRecord(studentId, pickupPersonId, pickupType, pickupMethod, operatorId);
        return Map.of("success", true, "data", record);
    }

    @GetMapping("/statistics/today")
    public Map<String, Object> getTodayStatistics() {
        Map<String, Object> stats = pickupService.getTodayStatistics();
        return Map.of("success", true, "data", stats);
    }
}