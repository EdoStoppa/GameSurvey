package it.polimi.db2.project.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
* Persistence class for the ProductOfDay DB table
*
*/

@Entity
@Table(name = "ProdOfDay", schema = "DB2Project")

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
  private Date choosenDate;


  // Getters and Setters
  public int getId() { return this.prodOfDayId; }

  public Product getProduct() { return this.product; }
  public void setProduct(Product product) { this.product = product; }

  public Date getChoosenDate() { return this.choosenDate; }
  public void setChoosenDate(Date choosenDate) { this.choosenDate = choosenDate; }


  // Inits
  public ProdOfDay() { }
  
  public ProdOfDay(Product product, Date choosenDate) {
    this.product = product;
    this.choosenDate = choosenDate;
  }

}
