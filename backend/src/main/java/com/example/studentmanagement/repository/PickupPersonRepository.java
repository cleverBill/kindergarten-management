package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.PickupPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PickupPersonRepository extends JpaRepository<PickupPerson, Long> {
    List<PickupPerson> findByStudentId(Long studentId);
    List<PickupPerson> findByStudentIdAndIsVerified(Long studentId, Boolean isVerified);
}