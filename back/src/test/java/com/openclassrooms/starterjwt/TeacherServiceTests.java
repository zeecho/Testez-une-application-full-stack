package com.openclassrooms.starterjwt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
        Assertions.assertEquals(teachers.size(), result.size());
        Assertions.assertEquals(teachers.get(0).getFirstName(), result.get(0).getFirstName());
        Assertions.assertEquals(teachers.get(1).getLastName(), result.get(1).getLastName());
    }

    @Test
    public void testFindById_ExistingId_ReturnsTeacher() {
        // Mock data
        Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());

        // Mock the repository behavior
        Mockito.when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        // Call the service method
        Teacher result = teacherService.findById(1L);

        // Assertions
        Assertions.assertNotNull(result);
        Assertions.assertEquals(teacher.getLastName(), result.getLastName());
    }

    @Test
    public void testFindById_NonExistingId_ReturnsNull() {
        // Mock the repository behavior
        Mockito.when(teacherRepository.findById(999L)).thenReturn(Optional.empty());

        // Call the service method
        Teacher result = teacherService.findById(999L);

        // Assertions
        Assertions.assertNull(result);
    }
}
