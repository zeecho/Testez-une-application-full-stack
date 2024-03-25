package com.openclassrooms.starterjwt.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;

class TeacherServiceTests {
    @InjectMocks
    private TeacherService teacherService;

    @Mock
    private TeacherRepository teacherRepository;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    @DisplayName("Getting all teachers (comparing size of array and some data)")
    public void testFindAll() {
        // Mock data
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now()));
        teachers.add(new Teacher(2L, "Smith", "Jane", LocalDateTime.now(), LocalDateTime.now()));

        // Mock the repository behavior
        Mockito.when(teacherRepository.findAll()).thenReturn(teachers);

        // Call the service method
        List<Teacher> result = teacherService.findAll();

        // Assertions
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("John", result.get(0).getFirstName());
        Assertions.assertEquals("Smith", result.get(1).getLastName());
    }

    @Test
    @DisplayName("Retriving an existing Teacher through the service")
    public void testFindById_ExistingId_ReturnsTeacher() {
        // Mock data
        Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());

        // Mock the repository behavior
        Mockito.when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        // Call the service method
        Teacher result = teacherService.findById(1L);

        // Assertions
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Doe", result.getLastName());
    }

    @Test
    @DisplayName("Checking we get NULL when trying to retrieve a non-existing teacher")
    public void testFindById_NonExistingId_ReturnsNull() {
        // Mock the repository behavior
        Mockito.when(teacherRepository.findById(999L)).thenReturn(Optional.empty());

        // Call the service method
        Teacher result = teacherService.findById(999L);

        // Assertions
        Assertions.assertNull(result);
    }
}
