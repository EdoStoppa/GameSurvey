package it.polimi.db2.project.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import it.polimi.db2.project.entities.*;
import it.polimi.db2.project.exceptions.NotUniqueException;

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
			leadList = em.createNamedQuery("Leaderboard.getLeaderboard", Leaderboard.class)
					.getResultList();
		} catch (PersistenceException e) {
			throw new Exception("Could not reach DB");
		}
		
		// Do a refresh on User (it's possible that their points are changed for what's stored in the persistence context)
		for(Leaderboard l : leadList) {
			em.refresh(l.getUser());
		}
		
		return leadList;
		
	}
	
	// Return true if the passed user is present, false otherwhise
	public Boolean isUserInLeaderboard(int userId) throws Exception, NotUniqueException {
		
		List<Leaderboard> leadList = null;
		try {
			leadList = em.createNamedQuery("Leaderboard.getByUserID", Leaderboard.class)
					.setParameter(1, userId)
					.getResultList();
		} catch (PersistenceException e) {
			throw new Exception("Could not reach DB");
		}
		
		if (leadList.size() > 1) { throw new NotUniqueException("Multiple users registered with the same Id"); }
		else { return leadList.size() == 1; }
		
	}

}

