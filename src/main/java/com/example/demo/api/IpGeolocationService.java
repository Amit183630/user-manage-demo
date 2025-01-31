package com.example.demo.api;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class IpGeolocationService {
	private final WebClient webClient;

	public String getCountryFromIp(String ip) {
		if (!ip.equals("Unknown")) {
			try {
				return webClient.get().uri("http://ip-api.com/json/{ip}?fields=country", ip).retrieve()
						.bodyToMono(JsonNode.class).map(jsonNode -> jsonNode.path("country").asText("Unknown")).block();
			} catch (Exception e) {
				log.error("IpGeolocationService:{}", e);
				return "Unknown";
			}
		} else {
			return "Unknown";
		}

	}
}
