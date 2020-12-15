package it.polimi.db2.project.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.project.entities.ProdOfDay;
import it.polimi.db2.project.entities.Product;
import it.polimi.db2.project.exceptions.*;


@Stateless
public class ProductService {
	
	@PersistenceContext(unitName = "DB2_Project_EJB")
	private EntityManager em;

	public ProductService() { }
	
	public Product getProdById(int id) throws Exception {
		Product prod = em.find(Product.class, id);
		
		if(prod != null)
			return prod;
		
		throw new NotFoundException("Product not found :(");
	}

	public List<Product> getAll() throws Exception {
		
		List<Product> prodList;

		try {
			prodList = em.createNamedQuery("Product.getAll", Product.class).getResultList();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new DatabaseException("DB Error");
		}

		if (prodList.isEmpty())
			return null;
		
		return prodList;
		
	}
	
}
