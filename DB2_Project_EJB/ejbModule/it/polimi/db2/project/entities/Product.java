package it.polimi.db2.project.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
* Persistence class for the Product DB table
*
*/

@Entity
@Table(name = "Product", schema = "db2Project")

public class Product implements Serializable {

  // Serializable
  private static final long serialVersionUID = 1L;

  // Properties
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int prodId;

  private String prodName;

  @Basic(fetch = FetchType.LAZY)
  @Lob
  private byte[] prodPhoto;


  // Getters and Setters
  public int getId() { return this.prodId; }

  public String getProdName() { return this.prodName; }
  public void setProdName(String prodName) { this.prodName = prodName; }

  public byte[] getProdPhoto() { return this.prodPhoto; }
  public void setProdPhoto(byte[] prodPhoto) { this.prodPhoto = prodPhoto; }


  // Inits
  public Product() { }
  
  public Product(String prodName, byte[] prodPhoto) {
    this.prodName = prodName;
    this.prodPhoto = prodPhoto;
  }

}
