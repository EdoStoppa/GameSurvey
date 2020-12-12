package it.polimi.db2.project.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


/**
* Persistence class for the Question DB table
*
*/

@Entity
@Table(name = "Question", schema = "db2Project")

public class Question implements Serializable {

  // Serializable
  private static final long serialVersionUID = 1L;

  // Properties
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int questId;

  @ManyToOne
  @JoinColumn(name = "prodOfDayID")
  private ProdOfDay prodOfDay;

  private String question;
  

  // Getters and Setters
  public int getId() { return this.questId; }

  public ProdOfDay getProdOfDay() { return this.prodOfDay; }
  public void setProdOfDay(ProdOfDay prodOfDay) { this.prodOfDay = prodOfDay; }

  public String getQuestion() { return this.question; }
  public void setQuestion(String question) { this.question = question; }
	

  // Inits
  public Question() { }
  
  public Question(ProdOfDay prodOfDay, String question) {
    this.prodOfDay = prodOfDay;
    this.question = question;
  }

}
