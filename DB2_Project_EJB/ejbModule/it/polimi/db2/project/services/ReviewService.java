package it.polimi.db2.project.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.project.entities.Review;

@Stateless
public class ReviewService {
	
	@PersistenceContext(unitName = "DB2_Project_EJB")
	private EntityManager em;

	public ReviewService() { }

	
	// Methods
	
	// Returns a list of all the reviews for the provided productOfDayId
	public List<Review> getReviewsForProduct(int productOfDayId) throws Exception {
		
		List<Review> reviewsForProduct;

		try {
			
			reviewsForProduct = em.createNamedQuery("Review.getReviewsByProductId", Review.class)
					.setParameter(1, productOfDayId)
					.getResultList();
			
		} catch (PersistenceException e) {
			throw new Exception("Could not get list of reviews");
		}
		
		return reviewsForProduct;
		
	}

}
