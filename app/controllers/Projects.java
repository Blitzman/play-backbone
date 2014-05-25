package controllers;

import java.util.*;

import static play.data.Form.*;
import play.mvc.*;
import play.mvc.Http.Context;
import views.html.*;
import views.html.projects.*;
import models.*;

@Security.Authenticated(Secured.class)
public class Projects extends Controller
{
	public static Result index() 
	{
        return ok(dashboard.render(
                Project.findInvolving(request().username()),
                Task.findTodoInvolving(request().username()),
                User.find.byId(request().username())));
    }

	public static Result add ()
	{
		Project project = Project.create("New Project", form().bindFromRequest().get("group"), request().username());
		return ok(item.render(project));
	}
	
	public static Result rename (Long project)
	{
		if (Secured.isMemberOf(project))
			return ok(Project.rename(project, form().bindFromRequest().get("name")));
		else
			return forbidden();
	}
	
	public static Result delete (Long project)
	{
		if (Secured.isMemberOf(project))
		{
			Project.find.ref(project).delete();
			return ok();
		}
		else
			return forbidden();
	}
	
	public static Result addGroup ()
	{
		return ok(group.render("New group", new ArrayList<Project>()));
	}
  
    public static Result deleteGroup (String group) 
    {
        Project.deleteInFolder(group);
        return ok();
    }
  
    public static Result renameGroup (String group) 
    {
        return ok(Project.renameFolder(group, form().bindFromRequest().get("name")));
    }
  
    public static Result addUser (Long project) 
    {
        if(Secured.isMemberOf(project)) 
        {
            Project.addMember(project,form().bindFromRequest().get("user"));
            return ok();
        } 
        else
            return forbidden();
    }
  
    public static Result removeUser (Long project) 
    {
        if(Secured.isMemberOf(project)) 
        {
            Project.removeMember(project,form().bindFromRequest().get("user"));
            return ok();
        } 
        else
            return forbidden();
    }
}
