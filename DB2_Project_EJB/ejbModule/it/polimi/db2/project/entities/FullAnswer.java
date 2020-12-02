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
  @EmbeddedId
  private FullAnswerId fullAnswerId;

  private String answer;


  // Getters and Setters
  public FullAnswerId getId() { return this.fullAnswerId; }

  public String getAnswer() { return this.answer; }
  public void setAnswer(String answer) { this.answer = answer; }


  // Inits
  public FullAnswer() { }
  
  public FullAnswer(String answer) {
    this.answer = answer;
  }

}
