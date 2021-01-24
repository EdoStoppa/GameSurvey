package it.polimi.db2.project.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;


/**
* Persistence class for the ProductOfDay DB table
*
*/

@Entity
@Table(name = "ProdOfDay", schema = "db2Project")

@NamedQuery(name = "ProdOfDay.getAll", query = "SELECT r FROM ProdOfDay r")
@NamedQuery(name = "ProdOfDay.getPOfDayByDate", query = "SELECT r FROM ProdOfDay r  WHERE r.chosenDate = ?1")
@NamedQuery(name = "ProdOfDay.getPOfDayById", query = "SELECT r FROM ProdOfDay r  WHERE r.prodOfDayId = ?1")

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

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "prodOfDay", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<AnswerLog> answerLogs;
  
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "prodOfDay", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Question> questions;

  
  // Getters and Setters
  public int getId() { return this.prodOfDayId; }

  public Product getProduct() { return this.product; }
  public void setProduct(Product product) { this.product = product; }

  public Date getChosenDate() { return this.chosenDate; }
  public void setChosenDate(Date chosenDate) { this.chosenDate = chosenDate; }

  public List<AnswerLog> getAnswerLogs() { return this.answerLogs; }
  public void addAnswerLog(AnswerLog answerLog) {
	  this.answerLogs.add(answerLog);
	  answerLog.setProdOfDay(this);
  }
  public void removeAnswerLog(AnswerLog answerLog) {
	  this.answerLogs.remove(answerLog);
  }
  
  public List<Question> getQuestions() { return this.questions; }
  public void addQuestion(Question question) {
	  this.questions.add(question); 
	  question.setProdOfDay(this);
  }
  public void removeQuestion(Question question) {
	  this.questions.remove(question);
  }
  

  // Inits
  public ProdOfDay() { 
	this.questions = new ArrayList<Question>();
  }
  
  public ProdOfDay(Product product, Date chosenDate) {
    this.product = product;
    this.chosenDate = chosenDate;
    
    this.questions = new ArrayList<Question>();
  }

}
