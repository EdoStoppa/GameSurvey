package it.polimi.db2.project.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
* Persistence class for the FullAnswer DB table
*
*/

@Entity
@Table(name = "FullAnswer", schema = "db2Project")

public class FullAnswer implements Serializable {

  // Serializable
  private static final long serialVersionUID = 1L;

  // Properties
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int answerId;
  
  @ManyToOne
  @JoinColumn(name = "logId")
  private AnswerLog answerLog;
  
  @ManyToOne
  @JoinColumn(name = "questId")
  private Question question;

  private String answer;


  // Getters and Setters
  public int getId() { return this.answerId; }
  
  public AnswerLog getAnswerLog() { return this.answerLog; }
  public void setAnswerLog(AnswerLog answerLog) { this.answerLog = answerLog; }
  
  public Question getQustion() { return this.question; }
  public void setQuestion(Question question) { this.question = question; }

  public String getAnswer() { return this.answer; }
  public void setAnswer(String answer) { this.answer = answer; }


  // Inits
  public FullAnswer() { }
  
  public FullAnswer(AnswerLog answerLog, Question question, String answer) {
	  this.answerLog = answerLog;
	  this.question = question;
	  this.answer = answer;
  }

}
