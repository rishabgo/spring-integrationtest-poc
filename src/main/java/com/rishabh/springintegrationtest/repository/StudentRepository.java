package com.rishabh.springintegrationtest.repository;

import com.rishabh.springintegrationtest.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, String> {
}
