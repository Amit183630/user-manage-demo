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
public class IpGeolocationServiceTest {

	@Mock
	private WebClient webClient;

	@Mock
	private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

	@Mock
	private WebClient.ResponseSpec responseSpec;

	@InjectMocks
	private IpGeolocationService ipGeolocationService;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
	}

	@Test
	void testGetCountryFromIp_Success() throws Exception {
		// Arrange
		String ip = "192.168.1.1";
		String jsonResponse = "{\"country\": \"United States\"}";
		JsonNode jsonNode = objectMapper.readTree(jsonResponse);

		when(webClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri(anyString(), anyString())).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.just(jsonNode));

		// Act
		String country = ipGeolocationService.getCountryFromIp(ip);

		// Assert
		assertEquals("United States", country);
	}

	@Test
	void testGetCountryFromIp_UnknownIp() {
		// Arrange
		String ip = "Unknown";

		// Act
		String country = ipGeolocationService.getCountryFromIp(ip);

		// Assert
		assertEquals("Unknown", country);
	}

	@Test
	void testGetCountryFromIp_Exception() {
		// Arrange
		String ip = "192.168.1.1";

		when(webClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri(anyString(), anyString())).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.error(new RuntimeException("API Error")));

		// Act
		String country = ipGeolocationService.getCountryFromIp(ip);

		// Assert
		assertEquals("Unknown", country);
	}
}