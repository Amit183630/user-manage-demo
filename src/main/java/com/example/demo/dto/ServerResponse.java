package com.example.demo.dto;

import lombok.Data;

@Data
public class ServerResponse {

	private boolean success;
	private String msg;
	private Object data;

	public ServerResponse(String msg, Object data) {
		super();
		this.success = true;
		this.msg = msg;
		this.data = data;
	}

	public ServerResponse(boolean success, String msg) {
		super();
		this.success = success;
		this.msg = msg;
	}

}
