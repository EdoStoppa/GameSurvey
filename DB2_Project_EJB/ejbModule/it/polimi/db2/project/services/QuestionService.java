package it.polimi.db2.project.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import it.polimi.db2.project.entities.Question;
//import it.polimi.db2.album.exceptions.*;


@Stateless
public class QuestionService {
	
	@PersistenceContext(unitName = "DB2_Project_EJB")
	private EntityManager em;

	public QuestionService() { }

}