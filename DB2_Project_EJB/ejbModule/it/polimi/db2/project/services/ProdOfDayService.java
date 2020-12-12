package it.polimi.db2.project.services;

import java.util.Date;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.project.entities.ProdOfDay;
import it.polimi.db2.project.exceptions.*;


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
			prodList = em.createNamedQuery("ProdOfDay.getPOfDayByDate", ProdOfDay.class).setParameter(1, date).getResultList();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new DatabaseException("DB Error");
		}
		
		
		if (prodList.isEmpty())
			return null;
		if (prodList.size() == 1)
			return prodList.get(0);
		
		throw new NotUniqueException("More than one product for the specified date");
		
	}
	
	public ProdOfDay getProductOfDayFor(Integer id) throws Exception {
		
		List<ProdOfDay> prodList;
		
		try {
			prodList = em.createNamedQuery("ProdOfDay.getPOfDayById", ProdOfDay.class).setParameter(1, id).getResultList();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new DatabaseException("DB Error");
		}
		
		
		if (prodList.isEmpty())
			return null;
		if (prodList.size() == 1)
			return prodList.get(0);
		
		throw new NotUniqueException("More than one product for the specified date");
		
	}
	
	public List<ProdOfDay> getAll() throws Exception {
		
		List<ProdOfDay> prodList;

		try {
			prodList = em.createNamedQuery("ProdOfDay.getAll", ProdOfDay.class).getResultList();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new DatabaseException("DB Error");
		}

		if (prodList.isEmpty())
			return null;
		
		return prodList;
		
	}
	
	public void deleteFor(Integer id) throws Exception {
		
		ProdOfDay productOfDay = getProductOfDayFor(id);
		em.remove(productOfDay);
		
	}
	
}