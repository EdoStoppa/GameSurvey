package it.polimi.db2.project.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
* Persistence class for the UserTable DB table
*
*/

@Entity
@Table(name = "UserTable", schema = "db2Project")

public class User implements Serializable {

  // Serializable
  private static final long serialVersionUID = 1L;

  // Properties
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int userId;

  private String username;
  private String passw;
  private String email;
  private boolean blocked;
  private int totPoints;


  // Getters and Setters
  public int getId() { return this.userId; }

  public String getUsername() { return this.username; }
  public void setUsername(String username) { this.username = username; }

  public String getPassw() { return this.passw; }
  public void setPassw(String passw) { this.passw = passw; }

  public String getEmail() { return this.email; }
  public void setEmail(String email) { this.email = email; }

  public boolean getBlocked() { return this.blocked; }
  public void blockUser() { this.blocked = true; }      // Note: a user cannot be unlocked

  public int getTotPoints() { return this.totPoints; }
  public void setTotPoints(int totPoints) { this.totPoints = totPoints; }


  // Inits
  public User() {
    blocked = false;
    totPoints = 0;
  }

  public User(String username, String passw, String email) {
    this.username = username;
    this.passw = passw;
    this.email = email;

    blocked = false;
    totPoints = 0;
  }

  public User(String username, String passw, String email, boolean blocked, int totPoints) {
    this.username = username;
    this.passw = passw;
    this.email = email;
    this.blocked = blocked;
    this.totPoints = totPoints;
  }

}
