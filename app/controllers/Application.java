package controllers;

import models.Project;
import models.Task;
import models.User;
import play.*;
import play.data.*;
import play.mvc.*;
import views.html.*;

import static play.data.Form.*;

public class Application extends Controller 
{
	public static class Login
	{
		public String email;
		public String pass;

		public String validate()
		{
			if (User.authenticate(email, pass) == null)
				return "Invalid user or password.";
		
			return null;
		}
	}
	
	public static Result authenticate()
	{
		Form<Login> loginForm = Form.form(Login.class).bindFromRequest();

		if (loginForm.hasErrors())
			return badRequest(login.render(loginForm));
		else
		{
			session().clear();
			session("email", loginForm.get().email);

			return redirect(routes.Application.index());
		}
	}
	
	public static Result login()
	{
		return ok(login.render(Form.form(Login.class)));
	}

	public static Result logout()
	{
		session().clear();
		flash("success", "You've been logged out.");

		return redirect(routes.Application.login());
	}
	
	@Security.Authenticated(Secured.class)
    public static Result index() 
    {
        return ok(index.render(
        	Project.findInvolving(request().username()), 
        	Task.findTodoInvolving(request().username()),
        	User.find.byId(request().username())
        	));
    }
}
