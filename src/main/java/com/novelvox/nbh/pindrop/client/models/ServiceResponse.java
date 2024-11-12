package com.novelvox.nbh.pindrop.client.models;

import lombok.Data;

@Data
public class ServiceResponse {

	public enum Status {
		ZERO, ONE;
	}

	private Object msg;
	private Status status;

	private Object data;

	public ServiceResponse(Status status, Object msg, Object data) {
		super();
		this.msg = msg;
		this.status = status;
		this.data = data;
	}

	public ServiceResponse(Status status, Object data) {
		super();
		this.status = status;
		this.data = data;
	}

	public ServiceResponse(Status status) {
		super();
		this.status = status;
	}

}
