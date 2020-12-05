package it.polimi.db2.project.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.NonUniqueResultException;
import it.polimi.db2.project.entities.*;
import it.polimi.db2.project.exceptions.UserTakenException;

//import it.polimi.db2.album.exceptions.*;
import java.util.List;

@Stateless
public class AdminService {
	
	@PersistenceContext(unitName = "DB2_Project_EJB")
	private EntityManager em;
	
	public AdminService() { }
	
	// Registers a new user
	public Boolean isAdmin(int usrId) throws Exception {
		List<Admin> aList = null;
		try {
			aList = em.createNamedQuery("Admin.isAdmin", Admin.class).setParameter(1, usrId)
					.getResultList();
		} catch (PersistenceException e) {
			throw new Exception("Could not reach DB");
		}
		
		// If aList is empty, then he isn't an admin, otherwise he is
		return !aList.isEmpty();
		
	}

}
