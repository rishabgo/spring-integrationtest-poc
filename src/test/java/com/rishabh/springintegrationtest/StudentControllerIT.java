package com.rishabh.springintegrationtest;

import com.rishabh.springintegrationtest.entity.Student;
import com.rishabh.springintegrationtest.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

//Using TestRestTemplate to make api call

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private StudentRepository studentRepository;

    @Test
    public void testRetrieveStudent() {

        when(studentRepository.findAll()).thenReturn(List.of(Student.builder().id("1").firstName("Rishabh").build(),
                Student.builder().id("2").firstName("George").build()));

        ResponseEntity<List<Student>> response = testRestTemplate.exchange(
                createURLWithPort("/students"), HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).size()).isEqualTo(2);
        assertThat(response.getBody().get(0).getFirstName()).isEqualTo("Rishabh");
        assertThat(response.getBody().get(1).getFirstName()).isEqualTo("George");
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + "/api/v1" + uri;
    }

}
