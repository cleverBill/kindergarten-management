package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.FeeItemDTO;
import com.example.studentmanagement.dto.FeeRecordDTO;
import com.example.studentmanagement.entity.FeeItem;
import com.example.studentmanagement.service.FeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fee")
@RequiredArgsConstructor
public class FeeController {

    private final FeeService feeService;

    @GetMapping("/items")
    public Map<String, Object> getAllFeeItems() {
        List<FeeItemDTO> items = feeService.getAllFeeItems();
        return Map.of("success", true, "data", items);
    }

    @PostMapping("/items")
    public Map<String, Object> createFeeItem(@RequestBody Map<String, Object> request) {
        FeeItem item = new FeeItem();
        item.setName((String) request.get("name"));
        item.setType((String) request.get("type"));
        item.setAmount(new BigDecimal(request.get("amount").toString()));
        item.setGrade((String) request.get("grade"));
        item.setClassName((String) request.get("className"));
        if (request.get("deadline") != null) {
            item.setDeadline(LocalDate.parse(request.get("deadline").toString()));
        }
        item.setDescription((String) request.get("description"));

        FeeItemDTO dto = feeService.createFeeItem(item);
        return Map.of("success", true, "data", dto);
    }

    @GetMapping("/items/{itemId}/records")
    public Map<String, Object> getRecordsByFeeItem(@PathVariable Long itemId) {
        List<FeeRecordDTO> records = feeService.getRecordsByFeeItemId(itemId);
        return Map.of("success", true, "data", records);
    }

    @GetMapping("/students/{studentId}/records")
    public Map<String, Object> getRecordsByStudent(@PathVariable Long studentId) {
        List<FeeRecordDTO> records = feeService.getRecordsByStudentId(studentId);
        return Map.of("success", true, "data", records);
    }

    @PostMapping("/records/{recordId}/pay")
    public Map<String, Object> payFee(@PathVariable Long recordId, @RequestBody Map<String, String> request, @RequestHeader("X-User-Id") Long operatorId) {
        String paymentMethod = request.getOrDefault("paymentMethod", "cash");
        FeeRecordDTO record = feeService.payFee(recordId, paymentMethod, operatorId);
        return Map.of("success", true, "data", record);
    }

    @PostMapping("/items/{itemId}/batch-pay")
    public Map<String, Object> batchPay(@PathVariable Long itemId, @RequestBody Map<String, Object> request, @RequestHeader("X-User-Id") Long operatorId) {
        List<Integer> studentIdsRaw = (List<Integer>) request.get("studentIds");
        List<Long> studentIds = studentIdsRaw.stream().map(Integer::longValue).toList();
        String paymentMethod = (String) request.getOrDefault("paymentMethod", "cash");
        feeService.batchPayFee(itemId, studentIds, paymentMethod, operatorId);
        return Map.of("success", true);
    }

    @GetMapping("/statistics")
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = feeService.getFeeStatistics();
        return Map.of("success", true, "data", stats);
    }
}