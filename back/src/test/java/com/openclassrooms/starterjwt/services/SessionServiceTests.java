package com.openclassrooms.starterjwt.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class SessionServiceTests {
    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    private SessionService sessionService;

    @BeforeEach
    public void init() {
        sessionService = new SessionService(sessionRepository, userRepository);
    }

    @Test
    @DisplayName("Create a new session (checkink it's not null and has the right name")
    public void testCreate() {
        Session session = new Session(1L, "Test Session", new Date(), "Description", null, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());

        when(sessionRepository.save(session)).thenReturn(session);

        Session result = sessionService.create(session);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Session");
    }
    
    @Test
    @DisplayName("Getting all sessions (comparing size of array and some data)")
    public void testFindAll() {
        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session(1L, "Test Session 1", new Date(), "Description 1", null, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()));
        sessions.add(new Session(2L, "Test Session 2", new Date(), "Description 2", null, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()));

        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> result = sessionService.findAll();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getName()).isEqualTo("Test Session 1");
        assertThat(result.get(1).getDescription()).isEqualTo("Description 2");
    }

    @Test
    @DisplayName("Retriving an existing Session through the service")
    public void testGetById_ExistingId_ReturnsSession() {
        Long sessionId = 1L;
        Session session = new Session(sessionId, "Test Session", new Date(), "Description", null, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        
        Session result = sessionService.getById(sessionId);

        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo("Description");
    }

    @Test
    @DisplayName("Checking we get NULL when trying to retrieve a non-existing session")
    public void testGetById_NonExistingId_ReturnsNull() {
        when(sessionRepository.findById(999L)).thenReturn(Optional.empty());
        
        Session result = sessionService.getById(999L);

        assertThat(result).isNull();
    }
    
    @Test
    @DisplayName("Delete an existing session")
    void testDelete_ExistingSession_DeletesSession() {
        Long sessionId = 1L;
        
        sessionService.delete(sessionId);
        
        verify(sessionRepository).deleteById(sessionId);
    }
    

    @Test
    @DisplayName("Update an existing session")
    void testUpdate_ExistingSession_UpdatesSession() {
        Long sessionId = 1L;
        Session updatedSession = new Session(sessionId, "Updated Session", new Date(), "Updated Description", null, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
        
        when(sessionRepository.save(any(Session.class))).thenReturn(updatedSession);
        
        Session result = sessionService.update(sessionId, updatedSession);
        
        assertThat(result).isEqualTo(updatedSession);
    }
    
    @Test
    @DisplayName("Participate in a session")
    void testParticipate_InExistingSession_ParticipatesInSession() {
        Long sessionId = 1L;
        Long userId = 1L;
        Session session = new Session(sessionId, "Test Session", new Date(), "Description", null, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
        User user = new User(userId, "user@example.com", "John", "Doe", "password", false, LocalDateTime.now(), LocalDateTime.now());
        
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);
        
        sessionService.participate(sessionId, userId);
        
        verify(sessionRepository).save(session);
    }
    
    @Test
    @DisplayName("No longer participate in a session")
    void testNoLongerParticipate_InExistingSession_NoLongerParticipatesInSession() {
        Long sessionId = 1L;
        Long userId = 1L;
        Session session = new Session(sessionId, "Test Session", new Date(), "Description", null, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
        User user = new User(userId, "user@example.com", "John", "Doe", "password", false, LocalDateTime.now(), LocalDateTime.now());
        session.getUsers().add(user);
        
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);
        
        sessionService.noLongerParticipate(sessionId, userId);
        
        verify(sessionRepository).save(session);
    }
}
