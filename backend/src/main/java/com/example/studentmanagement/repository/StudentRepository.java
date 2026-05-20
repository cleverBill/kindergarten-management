package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByNameContaining(String name);
    List<Student> findByGrade(String grade);
    List<Student> findByClassName(String className);
    
    @Query("SELECT DISTINCT s.className FROM Student s WHERE s.className IS NOT NULL AND s.className != ''")
    List<String> findAllClassNames();
}
