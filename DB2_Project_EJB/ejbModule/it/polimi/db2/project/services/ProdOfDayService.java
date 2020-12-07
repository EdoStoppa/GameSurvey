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
	
	public ProdOfDay getCurrenProdOfDay() throws Exception {
		
		Date currentDate = Calendar.getInstance().getTime();
		return getProductOfDayFor(currentDate);
		
	}

	public ProdOfDay getProductOfDayFor(Date date) throws Exception {
		
		List<ProdOfDay> prodList;
		
		try {
			prodList = em.createNamedQuery("ProdOfDay.getPOfDayByDate", ProdOfDay.class).setParameter(1, date)
					.getResultList();
		} catch (PersistenceException e) {
			throw new Exception("Could not get product of the day for the specified date");
		}
		
		if (prodList.isEmpty())
			throw new Exception("No product found");
		if (prodList.size() == 1)
			return prodList.get(0);
		
		throw new NonUniqueResultException("More than one product for the specified date");
		
	}
	
}