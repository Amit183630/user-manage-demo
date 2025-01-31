package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.api.IpFinderService;
import com.example.demo.api.IpGeolocationService;
import com.example.demo.dto.UserRegDTO;
import com.example.demo.dto.UserRoles;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.ApiMessages;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private IpFinderService ipFinderService;

	@Mock
	private IpGeolocationService ipGeolocationService;

	@InjectMocks
	private UserService userService;

	private UserRegDTO userRegDTO;
	private User user;

	@BeforeEach
	void setUp() {
		userRegDTO = new UserRegDTO("John Doe", "john.doe@example.com", "Male", "Password@123");
		user = new User();
		user.setName("John Doe");
		user.setEmail("john.doe@example.com");
		user.setGender("Male");
		user.setPassword("encodedPassword");
		user.setIpAddress("192.168.1.1");
		user.setCountry("United States");
		user.setRoles(UserRoles.USER.getValue());
	}

	@Test
	void testRegisterUser_Success() {
		// Arrange
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
		when(ipFinderService.getIp()).thenReturn("192.168.1.1");
		when(ipGeolocationService.getCountryFromIp(anyString())).thenReturn("United States");
		when(userRepository.save(any(User.class))).thenReturn(user);

		// Act
		User registeredUser = userService.registerUser(userRegDTO, UserRoles.USER);

		// Assert
		assertNotNull(registeredUser);
		assertEquals("John Doe", registeredUser.getName());
		assertEquals("john.doe@example.com", registeredUser.getEmail());
		assertEquals("Male", registeredUser.getGender());
		assertEquals("encodedPassword", registeredUser.getPassword());
		assertEquals("192.168.1.1", registeredUser.getIpAddress());
		assertEquals("United States", registeredUser.getCountry());
		assertEquals(UserRoles.USER.getValue(), registeredUser.getRoles());

		// Verify
		verify(userRepository, times(1)).findByEmail(anyString());
		verify(passwordEncoder, times(1)).encode(anyString());
		verify(ipFinderService, times(1)).getIp();
		verify(ipGeolocationService, times(1)).getCountryFromIp(anyString());
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	void testRegisterUser_EmailAlreadyExists() {
		// Arrange
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

		// Act & Assert
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			userService.registerUser(userRegDTO, UserRoles.USER);
		});

		assertEquals(ApiMessages.Reg.ALREADY_EXISTS, exception.getMessage());

		// Verify
		verify(userRepository, times(1)).findByEmail(anyString());
		verify(passwordEncoder, never()).encode(anyString());
		verify(ipFinderService, never()).getIp();
		verify(ipGeolocationService, never()).getCountryFromIp(anyString());
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void testGetUsers_Success() {
		// Arrange
		when(userRepository.findAll()).thenReturn(List.of(user));

		// Act
		List<User> users = userService.getUsers();

		// Assert
		assertNotNull(users);
		assertEquals(1, users.size());
		assertEquals("John Doe", users.get(0).getName());

		// Verify
		verify(userRepository, times(1)).findAll();
	}

	@Test
	void testGetUsers_EmptyList() {
		// Arrange
		when(userRepository.findAll()).thenReturn(Collections.emptyList());

		// Act & Assert
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			userService.getUsers();
		});

		assertEquals(ApiMessages.Get.EMPTY, exception.getMessage());

		// Verify
		verify(userRepository, times(1)).findAll();
	}

	@Test
	void testValidate_Success() {
		// Arrange
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

		// Act & Assert
		assertDoesNotThrow(() -> {
			userService.validate("john.doe@example.com", "Password@123");
		});

		// Verify
		verify(userRepository, times(1)).findByEmail(anyString());
		verify(passwordEncoder, times(1)).matches(anyString(), anyString());
	}

	@Test
	void testValidate_InvalidCredentials() {
		// Arrange
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

		// Act & Assert
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			userService.validate("john.doe@example.com", "WrongPassword");
		});

		assertEquals(ApiMessages.Val.INVALID_CREDENTIALS, exception.getMessage());

		// Verify
		verify(userRepository, times(1)).findByEmail(anyString());
		verify(passwordEncoder, never()).matches(anyString(), anyString());
	}

	@Test
	void testDelete_Success() {
		// Arrange
		when(userRepository.deleteByEmail(anyString())).thenReturn(1);

		// Act & Assert
		assertDoesNotThrow(() -> {
			userService.delete("john.doe@example.com");
		});

		// Verify
		verify(userRepository, times(1)).deleteByEmail(anyString());
	}

	@Test
	void testDelete_UserNotFound() {
		// Arrange
		when(userRepository.deleteByEmail(anyString())).thenReturn(0);

		// Act & Assert
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			userService.delete("nonexistent@example.com");
		});

		assertEquals(ApiMessages.Del.NOT_EXIST + "nonexistent@example.com", exception.getMessage());

		// Verify
		verify(userRepository, times(1)).deleteByEmail(anyString());
	}
}