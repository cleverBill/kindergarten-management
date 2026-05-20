package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.FeeItemDTO;
import com.example.studentmanagement.dto.FeeRecordDTO;
import com.example.studentmanagement.entity.FeeItem;
import com.example.studentmanagement.entity.FeeRecord;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeeService {

    private final FeeItemRepository feeItemRepository;
    private final FeeRecordRepository feeRecordRepository;
    private final StudentRepository studentRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public List<FeeItemDTO> getAllFeeItems() {
        return feeItemRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public FeeItemDTO createFeeItem(FeeItem item) {
        item.setCreatedAt(LocalDateTime.now());
        item.setStatus("active");
        item = feeItemRepository.save(item);

        if (item.getGrade() != null || item.getClassName() != null) {
            generateRecordsForStudents(item);
        }

        return convertToDTO(item);
    }

    @Transactional
    public void generateRecordsForStudents(FeeItem item) {
        List<Student> students = studentRepository.findAll();
        for (Student student : students) {
            boolean match = true;
            if (item.getGrade() != null && !item.getGrade().equals(student.getGrade())) {
                match = false;
            }
            if (item.getClassName() != null && !item.getClassName().equals(student.getClassName())) {
                match = false;
            }

            if (match) {
                Optional<FeeRecord> existing = feeRecordRepository.findByFeeItemIdAndStudentId(item.getId(), student.getId());
                if (existing.isEmpty()) {
                    FeeRecord record = new FeeRecord();
                    record.setFeeItemId(item.getId());
                    record.setStudentId(student.getId());
                    record.setAmount(item.getAmount());
                    record.setStatus("unpaid");
                    record.setCreatedAt(LocalDateTime.now());
                    feeRecordRepository.save(record);
                }
            }
        }
    }

    public List<FeeRecordDTO> getRecordsByFeeItemId(Long feeItemId) {
        return feeRecordRepository.findByFeeItemId(feeItemId).stream()
                .map(this::convertRecordToDTO)
                .collect(Collectors.toList());
    }

    public List<FeeRecordDTO> getRecordsByStudentId(Long studentId) {
        return feeRecordRepository.findByStudentId(studentId).stream()
                .map(this::convertRecordToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public FeeRecordDTO payFee(Long recordId, String paymentMethod, Long operatorId) {
        FeeRecord record = feeRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("缴费记录不存在"));
        record.setStatus("paid");
        record.setPaidAt(LocalDateTime.now());
        record.setPaymentMethod(paymentMethod);
        record.setOperatorId(operatorId);
        record = feeRecordRepository.save(record);
        return convertRecordToDTO(record);
    }

    @Transactional
    public void batchPayFee(Long feeItemId, List<Long> studentIds, String paymentMethod, Long operatorId) {
        for (Long studentId : studentIds) {
            Optional<FeeRecord> optRecord = feeRecordRepository.findByFeeItemIdAndStudentId(feeItemId, studentId);
            if (optRecord.isPresent()) {
                FeeRecord record = optRecord.get();
                if ("unpaid".equals(record.getStatus()) || "overdue".equals(record.getStatus())) {
                    record.setStatus("paid");
                    record.setPaidAt(LocalDateTime.now());
                    record.setPaymentMethod(paymentMethod);
                    record.setOperatorId(operatorId);
                    feeRecordRepository.save(record);
                }
            }
        }
    }

    public Map<String, Object> getFeeStatistics() {
        Map<String, Object> stats = new HashMap<>();
        List<FeeRecord> allRecords = feeRecordRepository.findAll();

        BigDecimal totalAmount = allRecords.stream()
                .map(FeeRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal paidAmount = allRecords.stream()
                .filter(r -> "paid".equals(r.getStatus()))
                .map(FeeRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long paidCount = allRecords.stream().filter(r -> "paid".equals(r.getStatus())).count();
        long unpaidCount = allRecords.stream().filter(r -> "unpaid".equals(r.getStatus()) || "overdue".equals(r.getStatus())).count();

        stats.put("totalAmount", totalAmount);
        stats.put("paidAmount", paidAmount);
        stats.put("unpaidAmount", totalAmount.subtract(paidAmount));
        stats.put("paidCount", paidCount);
        stats.put("unpaidCount", unpaidCount);
        stats.put("totalCount", allRecords.size());

        return stats;
    }

    private FeeItemDTO convertToDTO(FeeItem item) {
        FeeItemDTO dto = new FeeItemDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setType(item.getType());
        dto.setAmount(item.getAmount());
        dto.setGrade(item.getGrade());
        dto.setClassName(item.getClassName());
        dto.setDeadline(item.getDeadline());
        dto.setDescription(item.getDescription());
        dto.setStatus(item.getStatus());
        dto.setCreatedAt(item.getCreatedAt().format(formatter));

        dto.setPaidCount(feeRecordRepository.countPaidByFeeItemId(item.getId()));
        dto.setUnpaidCount(feeRecordRepository.countUnpaidByFeeItemId(item.getId()));
        dto.setTotalAmount(item.getAmount().multiply(new BigDecimal(dto.getPaidCount() + dto.getUnpaidCount())));
        dto.setPaidAmount(item.getAmount().multiply(new BigDecimal(dto.getPaidCount())));

        return dto;
    }

    private FeeRecordDTO convertRecordToDTO(FeeRecord record) {
        FeeRecordDTO dto = new FeeRecordDTO();
        dto.setId(record.getId());
        dto.setFeeItemId(record.getFeeItemId());
        dto.setStudentId(record.getStudentId());
        dto.setAmount(record.getAmount());
        dto.setStatus(record.getStatus());
        dto.setPaymentMethod(record.getPaymentMethod());
        dto.setRemark(record.getRemark());
        if (record.getPaidAt() != null) {
            dto.setPaidAt(record.getPaidAt().format(formatter));
        }

        feeItemRepository.findById(record.getFeeItemId()).ifPresent(f -> dto.setFeeItemName(f.getName()));
        studentRepository.findById(record.getStudentId()).ifPresent(s -> dto.setStudentName(s.getName()));

        return dto;
    }
}