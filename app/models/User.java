package models;

import javax.persistence.*;
import play.db.ebean.*;
import com.avaje.ebean.*;

@Entity
public class User extends Model 
{
	@Id
	public String email;
	public String name;
	public String pass;
	
	public static Finder<String, User> find = new Finder<String, User>(String.class, User.class);
	
	public User (String email, String name, String pass)
	{
		this.email 	= email;
		this.name 	= name;
		this.pass 	= pass;
	}
	
	public static User authenticate (String email, String pass)
	{
		return find.where().eq("email", email).eq("pass", pass).findUnique();
	}
}
