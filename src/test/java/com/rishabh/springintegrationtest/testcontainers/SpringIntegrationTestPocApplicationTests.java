package com.rishabh.springintegrationtest.testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rishabh.springintegrationtest.entity.Student;
import com.rishabh.springintegrationtest.repository.StudentRepository;
import com.rishabh.springintegrationtest.testcontainers.AbstractContainerBaseTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class SpringIntegrationTestPocApplicationTests extends AbstractContainerBaseTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //BDD style test
    @Test
    public void givenStudents_whenGetAllStudents_thenListOfStudents() throws Exception {

        System.out.println(MY_SQL_CONTAINER.getJdbcUrl());
        System.out.println(MY_SQL_CONTAINER.getUsername());
        System.out.println(MY_SQL_CONTAINER.getPassword());

        //given
        List<Student> studentList = List.of(Student.builder().id(UUID.randomUUID().toString()).firstName("rishabh").lastName("goyal").email("rishabhg@test.com").build(),
                Student.builder().id(UUID.randomUUID().toString()).firstName("John").lastName("Snow").email("John@test.com").build());
        List<Student> savedList = studentRepository.saveAll(studentList);

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/students"));

        //then
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(jsonPath("$.size()", Matchers.is(2)));
    }

    @Test
    public void givenStudent_whenGetStudentById_thenReturnStudent() throws Exception {

        //given
        Student student = Student.builder().id(UUID.randomUUID().toString()).firstName("rishabh").lastName("goyal").email("rishabhg@test.com").build();
        studentRepository.save(student);

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/student/" + student.getId()));

        //then
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(jsonPath("$.firstName", Matchers.is("rishabh")));
        response.andExpect(jsonPath("$.lastName", Matchers.is("goyal")));
    }

    @Test
    public void givenStudentObject_whenSave_thenReturnSavedStudent() throws Exception {

        //given
        Student student = Student.builder().id(UUID.randomUUID().toString()).firstName("rishabh").lastName("goyal").email("rishabhg@test.com").build();

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));

        //then
        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void givenStudentWithNullId_whenSave_thenReturnException() throws Exception {

        //given
        Student student = Student.builder().firstName("rishabh").lastName("goyal").email("rishabhg@test.com").build();

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));

        //then
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
