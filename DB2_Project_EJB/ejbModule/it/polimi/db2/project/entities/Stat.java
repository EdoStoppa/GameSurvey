package it.polimi.db2.project.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
* Persistence class for the Stat DB table
*
*/

@Entity
@Table(name = "Stat", schema = "db2Project")

public class Stat implements Serializable {

  /*// Enums
  public enum Sex { MALE, FEMALE, OTHER }
  public enum Expertise { LOW, MEDIUM, HIGH }*/

  // Serializable
  private static final long serialVersionUID = 1L;

  // Properties
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int statId;

  @OneToOne
  private AnswerLog answerLog;

  private String sex;
  private int age;
  private String expertise;


  // Getters and Setters
  public int getId() { return this.statId; }

  public AnswerLog getAnswerLog() { return this.answerLog; }
  public void setAnswerLog(AnswerLog answerLog) { this.answerLog = answerLog; }

  public String getSex() { return this.sex; }
  public void setSex(String sex) { this.sex = sex; }

  public int getAge() { return this.age; }
  public void setAge(int age) { this.age = age; }

  public String getExpertise() { return this.expertise; }
  public void setExpertise(String expertise) { this.expertise = expertise; }

  // Inits
  public Stat() { }
  
  public Stat(AnswerLog answerLog, String sex, int age, String expertise) {
    this.answerLog = answerLog;
    this.sex = sex;
    this.age = age;
    this.expertise = expertise;
  }

}
