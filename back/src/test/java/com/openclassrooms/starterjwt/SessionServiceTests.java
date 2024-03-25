package com.openclassrooms.starterjwt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;

class SessionServiceTests {
    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    @BeforeEach
    public void setUp() {
    	MockitoAnnotations.openMocks(this);        
    }

    @Test
    public void testCreate() {
        // Mock data
        Session session = new Session(1L, "Test Session", new Date(), "Description", null, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());

        // Mock the repository behavior
        Mockito.when(sessionRepository.save(Mockito.any(Session.class))).thenReturn(session);

        // Call the service method
        Session result = sessionService.create(session);

        // Assertions
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Test Session", result.getName());
    }
    
    @Test
    public void testFindAll() {
        // Mock data
        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session(1L, "Test Session 1", new Date(), "Description 1", null, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()));
        sessions.add(new Session(2L, "Test Session 2", new Date(), "Description 2", null, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()));

        // Mock the repository behavior
        Mockito.when(sessionRepository.findAll()).thenReturn(sessions);

        // Call the service method
        List<Session> result = sessionService.findAll();

        // Assertions
        Assertions.assertEquals(sessions.size(), result.size());
        Assertions.assertEquals(sessions.get(0).getName(), result.get(0).getName());
        Assertions.assertEquals(sessions.get(1).getDescription(), result.get(1).getDescription());
    }

    @Test
    public void testGetById_ExistingId_ReturnsSession() {
        // Mock data
        Session session = new Session(1L, "Test Session", new Date(), "Description", null, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());

        // Mock the repository behavior
        Mockito.when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        // Call the service method
        Session result = sessionService.getById(1L);

        // Assertions
        Assertions.assertNotNull(result);
        Assertions.assertEquals(session.getDescription(), result.getDescription());
    }

    @Test
    public void testGetById_NonExistingId_ReturnsNull() {
        // Mock the repository behavior
        Mockito.when(sessionRepository.findById(999L)).thenReturn(Optional.empty());

        // Call the service method
        Session result = sessionService.getById(999L);

        // Assertions
        Assertions.assertNull(result);
    }

}
