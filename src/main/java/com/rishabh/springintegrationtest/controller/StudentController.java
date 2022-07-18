package com.rishabh.springintegrationtest.controller;

import com.rishabh.springintegrationtest.entity.Student;
import com.rishabh.springintegrationtest.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/student")
    public ResponseEntity<Student> createStudent(@RequestBody @NonNull final Student student) {
        if(student.getId().isBlank()) throw new RuntimeException("Student id is not valid.");
        return new ResponseEntity<>(studentRepository.save(student), HttpStatus.CREATED);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable("studentId") @NonNull final String studentId) {
        final Student student = studentRepository.findById(studentId).orElseThrow(IllegalArgumentException::new);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        return new ResponseEntity<>(studentRepository.findAll(), HttpStatus.OK);
    }
}
