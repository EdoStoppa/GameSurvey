package it.polimi.db2.project.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
* Persistence class for the UserTable DB table
*
*/

@Entity
@Table(name = "Leaderboard", schema = "db2Project")
@NamedQuery(name = "Leaderboard.getLeaderboard", query = "SELECT r FROM Leaderboard r ORDER BY r.user.totPoints DESC")
@NamedQuery(name = "Leaderboard.getByUserID", query = "SELECT r FROM Leaderboard r  WHERE r.user.userId = ?1")

public class Leaderboard implements Serializable{
	
	// Serializable
	private static final long serialVersionUID = 1L;
	  
	// Properties
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int entryId;
	
	@OneToOne
	@JoinColumn(name = "userId")
	private User user;
	
	
	// Getters and Setters
	public int getId() { return this.entryId; }
	
	public User getUser() { return this.user; }
	public void setUser(User user) { this.user = user; }
	
	
	// Inits
	public Leaderboard() { }
	
	public Leaderboard(User user) {
		this.user = user;
	}
	
}
