package com.tse.de3.spring_api_rest.exception;

public class PlayerNotFoundException extends RuntimeException {

	public PlayerNotFoundException(Long id) {
		super("Could not find player " + id);
	}
}
