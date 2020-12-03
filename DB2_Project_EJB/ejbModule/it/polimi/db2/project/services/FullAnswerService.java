package it.polimi.db2.project.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import it.polimi.db2.project.entities.FullAnswer;
//import it.polimi.db2.album.exceptions.*;


@Stateless
public class FullAnswerService {
	
	@PersistenceContext(unitName = "DB2_Project_EJB")
	private EntityManager em;

	public FullAnswerService() { }

}
