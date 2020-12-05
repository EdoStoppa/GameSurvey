package it.polimi.db2.project.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
* Persistence class for the UserTable DB table
*
*/

@Entity
@Table(name = "AdminTable", schema = "db2Project")
@NamedQuery(name = "Admin.isAdmin", query = "SELECT r FROM Admin r  WHERE r.user.userId = ?1")

public class Admin implements Serializable{
	
	// Serializable
	private static final long serialVersionUID = 1L;
	  
	// Properties
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int accountId;
	
	@OneToOne
	@JoinColumn(name = "userId")
	private User user;
	
	
	// Getters and Setters
	public int getId() { return this.accountId; }
	
	public User getUser() { return this.user; }
	public void setUser(User user) { this.user = user; }
	
	
	// Inits
	public Admin() { }

	public Admin(User user) {
		this.user = user;
	}
}
