package it.polimi.db2.project.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
* Persistence class for the ProductOfDay DB table
*
*/

@Entity
@Table(name = "ProdOfDay", schema = "db2Project")
@NamedQuery(name = "ProdOfDay.getPOfDayByDate", query = "SELECT r FROM ProdOfDay r  WHERE r.chosenDate = ?1")

public class ProdOfDay implements Serializable {

  // Serializable
  private static final long serialVersionUID = 1L;

  // Properties
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int prodOfDayId;

  @ManyToOne
  @JoinColumn(name = "prodId")
  private Product product;

  @Temporal(TemporalType.DATE)
  private Date chosenDate;


  // Getters and Setters
  public int getId() { return this.prodOfDayId; }

  public Product getProduct() { return this.product; }
  public void setProduct(Product product) { this.product = product; }

  public Date getChoosenDate() { return this.chosenDate; }
  public void setChoosenDate(Date choosenDate) { this.chosenDate = choosenDate; }


  // Inits
  public ProdOfDay() { }
  
  public ProdOfDay(Product product, Date chosenDate) {
    this.product = product;
    this.chosenDate = chosenDate;
  }

}
