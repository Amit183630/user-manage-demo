package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ServerResponse;
import com.example.demo.dto.UserRegDTO;
import com.example.demo.dto.UserRoles;
import com.example.demo.service.UserService;
import com.example.demo.utils.ApiMessages;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {

	private final UserService userService;

	private ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(HttpStatus.OK);

	@PostMapping("/register")
	@Operation(summary = ApiMessages.Reg.SUMMARY, description = ApiMessages.Reg.DESC)
	public ResponseEntity<ServerResponse> registerUser(@RequestBody UserRegDTO userRegDTO) {
		return responseBuilder.body(
				new ServerResponse(ApiMessages.Reg.SUCCESS, userService.registerUser(userRegDTO, UserRoles.USER)));
	}

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = ApiMessages.Get.SUMMARY, description = ApiMessages.Get.DESC)
	public ResponseEntity<ServerResponse> getAllUsers() {
		log.info("Authentication: {}", SecurityContextHolder.getContext().getAuthentication());
		return responseBuilder.body(new ServerResponse(ApiMessages.Get.SUCCESS, userService.getUsers()));
	}

	@PostMapping("/validate")
	@Operation(summary = ApiMessages.Val.SUMMARY, description = ApiMessages.Val.DESC)
	public ResponseEntity<ServerResponse> registerUser(@RequestParam String email, @RequestParam String password) {
		userService.validate(email, password);
		return responseBuilder.body(new ServerResponse(ApiMessages.Val.SUCCESS, null));
	}

	@DeleteMapping("/delete")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = ApiMessages.Del.SUMMARY, description = ApiMessages.Del.DESC)
	public ResponseEntity<ServerResponse> registerUser(@RequestParam String email) {
		userService.delete(email);
		return responseBuilder.body(new ServerResponse(ApiMessages.Del.SUCCESS, null));
	}
}
