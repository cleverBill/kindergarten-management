package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.PickupPersonDTO;
import com.example.studentmanagement.dto.PickupRecordDTO;
import com.example.studentmanagement.entity.PickupPerson;
import com.example.studentmanagement.entity.PickupRecord;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.repository.PickupPersonRepository;
import com.example.studentmanagement.repository.PickupRecordRepository;
import com.example.studentmanagement.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PickupService {

    private final PickupPersonRepository personRepository;
    private final PickupRecordRepository recordRepository;
    private final StudentRepository studentRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public List<PickupPersonDTO> getPickupPersons(Long studentId) {
        return personRepository.findByStudentId(studentId).stream()
                .map(this::convertPersonToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PickupPersonDTO addPickupPerson(Long studentId, String name, String phone, String relation, String idCard) {
        PickupPerson person = new PickupPerson();
        person.setStudentId(studentId);
        person.setName(name);
        person.setPhone(phone);
        person.setRelation(relation);
        person.setIdCard(idCard);
        person.setIsVerified(false);
        person.setCreatedAt(LocalDateTime.now());
        person = personRepository.save(person);
        return convertPersonToDTO(person);
    }

    @Transactional
    public void deletePickupPerson(Long personId) {
        personRepository.deleteById(personId);
    }

    public List<PickupRecordDTO> getRecordsByStudent(Long studentId) {
        return recordRepository.findByStudentIdOrderByPickupTimeDesc(studentId).stream()
                .map(this::convertRecordToDTO)
                .collect(Collectors.toList());
    }

    public List<PickupRecordDTO> getTodayRecords() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);
        return recordRepository.findByDateRange(start, end).stream()
                .map(this::convertRecordToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PickupRecordDTO createPickupRecord(Long studentId, Long pickupPersonId, String pickupType, String pickupMethod, Long operatorId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("学生不存在"));

        PickupRecord record = new PickupRecord();
        record.setStudentId(studentId);
        record.setPickupType(pickupType);
        record.setPickupTime(LocalDateTime.now());
        record.setPickupMethod(pickupMethod);
        record.setOperatorId(operatorId);
        record.setCreatedAt(LocalDateTime.now());

        String status = "normal";
        String remark = "";

        if (pickupPersonId != null) {
            PickupPerson person = personRepository.findById(pickupPersonId).orElse(null);
            if (person != null) {
                record.setPickupPersonId(pickupPersonId);
                record.setPickupPersonName(person.getName());

                List<PickupPerson> verifiedPersons = personRepository.findByStudentIdAndIsVerified(studentId, true);
                boolean isVerifiedPerson = verifiedPersons.stream().anyMatch(p -> p.getId().equals(pickupPersonId));

                if (!isVerifiedPerson) {
                    status = "abnormal";
                    remark = "未备案人员接送";
                }
            }
        } else {
            status = "abnormal";
            remark = "临时/陌生人接送";
        }

        record.setStatus(status);
        record.setRemark(remark);
        record = recordRepository.save(record);
        return convertRecordToDTO(record);
    }

    public Map<String, Object> getTodayStatistics() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);

        Map<String, Object> stats = Map.of(
            "totalIn", recordRepository.countByDateRangeAndStatus(start, end, "in"),
            "totalOut", recordRepository.countByDateRangeAndStatus(start, end, "out"),
            "normalCount", recordRepository.countByDateRangeAndStatus(start, end, "normal"),
            "abnormalCount", recordRepository.countByDateRangeAndStatus(start, end, "abnormal")
        );
        return stats;
    }

    private PickupPersonDTO convertPersonToDTO(PickupPerson person) {
        PickupPersonDTO dto = new PickupPersonDTO();
        dto.setId(person.getId());
        dto.setStudentId(person.getStudentId());
        dto.setName(person.getName());
        dto.setPhone(person.getPhone());
        dto.setRelation(person.getRelation());
        dto.setIdCard(person.getIdCard());
        dto.setIsVerified(person.getIsVerified());

        studentRepository.findById(person.getStudentId()).ifPresent(s -> dto.setStudentName(s.getName()));
        return dto;
    }

    private PickupRecordDTO convertRecordToDTO(PickupRecord record) {
        PickupRecordDTO dto = new PickupRecordDTO();
        dto.setId(record.getId());
        dto.setStudentId(record.getStudentId());
        dto.setPickupPersonId(record.getPickupPersonId());
        dto.setPickupPersonName(record.getPickupPersonName());
        dto.setPickupType(record.getPickupType());
        dto.setPickupTime(record.getPickupTime().format(formatter));
        dto.setPickupMethod(record.getPickupMethod());
        dto.setStatus(record.getStatus());
        dto.setRemark(record.getRemark());

        studentRepository.findById(record.getStudentId()).ifPresent(s -> dto.setStudentName(s.getName()));
        return dto;
    }
}