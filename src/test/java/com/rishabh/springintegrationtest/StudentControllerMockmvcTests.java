package com.rishabh.springintegrationtest;

import com.rishabh.springintegrationtest.entity.Student;
import com.rishabh.springintegrationtest.repository.StudentRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerMockmvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @Test
    public void testCreateRetrieveWithMockMVC() throws Exception {

        when(studentRepository.findAll()).thenReturn(List.of(Student.builder().id("1").firstName("rishabh").build(),
                Student.builder().id("2").firstName("George").build()));

        ResultActions response = this.mockMvc.perform(get("/api/v1/students"));

        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(jsonPath("$.size()", Matchers.is(2)));

    }
}
