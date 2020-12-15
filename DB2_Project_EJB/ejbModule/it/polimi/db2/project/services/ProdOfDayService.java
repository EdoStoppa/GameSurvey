package it.polimi.db2.project.services;

import java.util.Date;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.project.entities.ProdOfDay;
import it.polimi.db2.project.entities.User;
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

	// Adds a new product of the day
	public void addProdOfDay(ProdOfDay prodOfDay) throws Exception {
		
		if (prodOfDay != null) {
			em.persist(prodOfDay);
		} else {
			throw new Exception("Product is null!");
		}
		
	}
	
	// Get a product of the day by the date
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

	// Get a product of the day by ID
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

	// Get all the product of the day 
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

	// Delete a product of the day by ID
	public void deleteFor(Integer id) throws Exception {
		
		ProdOfDay productOfDay = getProductOfDayFor(id);
		em.remove(productOfDay);
		
	}
	
}