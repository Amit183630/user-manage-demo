package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserRegDTO(@Schema(example = "example name") String name,
		@Schema(example = "name@example.com") String email, @Schema(example = "Male") String gender,
		@Schema(example = "example@123") String password) {
}