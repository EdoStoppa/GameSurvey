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

  // Enums
  public enum Sex { MALE, FEMALE, OTHER }
  public enum Expertise { LOW, MEDIUM, HIGH }

  // Serializable
  private static final long serialVersionUID = 1L;

  // Properties
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int statId;

  @OneToOne
  private AnswerLog answerLog;

  private Sex sex;
  private int age;
  private Expertise expertise;


  // Getters and Setters
  public int getId() { return this.statId; }

  public AnswerLog getAnswerLog() { return this.answerLog; }
  public void setAnswerLog(AnswerLog answerLog) { this.answerLog = answerLog; }

  public Sex getSex() { return this.sex; }
  public void setSex(Sex sex) { this.sex = sex; }

  public int getAge() { return this.age; }
  public void setAge(int age) { this.age = age; }

  public Expertise getExpertise() { return this.expertise; }
  public void setExpertise(Expertise expertise) { this.expertise = expertise; }

  // Inits
  public Stat() { }
  
  public Stat(AnswerLog answerLog, Sex sex, int age, Expertise expertise) {
    this.answerLog = answerLog;
    this.sex = sex;
    this.age = age;
    this.expertise = expertise;
  }

}
