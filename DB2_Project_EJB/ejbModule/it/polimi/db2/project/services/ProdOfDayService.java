package it.polimi.db2.project.services;

import java.util.Date;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.project.entities.ProdOfDay;
//import it.polimi.db2.project.exceptions.*;
import it.polimi.db2.project.entities.User;


@Stateless
public class ProdOfDayService {
	
	@PersistenceContext(unitName = "DB2_Project_EJB")
	private EntityManager em;

	public ProdOfDayService() { }
	
	public ProdOfDay getCurrenProdOfDay() throws Exception{
		List<ProdOfDay> prodList;
		Date currentDate = Calendar.getInstance().getTime();
		//currentDate
		try {
			prodList = em.createNamedQuery("ProdOfDay.getPOfDayByDate", ProdOfDay.class).setParameter(1, currentDate)
					.getResultList();
		} catch (PersistenceException e) {
			throw new Exception("Could not get product of the day");
		}
		
		if (prodList.isEmpty())
			throw new Exception("No product found :(");
		if (prodList.size() == 1)
			return prodList.get(0);
		
		throw new NonUniqueResultException("More than one product for today :(");
	}

}