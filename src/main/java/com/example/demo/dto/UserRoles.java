package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoles {
	
	ADMIN("ROLE_ADMIN"),USER("ROLE_USER");
	private String value;
}
