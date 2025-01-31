package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.api.IpFinderService;
import com.example.demo.api.IpGeolocationService;
import com.example.demo.dto.UserRegDTO;
import com.example.demo.dto.UserRoles;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.ApiMessages;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final IpFinderService ipFinderService;
	private final IpGeolocationService ipGeolocationService;

	public User registerUser(UserRegDTO dto, UserRoles role) {
		if (userRepository.findByEmail(dto.email()).isPresent()) {
			throw new RuntimeException(ApiMessages.Reg.ALREADY_EXISTS);
		}

		User user = new User();
		user.setEmail(dto.email());
		user.setGender(dto.gender());
		user.setName(dto.name());
		user.setPassword(passwordEncoder.encode(dto.password()));
		user.setIpAddress(ipFinderService.getIp());
		user.setCountry(ipGeolocationService.getCountryFromIp(user.getIpAddress()));
		user.setRoles(role.getValue());
		return userRepository.save(user);

	}

	public List<User> getUsers() {
		List<User> users = userRepository.findAll();
		if (users.isEmpty()) {
			throw new RuntimeException(ApiMessages.Get.EMPTY);
		}
		return users;
	}

	public void validate(String email, String password) {
		Optional<User> user = userRepository.findByEmail(email);
		if (!user.isPresent() || !passwordEncoder.matches(password, user.get().getPassword())) {
			throw new RuntimeException(ApiMessages.Val.INVALID_CREDENTIALS);
		}
	}

	@Transactional
	public void delete(String email) {
		Integer count = userRepository.deleteByEmail(email);
		if (count == null || count == 0) {
			throw new RuntimeException(ApiMessages.Del.NOT_EXIST + email);
		}
	}
}
