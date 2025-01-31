package com.example.demo.api;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class IpFinderService {
	private final WebClient webClient;

	public String getIp() {
		try {
			return webClient.get().uri("https://api.ipify.org?format=json").retrieve().bodyToMono(JsonNode.class)
					.map(jsonNode -> jsonNode.path("ip").asText("Unknown")).block();
		} catch (Exception e) {
			log.error("IpFinderService:{}", e);
			return "Unknown";
		}
	}
}
