package it.polimi.db2.project.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import it.polimi.db2.project.entities.Product;
import it.polimi.db2.project.exceptions.*;


@Stateless
public class ProductService {
	
	@PersistenceContext(unitName = "DB2_Project_EJB")
	private EntityManager em;

	public ProductService() { }
	
	public Product getProdById(int id) throws Exception{
		Product prod = em.find(Product.class, id);
		
		if(prod != null)
			return prod;
		
		throw new NotFoundException("Product not found :(");
	}

}
