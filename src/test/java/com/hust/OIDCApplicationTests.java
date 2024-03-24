package com.hust;

import com.hust.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OIDCApplicationTests {

    private Validator validator;

    // 高度重复的代码，我们要把它抽取出来，保证代码的简洁性
    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testUserValidation() {
        UserDTO userDTO = createUserDTO("john", "password123", "john@example.com", "John", "https://example.com/avatar.jpg", "Hello, world!");

        // 使用验证器对 UserDTO 对象进行验证，并将验证结果存储在 violations 集合中。validate() 方法返回一个 Set 集合，其中包含了违反验证规则的约束违例。
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        // 断言 violations 集合不为空，即存在违反验证规则的约束违例。如果集合不为空，断言通过；如果集合为空，断言失败。
        assertTrue(violations.isEmpty());
    }

    @Test
    void testEmptyUsername() {
        UserDTO userDTO = createUserDTO("", "password123", "john@example.com", "John", "https://example.com/avatar.jpg", "Hello, world!");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        Iterator<ConstraintViolation<UserDTO>> iterator = violations.iterator();
        ConstraintViolation<UserDTO> firstViolation = iterator.next();
        ConstraintViolation<UserDTO> secondViolation = iterator.next();

        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
        List<String> violationMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertTrue(violationMessages.contains("Username cannot be blank"));
        assertTrue(violationMessages.contains("Username must be between 4 and 16 characters"));
    }

    @Test
    void testInvalidEmail() {
        UserDTO userDTO = createUserDTO("john", "password123", "invalid_email", "John", "https://example.com/avatar.jpg", "Hello, world!");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Invalid email address", violations.iterator().next().getMessage());
    }

    @Test
    void testShortPassword() {
        UserDTO userDTO = createUserDTO("john", "pass", "john@example.com", "John", "https://example.com/avatar.jpg", "Hello, world!");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Password must be at least 8 characters", violations.iterator().next().getMessage());
    }

    @Test
    void testLongBio() {
        UserDTO userDTO = createUserDTO("john", "password123", "john@example.com", "John", "https://example.com/avatar.jpg", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed aliquam, neque id finibus ullamcorper, massa metus consequat odio, sit amet ultrices turpis turpis in lectus.");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Bio must not exceed 64 characters", violations.iterator().next().getMessage());
    }

    // 避免重复的set，简化代码
    private UserDTO createUserDTO(String username, String password, String email, String nickname, String avatar, String bio) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setPassword(password);
        userDTO.setEmail(email);
        userDTO.setNickname(nickname);
        userDTO.setAvatar(avatar);
        userDTO.setBio(bio);
        return userDTO;
    }
}
