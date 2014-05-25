package controllers;

import java.util.*;

import static play.data.Form.form;
import play.mvc.*;
import play.mvc.Http.Context;
import views.html.group;
import views.html.item;
import views.html.projectItem;
import models.*;

@Security.Authenticated(Secured.class)
public class Projects extends Controller
{
	public static Result add ()
	{
		Project project = Project.create(
				"New Project", 
				form().bindFromRequest().get("group"), 
				request().username());
		
		return ok(projectItem.render(project));
	}
	
	public static boolean isMemberOf (Long project)
	{
		return Project.isMember(
				project,
				Context.current().request().username());
	}
	
	public static Result rename (Long project)
	{
		if (isMemberOf(project))
			return ok(Project.rename(project, form().bindFromRequest().get("name")));
		else
			return forbidden();
	}
	
	public static Result delete (Long project)
	{
		if (isMemberOf(project))
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
}
