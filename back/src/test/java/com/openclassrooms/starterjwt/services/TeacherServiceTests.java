package com.openclassrooms.starterjwt.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTests {
    private TeacherService teacherService;

    @Mock
    private TeacherRepository teacherRepository;
    
    @BeforeEach
    public void init() {
        teacherService = new TeacherService(teacherRepository);
    }
    
    @Test
    @DisplayName("Getting all teachers (comparing size of array and some data)")
    public void testFindAll() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now()));
        teachers.add(new Teacher(2L, "Smith", "Jane", LocalDateTime.now(), LocalDateTime.now()));

        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> result = teacherService.findAll();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
        assertThat(result.get(1).getLastName()).isEqualTo("Smith");
    }

    @Test
    @DisplayName("Retriving an existing Teacher through the service")
    public void testFindById_ExistingId_ReturnsTeacher() {
        Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        Teacher result = teacherService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getLastName()).isEqualTo("Doe");
    }

    @Test
    @DisplayName("Checking we get NULL when trying to retrieve a non-existing teacher")
    public void testFindById_NonExistingId_ReturnsNull() {        
        Long nonExistingTeacherId = 999L;
        when(teacherRepository.findById(nonExistingTeacherId)).thenReturn(Optional.empty());

        Teacher result = teacherService.findById(nonExistingTeacherId);
        
                assertThat(result).isNull();
    }
}
