package it.polimi.db2.project.services;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.project.entities.*;


@Stateless
public class FullAnswerService {
	
	@PersistenceContext(unitName = "DB2_Project_EJB")
	private EntityManager em;

	public FullAnswerService() { }
	
	// Save every answer given by the user on DB
	public void logAnswers(AnswerLog aLog, List<Question> qList, List<String> aList) {

		for(int i=0; i<qList.size(); i++) {
			
			// Get a question and its answer
			Question q = qList.get(i);
			String a = aList.get(i);
			
			// Generate the full answer entry
			FullAnswer fullAnsw = new FullAnswer(aLog, q, a);
			
			// Persist the answer
			em.persist(fullAnsw);
			
		}
		
	}
	
	// Returns a list of answers for the specified answerLog
	public List<FullAnswer> getAnswersForAnswerLog(int answerLogId) throws Exception {
		
		List<FullAnswer> answers;

		try {
			
			answers = em.createNamedQuery("FullAnswer.getAnswersForLog", FullAnswer.class)
					.setParameter(1, answerLogId)
					.getResultList();
			
		} catch (PersistenceException e) {
			throw new Exception("Could not get list of answers");
		}
		
		if (answers.size() == 0) {		
			return null;
		} else {
			return answers;
		}
		
	}
	
}
