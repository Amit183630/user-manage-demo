package com.example.demo.utils;

import org.springframework.http.HttpStatus;

public final class ApiMessages {
	private ApiMessages() {
	} // Prevent instantiation

	// HTTP Status Codes
	public static final String HTTP_OK = String.valueOf(HttpStatus.OK.value());
	public static final String HTTP_INTERNAL_ERROR = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());

	// User Operations
	public static final class Reg {
		public static final String SUCCESS = "User Registered Successfully";
		public static final String SUMMARY = "Register a new user";
		public static final String DESC = "Creates a new user account with the provided details";
		public static final String ALREADY_EXISTS = "User already exists";

		// Add other user-related messages here
	}

	public static final class Val {
		public static final String SUMMARY = "Validate user credentials";
		public static final String DESC = "Validates user's email and password combination";
		public static final String INVALID_CREDENTIALS = "Invalid email or password";
		public static final String SUCCESS = "User Validated Successfully";
	}

	// User Deletion
	public static final class Del {
		public static final String SUMMARY = "Delete a user";
		public static final String DESC = "Deletes a user by email address (Admin only)";
		public static final String NOT_EXIST = "User not found with email:";
		public static final String SUCCESS = "User Deleted Successfully";
	}

	// User Retrieve
	public static final class Get {
		public static final String SUMMARY = "Retrieve Users";
		public static final String DESC = "Retrieve all the Users";
		public static final String SUCCESS = "Users Retrieved Successfully";
		public static final String EMPTY = "Users list is empty";
	}
}