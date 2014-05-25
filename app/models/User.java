package models;

import javax.persistence.*;
import play.db.ebean.*;
import com.avaje.ebean.*;
import java.util.List;

@Entity
@Table(name="account")
public class User extends Model 
{
	@Id
	public String email;
	public String name;
	public String password;
	
	public static Finder<String, User> find = new Finder<String, User>(String.class, User.class);
	
	public User (String email, String name, String password)
	{
		this.email 	= email;
		this.name 	= name;
		this.password 	= password;
	}
	
	public static User authenticate (String email, String password)
	{
		System.out.println("Authenticating...");
		System.out.println(email);
		System.out.println(password);
		return find.where().eq("email", email).eq("password", password).findUnique();
	}

	public static List<User> findAll() 
	{
        return find.all();
    }

    public static User findByEmail(String email) 
    {
        return find.where().eq("email", email).findUnique();
    }

    public String toString() 
    {
        return "User(" + email + ")";
    }
}
