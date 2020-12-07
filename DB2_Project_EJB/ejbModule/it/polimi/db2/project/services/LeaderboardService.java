package it.polimi.db2.project.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import it.polimi.db2.project.entities.*;

//import it.polimi.db2.album.exceptions.*;
import java.util.List;

@Stateless
public class LeaderboardService {
	
	@PersistenceContext(unitName = "DB2_Project_EJB")
	private EntityManager em;
	
	public LeaderboardService() { }
	
	// Registers a new user
	public List<Leaderboard> getLeaderboard() throws Exception {
		List<Leaderboard> leadList = null;
		try {
			leadList = em.createNamedQuery("Admin.isAdmin", Leaderboard.class)
					.getResultList();
		} catch (PersistenceException e) {
			throw new Exception("Could not reach DB");
		}
		
		// If aList is empty, then he isn't an admin, otherwise he is
		return leadList;
		
	}

}

