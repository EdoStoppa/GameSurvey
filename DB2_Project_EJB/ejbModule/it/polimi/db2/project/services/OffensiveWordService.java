package it.polimi.db2.project.services;

import java.util.Date;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.project.entities.ProdOfDay;
import it.polimi.db2.project.entities.OffensiveWord;
import it.polimi.db2.project.exceptions.*;


@Stateless
public class OffensiveWordService {
	
	@PersistenceContext(unitName = "DB2_Project_EJB")
	private EntityManager em;

	public OffensiveWordService() { }

	// Get all the offensive words
	public List<OffensiveWord> getAll() throws Exception {
		
		List<OffensiveWord> offensiveWords;

		try {
			offensiveWords = em.createNamedQuery("OffensiveWord.getAll", OffensiveWord.class).getResultList();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new DatabaseException("DB Error");
		}

		if (offensiveWords.isEmpty())
			return null;
		
		return offensiveWords;
		
	}
	
}
