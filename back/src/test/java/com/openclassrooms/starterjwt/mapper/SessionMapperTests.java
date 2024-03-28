package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SessionMapperTests {
    private final TeacherService teacherService = mock(TeacherService.class);
    private final UserService userService = mock(UserService.class);

    private final SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);

//    // TODO fix this
//    @Test
//    @DisplayName("Test SessionDto to Session conversion")
//    void testSessionDtoToSessionMapping() {
//        SessionDto sessionDto = new SessionDto();
//        sessionDto.setId(1L);
//        sessionDto.setName("Session 1");
//        sessionDto.setDate(new Date());
//        sessionDto.setTeacher_id(1L);
//        sessionDto.setDescription("Description for session 1");
//        sessionDto.setUsers(Arrays.asList(1L, 2L));
//        sessionDto.setCreatedAt(LocalDateTime.now());
//        sessionDto.setUpdatedAt(LocalDateTime.now());
//
//        Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
//
//        when(teacherService.findById(1L)).thenReturn(teacher);
//
//        User user1 = new User();
//        user1.setId(1L);
//        user1.setEmail("test@example.com");
//        user1.setFirstName("John");
//        user1.setLastName("Doe");
//        user1.setAdmin(false);
//        user1.setPassword("encodedPassword");
//        User user2 = new User();
//        user2.setId(1L);
//        user2.setEmail("test@example.com");
//        user2.setFirstName("John");
//        user2.setLastName("Doe");
//        user2.setAdmin(false);
//        user2.setPassword("encodedPassword");
//
//        when(userService.findById(1L)).thenReturn(user1);
//        when(userService.findById(2L)).thenReturn(user2);
//
//        Session session = sessionMapper.toEntity(sessionDto);
//
//        assertNotNull(session);
//        assertEquals(sessionDto.getId(), session.getId());
//        assertEquals(sessionDto.getName(), session.getName());
//        assertEquals(sessionDto.getDate(), session.getDate());
//        assertEquals(sessionDto.getDescription(), session.getDescription());
//        assertEquals(teacher, session.getTeacher());
//        assertEquals(2, session.getUsers().size());
//        assertEquals(user1, session.getUsers().get(0));
//        assertEquals(user2, session.getUsers().get(1));
//        assertNotNull(session.getCreatedAt());
//        assertNotNull(session.getUpdatedAt());
//    }

    @Test
    @DisplayName("Test Session to SessionDto conversion")
    void testSessionToSessionDtoMapping() {
        Session session = new Session();
        session.setId(1L);
        session.setName("Session 1");
        session.setDate(new Date());
        session.setDescription("Description for session 1");

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        session.setTeacher(teacher);

        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        session.setUsers(Arrays.asList(user1, user2));

        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());

        SessionDto sessionDto = sessionMapper.toDto(session);

        assertNotNull(sessionDto);
        assertEquals(session.getId(), sessionDto.getId());
        assertEquals(session.getName(), sessionDto.getName());
        assertEquals(session.getDate(), sessionDto.getDate());
        assertEquals(session.getDescription(), sessionDto.getDescription());
        assertEquals(teacher.getId(), sessionDto.getTeacher_id());
        assertEquals(2, sessionDto.getUsers().size());
        assertEquals(user1.getId(), sessionDto.getUsers().get(0));
        assertEquals(user2.getId(), sessionDto.getUsers().get(1));
        assertNotNull(sessionDto.getCreatedAt());
        assertNotNull(sessionDto.getUpdatedAt());
    }
}
