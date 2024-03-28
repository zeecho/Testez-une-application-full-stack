package com.openclassrooms.starterjwt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapperImpl;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.services.TeacherService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(MockitoExtension.class)
class SessionControllerTests {
    private MockMvc mvc;

    @Mock
    private SessionService sessionService;
    
    @Mock
    private TeacherService teacherService;

    private SessionMapperImpl sessionMapper = new SessionMapperImpl();

    private JacksonTester<SessionDto> jsonSession;
    
    @BeforeEach
    void init() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(new SessionController(sessionService, sessionMapper)).build();
    }

    @Test
    @DisplayName("Get a specific session successfully")
    void testFindById_Success() throws Exception {
        Session session = new Session();
        session.setId(1L);
        session.setName("Session 1");
        session.setDate(new Date());
        session.setDescription("Description for session 1");

        when(sessionService.getById(1L)).thenReturn(session);

        MockHttpServletResponse response = mvc.perform(get("/api/session/1")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        SessionDto expectedDto = sessionMapper.toDto(session);
        String expectedJson = new ObjectMapper().writeValueAsString(expectedDto);
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    @Test
    @DisplayName("Get a specific session (not found)")
    void testFindById_NotFound() throws Exception {
        when(sessionService.getById(1L)).thenReturn(null);

        MockHttpServletResponse response = mvc.perform(get("/api/session/1")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Get all sessions with success")
    void testFindAll_Success() throws Exception {
        Session session1 = new Session();
        session1.setId(1L);
        session1.setName("Session 1");        
        session1.setDate(new Date());
        session1.setDescription("Description for session 1");

        Session session2 = new Session();
        session2.setId(2L);
        session2.setName("Session 2");
        session2.setDate(new Date());
        session2.setDescription("Description for session 2");

        List<Session> sessions = Arrays.asList(session1, session2);
        when(sessionService.findAll()).thenReturn(sessions);

        MockHttpServletResponse response = mvc.perform(get("/api/session")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        List<SessionDto> expectedDtoList = sessionMapper.toDto(sessions);
        String expectedJson = new ObjectMapper().writeValueAsString(expectedDtoList);
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

//  TODO this doesn't work  
//    @Test
//    void testCreateSession_Success() throws Exception {
//        Teacher teacher = new Teacher();
//        teacher.setId(1L);
//        teacher.setFirstName("John");
//        teacher.setLastName("Doe");
////        
////        Session session = new Session();
////        session.setId(1L);
////        session.setName("New Session");
////        session.setDate(new Date());
////        session.setDescription("Description for the new session");
////        session.setTeacher(teacher);
//        
//        SessionDto sessionDto = new SessionDto();
//        sessionDto.setId(1L);
//        sessionDto.setName("New Session");
//        sessionDto.setDescription("Description for the new session");
//        sessionDto.setDate(new Date());
//        sessionDto.setTeacher_id(1L);
////        sessionDto.setCreatedAt(LocalDateTime.now());
//        when(teacherService.findById(1L)).thenReturn(teacher);
////        when(sessionService.create(any())).thenReturn(session);
//        // Mock the service method to return a session when called with any session DTO
//        when(sessionService.create(any())).thenAnswer(invocation -> {
//            SessionDto dtoArgument = invocation.getArgument(0);
//            Session createdSession = new Session();
//            createdSession.setId(dtoArgument.getId());
//            createdSession.setName(dtoArgument.getName());
//            createdSession.setDate(dtoArgument.getDate());
//            createdSession.setDescription(dtoArgument.getDescription());
//            createdSession.setTeacher(teacherService.findById(dtoArgument.getTeacher_id()));
//            return createdSession;
//        });
////        SessionDto sessionDto = sessionMapper.toDto(session);
//        
//
//        MockHttpServletResponse response = mvc.perform(post("/api/session")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(sessionDto)))
//                .andReturn().getResponse();
////        MockHttpServletResponse response = mvc.perform(post("/api/session")
////        		.contentType(MediaType.APPLICATION_JSON)
////                .content(
////                        jsonSession
////                        .write(sessionDto)
////                        .getJson()
////                ))
////                .andReturn().getResponse();
//
////        verify(sessionService).create(any());
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
////        String expectedJson = new ObjectMapper().writeValueAsString(sessionMapper.toDto(session));
////        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
//    }
    
//    TODO this doesn't work  
//    @Test
//    void testUpdateSession_Success() throws Exception {
//        Session session = new Session();
//        session.setId(1L);
//        session.setName("Session 1");
//        session.setDate(new Date());
//        session.setDescription("Description for session 1");
//
//        when(sessionService.update(eq(1L), any())).thenReturn(session);
//
//        MockHttpServletResponse response = mvc.perform(put("/api/session/1")
//                .contentType("application/json")
//                .content(new ObjectMapper().writeValueAsString(sessionMapper.toDto(session))))
//                .andReturn().getResponse();
//
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        String expectedJson = new ObjectMapper().writeValueAsString(sessionMapper.toDto(session));
//        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
//    }

    @Test
    @DisplayName("Try to update a session, failing")
    void testUpdateSession_InvalidId() throws Exception {
        MockHttpServletResponse response = mvc.perform(put("/api/session/abc")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(new SessionDto())))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
