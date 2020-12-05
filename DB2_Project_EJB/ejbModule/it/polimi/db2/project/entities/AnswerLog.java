package it.polimi.db2.project.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
* Persistence class for the AnswerLog DB table
*
*/

@Entity
@Table(name = "AnswerLog", schema = "db2Project")
@NamedQuery(name = "AnswerLog.getAnswersLogByProductId", query = "SELECT r FROM AnswerLog r  WHERE r.prodOfDay.prodOfDayId = ?1")

public class AnswerLog implements Serializable {

  // Serializable
  private static final long serialVersionUID = 1L;

  // Properties
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int logId;

  @ManyToOne
  @JoinColumn(name = "userId")
  private User user;

  @ManyToOne
  @JoinColumn(name = "prodOfDayID")
  private ProdOfDay prodOfDay;

  @Temporal(TemporalType.DATE)
  private Date logTime;

  private Boolean confirmed;

  // Getters and Setters
  public int getId() { return this.logId; }

  public User getUser() { return this.user; }
  public void setUser(User user) { this.user = user; }

  public ProdOfDay getProdOfDay() { return this.prodOfDay; }
  public void setProdOfDay(ProdOfDay prodOfDay) { this.prodOfDay = prodOfDay; }

  public Date getLogTime() { return this.logTime; }
  public void setLogTime(Date logTime) { this.logTime = logTime; }

  public Boolean getConfirmed() { return this.confirmed; }
  public void setConfirmed(Boolean confirmed) { this.confirmed = confirmed; }


  // Inits
  public AnswerLog() { }
  
  public AnswerLog(User user, ProdOfDay prodOfDay, Date logTime, Boolean confirmed) {
    this.user = user;
    this.prodOfDay = prodOfDay;
    this.logTime = logTime;
    this.confirmed = confirmed;
  }

}
