package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;

class UserMapperTests {
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    @DisplayName("Test User to UserDto conversion")
    public void testUserToUserDtoMapping() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAdmin(false);
        user.setPassword("encodedPassword");

        UserDto userDto = userMapper.toDto(user);

        assertNotNull(userDto);
        assertEquals(1L, userDto.getId());
        assertEquals("John", userDto.getFirstName());
        assertEquals("Doe", userDto.getLastName());
        assertEquals("test@example.com", userDto.getEmail());
    }

    @Test
    @DisplayName("Test UserDto to User conversion")
    public void testUserDtoToUserMapping() {
        UserDto userDto = new UserDto();
        userDto.setId(2L);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("test@example.com");
        userDto.setAdmin(true);
        userDto.setPassword("encodedPassword");

        User user = userMapper.toEntity(userDto);

        assertNotNull(user);
        assertEquals(2L, user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals("test@example.com", user.getEmail());
    }
}
