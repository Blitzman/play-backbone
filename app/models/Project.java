package models;

import java.util.*;
import javax.persistence.*;
import com.avaje.ebean.*;
import play.db.ebean.*;

@Entity
public class Project extends Model 
{
	@Id
	public Long id;
	public String name;
	public String folder;
	@ManyToMany(cascade = CascadeType.REMOVE)
	public List<User> members = new ArrayList<User>();
	
	public static Finder<Long, Project> find = new Finder<Long, Project>(Long.class, Project.class);
	
	public Project (String name, String folder, User owner)
	{
		this.name 	= name;
		this.folder = folder;
		this.members.add(owner);
	}
	
	public static Project create (String name, String folder, String owner)
	{
		Project project = new Project(name, folder, User.find.ref(owner));
		project.save();
		project.saveManyToManyAssociations("members");
		return project;
	}
	
	public static String rename (Long id, String name)
	{
		Project project = find.ref(id);
		project.name = name;
		project.update();
		return name;
	}
	
	public static boolean isMember (Long project, String user)
	{
		return find.where().eq("members.email", user).eq("id", project).findRowCount() > 0;
	}

	public static void addMember(Long project, String user) 
    {
        Project p = Project.find.setId(project).fetch("members", "email").findUnique();
        p.members.add(User.find.ref(user));
        p.saveManyToManyAssociations("members");
    }

    public static void removeMember(Long project, String user) 
    {
        Project p = Project.find.setId(project).fetch("members", "email").findUnique();
        p.members.remove(User.find.ref(user));
        p.saveManyToManyAssociations("members");
    }
	
	public static List<Project> findInvolving (String user)
	{
		return find.where().eq("members.email", user).findList();
	}

    public static void deleteInFolder(String folder) 
    {
        Ebean.createSqlUpdate("delete from project where folder = :folder").setParameter("folder", folder).execute();
   	}

    public static String renameFolder(String folder, String newName) 
    {
        Ebean.createSqlUpdate("update project set folder = :newName where folder = :folder").setParameter("folder", folder).setParameter("newName", newName).execute();
        return newName;
    }

    public String toString() 
    {
        return "Project(" + id + ") with " + (members == null ? "null" : members.size()) + " members";
    }
}
