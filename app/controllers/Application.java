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
		public String password;

		public String validate()
		{
			if (User.authenticate(email, password) == null)
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

			return redirect(routes.Projects.index());
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

    public static Result javascriptRoutes() {
        response().setContentType("text/javascript");
        return ok(
            Routes.javascriptRouter("jsRoutes",
                controllers.routes.javascript.Projects.add(), 
                controllers.routes.javascript.Projects.delete(), 
                controllers.routes.javascript.Projects.rename(),
                controllers.routes.javascript.Projects.addGroup(), 
                controllers.routes.javascript.Projects.deleteGroup(), 
                controllers.routes.javascript.Projects.renameGroup(),
                controllers.routes.javascript.Projects.addUser(), 
                controllers.routes.javascript.Projects.removeUser(), 
                controllers.routes.javascript.Tasks.addFolder(), 
                controllers.routes.javascript.Tasks.renameFolder(), 
                controllers.routes.javascript.Tasks.deleteFolder(), 
                controllers.routes.javascript.Tasks.index(),
                controllers.routes.javascript.Tasks.add(), 
                controllers.routes.javascript.Tasks.update(), 
                controllers.routes.javascript.Tasks.delete()));
    }
}
