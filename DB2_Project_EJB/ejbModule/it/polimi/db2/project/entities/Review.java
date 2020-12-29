package it.polimi.db2.project.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
* Persistence class for the Review DB table
*
*/

@Entity
@Table(name = "Review", schema = "db2Project")
@NamedQuery(name = "Review.getReviewsByProductId", query = "SELECT r FROM Review r WHERE r.prodOfDay.prodOfDayId = ?1")

public class Review implements Serializable {

  // Serializable
  private static final long serialVersionUID = 1L;

  // Properties
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int reviewId;

  @OneToOne
  @JoinColumn(name = "userId")
  private User user;
  
  @ManyToOne
  @JoinColumn(name = "prodOfDayID")
  private ProdOfDay prodOfDay;

  private String reviewText;
  

  // Getters and Setters
  public int getId() { return this.reviewId; }
  
  public User getUser() { return this.user; }
  public void setUser(User user) { this.user = user; }

  public ProdOfDay getProdOfDay() { return this.prodOfDay; }
  public void setProdOfDay(ProdOfDay prodOfDay) { this.prodOfDay = prodOfDay; }

  public String getReviewText() { return this.reviewText; }
  public void setReviewText(String reviewText) { this.reviewText = reviewText; }
	

  // Inits
  public Review() { }
  
  public Review(User user, ProdOfDay prodOfDay, String reviewText) {
	this.user = user;
    this.prodOfDay = prodOfDay;
    this.reviewText = reviewText;
  }

}
