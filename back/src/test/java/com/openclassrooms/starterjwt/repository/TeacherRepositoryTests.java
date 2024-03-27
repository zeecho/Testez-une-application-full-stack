package com.openclassrooms.starterjwt.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.models.Teacher;

@ExtendWith(MockitoExtension.class)
class TeacherRepositoryTests {    
	@Mock
    private TeacherRepository teacherRepository;
    
    @Test
    @DisplayName("Get an existing teacher")
    public void testFindById_ExistingId_ReturnsTeacher() {
        Long id = 1L;
        Teacher teacher = new Teacher(id, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());

        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));

        Optional<Teacher> result = teacherRepository.findById(id);

        assertThat(result).isPresent();
        assertThat(teacher.getId()).isEqualTo(result.get().getId());
    }
    
    @Test
    @DisplayName("Get a non existing teacher (should return empty)")
    public void testFindById_NonExistingId_ReturnsEmpty() {
        Long id = 999L;

        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Teacher> result = teacherRepository.findById(id);
        
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Get all teachers")
    public void testFindAll_TeachersExist_ReturnsAllTeachers() {
        Teacher teacher1 = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher2 = new Teacher(2L, "Smith", "Jane", LocalDateTime.now(), LocalDateTime.now());
        Mockito.when(teacherRepository.findAll()).thenReturn(List.of(teacher1, teacher2));

        List<Teacher> result = teacherRepository.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
        assertThat(result.get(1).getLastName()).isEqualTo("Smith");
    }
}
