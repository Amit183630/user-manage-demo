package com.example.demo.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class IpFinderServiceTest {

	@Mock
	private WebClient webClient;

	@Mock
	private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

	@Mock
	private WebClient.ResponseSpec responseSpec;

	@InjectMocks
	private IpFinderService ipFinderService;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
	}

	@Test
	void testGetIp_Success() throws Exception {
		// Arrange
		String jsonResponse = "{\"ip\": \"192.168.1.1\"}";
		JsonNode jsonNode = objectMapper.readTree(jsonResponse);

		when(webClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.just(jsonNode));

		// Act
		String ip = ipFinderService.getIp();

		// Assert
		assertEquals("192.168.1.1", ip);
	}

	@Test
	void testGetIp_Exception() {
		// Arrange
		when(webClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.error(new RuntimeException("API Error")));

		// Act
		String ip = ipFinderService.getIp();

		// Assert
		assertEquals("Unknown", ip);
	}
}