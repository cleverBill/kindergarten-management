package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.FeeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface FeeRecordRepository extends JpaRepository<FeeRecord, Long> {
    List<FeeRecord> findByFeeItemId(Long feeItemId);

    List<FeeRecord> findByStudentId(Long studentId);

    Optional<FeeRecord> findByFeeItemIdAndStudentId(Long feeItemId, Long studentId);

    @Query("SELECT fr FROM FeeRecord fr WHERE fr.feeItemId = :feeItemId AND fr.status = :status")
    List<FeeRecord> findByFeeItemIdAndStatus(@Param("feeItemId") Long feeItemId, @Param("status") String status);

    @Query("SELECT fr FROM FeeRecord fr WHERE fr.status = 'unpaid' OR fr.status = 'overdue'")
    List<FeeRecord> findUnpaidRecords();

    @Query("SELECT COUNT(fr) FROM FeeRecord fr WHERE fr.feeItemId = :feeItemId AND fr.status = 'paid'")
    Integer countPaidByFeeItemId(@Param("feeItemId") Long feeItemId);

    @Query("SELECT COUNT(fr) FROM FeeRecord fr WHERE fr.feeItemId = :feeItemId AND fr.status != 'paid'")
    Integer countUnpaidByFeeItemId(@Param("feeItemId") Long feeItemId);
}