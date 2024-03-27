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
        // Mock data
        Session session = new Session(1L, "Test Session", new Date(), "Description", null, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());

        // Mock the repository behavior
        when(sessionRepository.save(session)).thenReturn(session);

        // Call the service method
        Session result = sessionService.create(session);

        // Assertions
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Session");
    }
    
    @Test
    @DisplayName("Getting all sessions (comparing size of array and some data)")
    public void testFindAll() {
        // Mock data
        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session(1L, "Test Session 1", new Date(), "Description 1", null, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()));
        sessions.add(new Session(2L, "Test Session 2", new Date(), "Description 2", null, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()));

        // Mock the repository behavior
        when(sessionRepository.findAll()).thenReturn(sessions);

        // Call the service method
        List<Session> result = sessionService.findAll();

        // Assertions
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getName()).isEqualTo("Test Session 1");
        assertThat(result.get(1).getDescription()).isEqualTo("Description 2");
    }

    @Test
    @DisplayName("Retriving an existing Session through the service")
    public void testGetById_ExistingId_ReturnsSession() {
        // Mock data
        Long sessionId = 1L;
        Session session = new Session(sessionId, "Test Session", new Date(), "Description", null, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());

        // Mock the repository behavior
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        
        // Call the service method
        Session result = sessionService.getById(sessionId);

        // Assertions
        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo("Description");
    }

    @Test
    @DisplayName("Checking we get NULL when trying to retrieve a non-existing session")
    public void testGetById_NonExistingId_ReturnsNull() {
        // Mock the repository behavior
        when(sessionRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Call the service method
        Session result = sessionService.getById(999L);

        // Assertions
        assertThat(result).isNull();
    }
}
