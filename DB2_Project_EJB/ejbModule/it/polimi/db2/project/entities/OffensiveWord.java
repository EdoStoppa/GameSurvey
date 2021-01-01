package it.polimi.db2.project.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
* Persistence class for the OffensiveWord DB table
*
*/

@Entity
@Table(name = "OffensiveWord", schema = "db2Project")
@NamedQuery(name = "OffensiveWord.getAll", query = "SELECT r FROM OffensiveWord r")

public class OffensiveWord implements Serializable {

  // Serializable
  private static final long serialVersionUID = 1L;

  // Properties
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int offensiveWordId;

  private String word;
  

  // Getters and Setters
  public int getId() { return this.offensiveWordId; }

  public String getWord() { return this.word; }
  public void setWord(String word) { this.word = word; }
	

  // Inits
  public OffensiveWord() { }
  
  public OffensiveWord(String word) {
	this.word = word;
  }

}
