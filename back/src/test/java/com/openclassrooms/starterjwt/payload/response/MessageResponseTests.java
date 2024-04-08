package com.openclassrooms.starterjwt.payload.response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MessageResponseTests {
    @Test
    void testSetMessage() {
        MessageResponse messageResponse = new MessageResponse("Initial message");
        messageResponse.setMessage("New message");
        assertEquals("New message", messageResponse.getMessage());
    }
}
