package it.polimi.db2.project.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.project.entities.AnswerLog;
import it.polimi.db2.project.entities.Stat;
//import it.polimi.db2.album.exceptions.*;


@Stateless
public class StatService {
	
	@PersistenceContext(unitName = "DB2_Project_EJB")
	private EntityManager em;

	public StatService() { }
	
	public void logStat(AnswerLog log, String sex, String age, String expertise) {
		
		// Convert the age to an Integer (if not possible, it means that the user didn't answered)
		int ageInt;
		try {
			ageInt = Integer.parseInt(age);
		} catch(Exception e) {
			ageInt = 0;
		}
		
		// Only if at least one value is inserted by the User then an entry is saved in DB
		String nopeValue = " -- ";
		if(!sex.equals(nopeValue) || !expertise.equals(nopeValue) || ageInt>0) {
			// Generate the stat
			Stat stat = new Stat(log, sex, ageInt, expertise);
			
			// Save the stat in the DB
			em.persist(stat);
			
		}
		
	}

}