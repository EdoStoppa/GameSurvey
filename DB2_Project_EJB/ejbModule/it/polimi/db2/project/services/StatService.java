package it.polimi.db2.project.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import it.polimi.db2.project.entities.Stat;
//import it.polimi.db2.album.exceptions.*;


@Stateless
public class StatService {
	
	@PersistenceContext(unitName = "DB2_Project_EJB")
	private EntityManager em;

	public StatService() { }

}