//package com.example.demo.config;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import com.example.demo.model.User;
//import com.example.demo.repository.UserRepository;
//
//import lombok.RequiredArgsConstructor;
//
//@Configuration
//@RequiredArgsConstructor
//public class AdminUserInitializer {
//
//	private final PasswordEncoder passwordEncoder;
//
//	@Bean
//	CommandLineRunner initAdminUser(UserRepository userRepository) {
//		return args -> {
//			String adminEmail = "admin@example.com";
//
//			if (userRepository.findByEmail(adminEmail).isEmpty()) {
//				User admin = new User();
//				admin.setName("Admin User");
//				admin.setEmail(adminEmail);
//				admin.setGender("Male");
//				admin.setPassword(passwordEncoder.encode("admin123"));
//				admin.setIpAddress("127.0.0.1");
//				admin.setCountry("Local");
//				admin.setRoles("ROLE_ADMIN,ROLE_USER");
//
//				userRepository.save(admin);
//				System.out.println("Admin user created successfully!");
//			}
//		};
//	}
//}