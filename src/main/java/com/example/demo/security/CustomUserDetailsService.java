package com.example.demo.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user= userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found with email:"+email));
		return new org.springframework.security.core.userdetails.User(
				email,
				user.getPassword(),
				AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles()));
	}

}
