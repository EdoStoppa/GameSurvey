package it.polimi.db2.project.exceptions;

public class NotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public NotFoundException(String message) {
		super(message);
	}

}
