package it.polimi.db2.project.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;


/**
* This class defines a composite key for the FullAnswer class
*
*/

@Embeddable
public class FullAnswerId implements Serializable {

  // Serializable
  private static final long serialVersionUID = 1L;

  // Properties
  private int logId;
  private int questId;
  
  
  // Getters and Setters
  public int getLogId() { return this.logId; }
  public void setLogId(int logId) { this.logId = logId; }
  
  public int getQuestId() { return this.questId; }
  public void setQuestId(int questId) { this.questId = questId; }
  
  
  // Inits
  public FullAnswerId() {}
  
  public FullAnswerId(int logId, int questId) {
	  this.logId = logId;
	  this.questId = questId;
  }
  
  
  // Methods
  public int hashCode() {
     return questId + (logId * 10000);
  }

  public boolean equals(Object obj) {
     if (obj == this) return true;
     if (!(obj instanceof FullAnswerId)) return false;
     
     FullAnswerId faid = (FullAnswerId) obj;
     return faid.logId == logId && faid.questId == questId ;
  }
  
}