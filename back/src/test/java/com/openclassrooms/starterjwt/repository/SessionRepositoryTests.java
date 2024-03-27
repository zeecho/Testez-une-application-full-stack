package com.openclassrooms.starterjwt.repository;

import java.time.LocalDateTime;
import java.util.Date;
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

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;

class SessionRepositoryTests {
    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionService sessionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    @DisplayName("Get an existing session")
    public void testFindById_ExistingId_ReturnsSession() {
        Long id = 1L;
        Session session = new Session(id, "Session 1", new Date(), "Description", null, null, LocalDateTime.now(), LocalDateTime.now());

        Mockito.when(sessionRepository.findById(id)).thenReturn(Optional.of(session));

        Optional<Session> result = sessionRepository.findById(id);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(session.getId(), result.get().getId());
    }

    @Test
    @DisplayName("Get a non existing teacher (should return empty)")
    public void testFindById_NonExistingId_ReturnsEmpty() {
        Long id = 999L;

        Mockito.when(sessionRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Session> result = sessionRepository.findById(id);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Get all teachers")
    public void testFindAll_SessionsExist_ReturnsAllSessions() {
        Session session1 = new Session(1L, "Session 1", new Date(), "Description", null, null, LocalDateTime.now(), LocalDateTime.now());
        Session session2 = new Session(2L, "Session 2", new Date(), "Description", null, null, LocalDateTime.now(), LocalDateTime.now());
        Mockito.when(sessionRepository.findAll()).thenReturn(List.of(session1, session2));

        List<Session> result = sessionRepository.findAll();

        Assertions.assertEquals(2, result.size());
    }
}
