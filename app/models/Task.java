package models;

import java.util.*;
import javax.persistence.*;
import com.avaje.ebean.*;
import play.db.ebean.*;

@Entity
public class Task extends Model 
{
	@Id
	public Long id;
	public String title;
	public boolean done = false;
	public Date dueDate;
	@ManyToOne
	public User assignedTo;
	public String folder;
	@ManyToOne
	public Project project;
	
	public static Finder<Long, Task> find = new Finder<Long, Task>(Long.class, Task.class);

	public static Task create (Task task, Long project, String folder)
	{
		task.project = Project.find.ref(project);
		task.folder = folder;
		task.save();
		return task;
	}
	
	public static List<Task> findTodoInvolving (String user)
	{
		return find.fetch("project").where().eq("done", false).eq("project.members.email", user).findList();
	}

	public static List<Task> findByProject(Long project) 
	{
        return Task.find.where().eq("project.id", project).findList();
    }

    public static String renameFolder(Long project, String folder, String newName) 
    {
        Ebean.createSqlUpdate("update task set folder = :newName where folder = :folder and project_id = :project").setParameter("folder", folder).setParameter("newName", newName).setParameter("project", project).execute();
        return newName;
    }
    
    public static void deleteInFolder(Long project, String folder) 
    {
        Ebean.createSqlUpdate("delete from task where folder = :folder and project_id = :project").setParameter("folder", folder).setParameter("project", project).execute();
    }

    public static void markAsDone(Long taskId, Boolean done) 
    {
        Task task = Task.find.ref(taskId);
        task.done = done;
        task.update();
    }
    
    public static boolean isOwner(Long task, String user) 
    {
        return find.where().eq("project.members.email", user).eq("id", task).findRowCount() > 0;
    }
    
    public String toString() 
    {
        return "Task(" + id + ") in project " + project;
    }
}
