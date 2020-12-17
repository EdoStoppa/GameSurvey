package it.polimi.db2.project.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import it.polimi.db2.project.entities.ProdOfDay;
import it.polimi.db2.project.entities.Product;
import it.polimi.db2.project.entities.Question;
//import it.polimi.db2.album.exceptions.*;


@Stateful
public class QuestionService {
	
	@PersistenceContext(unitName = "DB2_Project_EJB",  type = PersistenceContextType.EXTENDED)
	private EntityManager em;
	
	private List<Question> questList = null;
	private List<String> answerList = null;

	public QuestionService() { }
	
	// Retrieve from DB the correct prodOfDay and save the associated questions
	public void setQuestList(int pOfDayId) throws Exception{ 
		ProdOfDay pOfDay = em.find(ProdOfDay.class, pOfDayId);
		
		if(pOfDay == null)
			throw new Exception();
		
		this.questList = new ArrayList<Question>(pOfDay.getQuestions());
	}
	public List<Question> getQuestList() { return this.questList; }
	
	public void setAnswerList(List<String> answerList) { this.answerList = answerList; }
	public List<String> getAnswerList() { return this.answerList; }
	
	
	public void reset() {
		this.questList = null;
		this.answerList = null;
	}
	
	@Remove
	public void remove() {}
	
}