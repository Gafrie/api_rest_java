package com.tse.de3.spring_api_rest.exception;

public class TeamNotFoundException extends RuntimeException {

	public TeamNotFoundException(Long id) {
		super("Could not find team " + id);
	}
}
