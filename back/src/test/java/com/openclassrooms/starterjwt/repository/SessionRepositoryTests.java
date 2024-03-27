package com.openclassrooms.starterjwt.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.models.Session;

@ExtendWith(MockitoExtension.class)
class SessionRepositoryTests {
    @Mock
    private SessionRepository sessionRepository;
    
    @Test
    @DisplayName("Get an existing session")
    public void testFindById_ExistingId_ReturnsSession() {
        Long id = 1L;
        Session session = new Session(id, "Session 1", new Date(), "Description", null, null, LocalDateTime.now(), LocalDateTime.now());

        when(sessionRepository.findById(id)).thenReturn(Optional.of(session));

        Optional<Session> result = sessionRepository.findById(id);

        assertThat(result).isPresent();
        assertThat(session.getId()).isEqualTo(result.get().getId());
    }

    @Test
    @DisplayName("Get a non existing teacher (should return empty)")
    public void testFindById_NonExistingId_ReturnsEmpty() {
        Long id = 999L;

        when(sessionRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Session> result = sessionRepository.findById(id);
        
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Get all teachers")
    public void testFindAll_SessionsExist_ReturnsAllSessions() {
        Session session1 = new Session(1L, "Session 1", new Date(), "Description", null, null, LocalDateTime.now(), LocalDateTime.now());
        Session session2 = new Session(2L, "Session 2", new Date(), "Description", null, null, LocalDateTime.now(), LocalDateTime.now());
        when(sessionRepository.findAll()).thenReturn(List.of(session1, session2));

        List<Session> result = sessionRepository.findAll();
        
        assertThat(result).hasSize(2);
    }
}
