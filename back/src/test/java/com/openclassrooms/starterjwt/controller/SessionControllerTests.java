package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.services.TeacherService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionControllerTests {
    @Mock
    private SessionService sessionService;
    
    @Mock
    private TeacherService teacherService;

    @Mock
    private SessionMapper sessionMapper;
    
    @InjectMocks
    private SessionController sessionController;
    
    private Session session;
    
    @BeforeEach
    void init() {
    	List<User> users = new ArrayList<>();
    	users.add(new User(1L, "test@example.com", "John", "Doe", "encodedPassword", false, LocalDateTime.now(), LocalDateTime.now()));
        users.add(new User(2L, "test2@example.com", "Yann", "Stivel", "encodedPassword", true, LocalDateTime.now(), LocalDateTime.now()));
    	
        Teacher teacher = new Teacher(1L, "John", "Doe", LocalDateTime.now(), LocalDateTime.now());

    	this.session = new Session(1L, "Session 1", new Date(), "Description for session 1", teacher, users, LocalDateTime.now(), LocalDateTime.now());
    }
    
    @Test
    @DisplayName("Get a specific session successfully")
    void testFindById_Success() throws Exception {
    	Session session = this.session;
        when(sessionService.getById(anyLong())).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(new SessionDto());

        ResponseEntity<?> response = sessionController.findById("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(SessionDto.class);
    }

    @Test
    @DisplayName("Get a specific session (null id)")
    void testFindById_NotFound() {
        when(sessionService.getById(anyLong())).thenReturn(null);

        ResponseEntity<?> response = sessionController.findById("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Get a specific session (invalid id)")
    void testFindById_InvalidId() {
        ResponseEntity<?> response = sessionController.findById("invalid");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }
    
    @Test
    @DisplayName("Get all sessions with success")
    void testFindAll_Success() {
        List<Session> sessions = new ArrayList<>();
        sessions.add(session);
        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(anyList())).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = sessionController.findAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(List.class);
    }
    
    @Test
    @DisplayName("Create a session with success")
    void testCreateSession_Success() {
        SessionDto sessionDto = new SessionDto();
        Session session = this.session;
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.create(any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.create(sessionDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(SessionDto.class);
    }
    
    @Test
    @DisplayName("Update a session with success")
    void testUpdate_Success() {
        SessionDto sessionDto = new SessionDto();
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.update(anyLong(), any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.update("1", sessionDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(SessionDto.class);    
    }

    @Test
    @DisplayName("Try to update a session with an invalid id")
    void testUpdate_InvalidId() {
        ResponseEntity<?> response = sessionController.update("invalid", new SessionDto());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Save a session with success")
    void testSave_Success() {
        when(sessionService.getById(anyLong())).thenReturn(session);

        ResponseEntity<?> response = sessionController.save("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Try to save a session (null id)")
    void testSave_NotFound() {
        when(sessionService.getById(anyLong())).thenReturn(null);

        ResponseEntity<?> response = sessionController.save("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Try to save a session (invalid id)")
    void testSave_InvalidId() {
        ResponseEntity<?> response = sessionController.save("invalid");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Participate in session with success")
    void testParticipate_Success() {
        ResponseEntity<?> response = sessionController.participate("1", "1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Participate in session (invalid id)")
    void testParticipate_InvalidId() {
        ResponseEntity<?> response = sessionController.participate("invalid", "1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Unparticipate in session with success")
    void testNoLongerParticipate_Success() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Unparticipate in session (invalid id)")
    void testNoLongerParticipate_InvalidId() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("invalid", "1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }
}
