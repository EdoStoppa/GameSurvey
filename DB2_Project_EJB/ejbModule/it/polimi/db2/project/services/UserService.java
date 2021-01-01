package it.polimi.db2.project.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.NonUniqueResultException;
import it.polimi.db2.project.entities.User;
import it.polimi.db2.project.exceptions.UserTakenException;

//import it.polimi.db2.album.exceptions.*;
import java.util.List;

@Stateless
public class UserService {
	
	@PersistenceContext(unitName = "DB2_Project_EJB")
	private EntityManager em;

	public UserService() { }

	
	// Checks the provided user's credentials
	// Returned: matched user, null if no match found
	public User checkCredentials(String usrn, String pwd) throws Exception{//CredentialsException, NonUniqueResultException {
		List<User> uList = null;
		try {
			uList = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, usrn).setParameter(2, pwd)
					.getResultList();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new Exception("Could not verify credentals");
		}
		if (uList.isEmpty())
			return null;
		else if (uList.size() == 1)
			return uList.get(0);
		throw new NonUniqueResultException("More than one user registered with same credentials");

	}
	
	// Registers a new user
	public void registerUser(String mail, String usrn, String pwd) throws Exception {
		
		List<User> uList = null;
		try {
			uList = em.createNamedQuery("User.checkUserTaken", User.class).setParameter(1, usrn).setParameter(2, mail)
					.getResultList();
		} catch (PersistenceException e) {
			throw new Exception("Could not verify credentals");
		}
		
		if (uList.isEmpty()) {
			User newUser = new User(usrn, pwd, mail);
			em.persist(newUser);
		} else {
			throw new UserTakenException("User's credentials already taken");
		}
		
	}

	// Bans the specified user
	public void banUser(Integer userId) throws Exception {
		
		List<User> usersList = null;
		try {
			usersList = em.createNamedQuery("User.getUserById", User.class).setParameter(1, userId)
					.getResultList();
		} catch (PersistenceException e) {
			throw new Exception("Could not retrieve selected user");
		}
	
		if (usersList == null || usersList.size() != 1) {
			throw new Exception("Could not retrieve selected user");
		}
		
		// Get the actual user
		User user = usersList.get(0);
		user.blockUser();
		
		em.persist(user);
		
	}
	
}

