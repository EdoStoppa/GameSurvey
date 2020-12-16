package it.polimi.db2.project.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.project.entities.AnswerLog;


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
	
}
