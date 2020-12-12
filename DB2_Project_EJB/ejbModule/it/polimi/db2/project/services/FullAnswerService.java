package it.polimi.db2.project.services;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class FullAnswerService {
	
	@PersistenceContext(unitName = "DB2_Project_EJB")
	private EntityManager em;

	public FullAnswerService() { }
	
}
