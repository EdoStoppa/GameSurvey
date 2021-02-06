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
	
	public boolean isUsedOffensiveWord(List<String> answList) throws Exception {
		
		// First, retrieve all the offensive words
		List<OffensiveWord> offensiveWords = getAll();
		
		// Then, for every answer, check if any of these words is present
		boolean blocked=false;
		for(String answ : answList) {
			
			if(!blocked) {
				
				// First transform the entire answer to lower case
				String answLow = answ.toLowerCase();
				// check if each offensive-word is in the answer
				for (OffensiveWord offensiveWord : offensiveWords) {
					if (answLow.contains(offensiveWord.getWord())) {
						// Simply block the search because one offensive word is found
						blocked=true;
						break;
					}
				}
				
			} else {
				// End the search
				break;
			}
			
		}
		
		return blocked;
	}
	
}
