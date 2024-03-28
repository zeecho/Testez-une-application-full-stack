package com.openclassrooms.starterjwt.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.mapper.TeacherMapperImpl;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTests {
    private MockMvc mvc;
    
    @Mock
    private TeacherService teacherService;

    private TeacherMapperImpl teacherMapper = new TeacherMapperImpl();
    
    @BeforeEach
    void init() {
        mvc = MockMvcBuilders.standaloneSetup(new TeacherController(teacherService, teacherMapper)).build();
    }
    
    @Test
    @DisplayName("Get a specific teacher successfully")
    void testFindById_Success() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        when(teacherService.findById(1L)).thenReturn(teacher);

        MockHttpServletResponse response = mvc.perform(get("/api/teacher/1")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String expectedJson = "{\"id\":1,\"lastName\":\"Doe\",\"firstName\":\"John\",\"createdAt\":null,\"updatedAt\":null}";
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    @Test
    @DisplayName("Get a specific teacher (not found)")
    void testFindById_NotFound() throws Exception {
        when(teacherService.findById(1L)).thenReturn(null);

        MockHttpServletResponse response = mvc.perform(get("/api/teacher/1")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Get all teachers successfully")
    void testFindAll_Success() throws Exception {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Smith");

        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);
        when(teacherService.findAll()).thenReturn(teachers);

        MockHttpServletResponse response = mvc.perform(get("/api/teacher")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        String expectedJson = "[{\"id\":1,\"lastName\":\"Doe\",\"firstName\":\"John\",\"createdAt\":null,\"updatedAt\":null},{\"id\":2,\"lastName\":\"Smith\",\"firstName\":\"Jane\",\"createdAt\":null,\"updatedAt\":null}]";
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }
}
