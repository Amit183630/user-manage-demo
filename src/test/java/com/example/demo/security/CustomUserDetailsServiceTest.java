package com.example.demo.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private CustomUserDetailsService customUserDetailsService;

	private User user;

	@BeforeEach
	void setUp() {
		user = new User();
		user.setEmail("john.doe@example.com");
		user.setPassword("encodedPassword");
		user.setRoles("ROLE_USER,ROLE_ADMIN");
	}

	@Test
	void testLoadUserByUsername_Success() {
		// Arrange
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

		// Act
		UserDetails userDetails = customUserDetailsService.loadUserByUsername("john.doe@example.com");

		// Assert
		assertNotNull(userDetails);
		assertEquals("john.doe@example.com", userDetails.getUsername());
		assertEquals("encodedPassword", userDetails.getPassword());
		assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
		assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));

		// Verify
		verify(userRepository, times(1)).findByEmail(anyString());
	}

	@Test
	void testLoadUserByUsername_UserNotFound() {
		// Arrange
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

		// Act & Assert
		UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
			customUserDetailsService.loadUserByUsername("nonexistent@example.com");
		});

		assertEquals("User not found with email:nonexistent@example.com", exception.getMessage());

		// Verify
		verify(userRepository, times(1)).findByEmail(anyString());
	}

	@Test
	void testLoadUserByUsername_EmptyRoles() {
		// Arrange
		user.setRoles(""); // No roles assigned
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

		// Act
		UserDetails userDetails = customUserDetailsService.loadUserByUsername("john.doe@example.com");

		// Assert
		assertNotNull(userDetails);
		assertEquals("john.doe@example.com", userDetails.getUsername());
		assertEquals("encodedPassword", userDetails.getPassword());
		assertTrue(userDetails.getAuthorities().isEmpty());

		// Verify
		verify(userRepository, times(1)).findByEmail(anyString());
	}

	@Test
	void testLoadUserByUsername_SingleRole() {
		// Arrange
		user.setRoles("ROLE_USER"); // Single role
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

		// Act
		UserDetails userDetails = customUserDetailsService.loadUserByUsername("john.doe@example.com");

		// Assert
		assertNotNull(userDetails);
		assertEquals("john.doe@example.com", userDetails.getUsername());
		assertEquals("encodedPassword", userDetails.getPassword());
		assertEquals(1, userDetails.getAuthorities().size());
		assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));

		// Verify
		verify(userRepository, times(1)).findByEmail(anyString());
	}
}