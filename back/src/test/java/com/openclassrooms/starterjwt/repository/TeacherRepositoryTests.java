package com.openclassrooms.starterjwt.repository;

import java.time.LocalDateTime;
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
import com.openclassrooms.starterjwt.services.TeacherService;

class TeacherRepositoryTests {    
	@Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @BeforeEach
    public void setUp() {
    	MockitoAnnotations.openMocks(this);        
    }
    
    @Test
    @DisplayName("Get an existing teacher")
    public void testFindById_ExistingId_ReturnsTeacher() {
        Long id = 1L;
        Teacher teacher = new Teacher(id, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());

        Mockito.when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));

        Optional<Teacher> result = teacherRepository.findById(id);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(teacher.getId(), result.get().getId());
    }
    
    @Test
    @DisplayName("Get a non existing teacher (should return empty)")
    public void testFindById_NonExistingId_ReturnsEmpty() {
        Long id = 999L;

        Mockito.when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Teacher> result = teacherRepository.findById(id);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Get all teachers")
    public void testFindAll_TeachersExist_ReturnsAllTeachers() {
        Teacher teacher1 = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher2 = new Teacher(2L, "Smith", "Jane", LocalDateTime.now(), LocalDateTime.now());
        Mockito.when(teacherRepository.findAll()).thenReturn(List.of(teacher1, teacher2));

        List<Teacher> result = teacherRepository.findAll();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("John", result.get(0).getFirstName());
        Assertions.assertEquals("Smith", result.get(1).getLastName());
    }
}
