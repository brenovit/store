package io.github.brenovit.store.dto;

import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class GenericResponse {
	private Date timestamp;
	private int status;
	private String statusMessage;
	private String message;
	
	public GenericResponse(HttpStatus httpStatus, String message) {
		this.timestamp = new Date();
		this.status = httpStatus.value();
		this.statusMessage = httpStatus.name();
		this.message = message;
	}
	
	public GenericResponse(int status, String statusMessage, String message) {
		super();
		this.timestamp = new Date();
		this.status = status;
		this.statusMessage = statusMessage;
		this.message = message;
	}
	
}
