package it.polimi.db2.project.exceptions;

public class UserTakenException extends Exception {

	private static final long serialVersionUID = 1L;

	public UserTakenException(String message) {
		super(message);
	}
	
}
