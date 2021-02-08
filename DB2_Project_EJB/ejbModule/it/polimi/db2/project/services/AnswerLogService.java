package it.polimi.db2.project.services;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.project.entities.AnswerLog;
import it.polimi.db2.project.entities.ProdOfDay;
import it.polimi.db2.project.entities.User;


@Stateless
public class AnswerLogService {
	
	@PersistenceContext(unitName = "DB2_Project_EJB")
	private EntityManager em;

	public AnswerLogService() { }

	
	// Methods
	
	// Returns a list of answers' logs for the specified productOfDay's id
	public List<AnswerLog> getAnswersForProduct(int productOfDayId) throws Exception {
		
		List<AnswerLog> answersForProduct;

		try {
			
			answersForProduct = em.createNamedQuery("AnswerLog.getAnswersLogByProductId", AnswerLog.class)
					.setParameter(1, productOfDayId)
					.getResultList();
			
		} catch (PersistenceException e) {
			throw new Exception("Could not get list of answers log");
		}
		
		return answersForProduct;
		
	}
	
	// Returns the answerLog for the specified productOfDay and user's id
	public AnswerLog getConfirmedAnswerForProductAndUser(int productOfDayId, int userId) throws Exception {
		
		List<AnswerLog> answersForProduct;

		try {
			
			answersForProduct = em.createNamedQuery("AnswerLog.getAnswerLogByProductAndUserId", AnswerLog.class)
					.setParameter(1, productOfDayId)
					.setParameter(2, userId)
					.getResultList();
			
		} catch (PersistenceException e) {
			throw new Exception("Could not get list of answer logs");
		}
		
		if (answersForProduct.size() == 0) {
			throw new Exception("Could not get any answers");
		} else {
			
			AnswerLog log = null;
			for(AnswerLog l: answersForProduct) {
				if(l.getConfirmed()) {
					log = l;
					break;
				}
					
			}
			
			if(log == null)
				throw new Exception("Could not get any confirmed answer");
			
			return log;
			
		}
		
	}
	
	public AnswerLog logSubmission(User user, ProdOfDay prodOfDay, Date logTime, Boolean confirmed, int points) {
		
		// Create new log
		AnswerLog log = new AnswerLog(user, prodOfDay, logTime, confirmed, points);
		
		// Persist and flush immediately to DB
		em.persist(log);
		em.flush();
		
		return log;
	}
	
}
