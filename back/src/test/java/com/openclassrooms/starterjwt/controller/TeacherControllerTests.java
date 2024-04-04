package com.openclassrooms.starterjwt.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTests {  
    @Mock
    private TeacherService teacherService;
    
    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;
    
    private Teacher teacher;
    
    @BeforeEach
    void init() {
    	this.teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
    }
    
    @Test
    @DisplayName("Get a specific teacher successfully")
    void testFindById_Success() throws Exception {    	
    	Teacher teacher = this.teacher;
    	when(teacherService.findById(anyLong())).thenReturn(teacher);
        when(teacherMapper.toDto(any(Teacher.class))).thenReturn(new TeacherDto());

        ResponseEntity<?> response = teacherController.findById("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(TeacherDto.class);
    }

    @Test
    @DisplayName("Get a specific teacher (not found)")
    void testFindById_NotFound() throws Exception {
        when(teacherService.findById(anyLong())).thenReturn(null);

        ResponseEntity<?> response = teacherController.findById("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Get a specific session (invalid id)")
    void testFindById_InvalidId() {
        ResponseEntity<?> response = teacherController.findById("invalid");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Get all teachers successfully")
    void testFindAll_Success() throws Exception {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);
        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(anyList())).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = teacherController.findAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(List.class);
    }
}
