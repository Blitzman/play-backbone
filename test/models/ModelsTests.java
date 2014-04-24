package models;

import java.util.List;

import models.*;

import org.junit.*;

import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;

public class ModelsTests extends WithApplication
{
	@Before
	public void setUp ()
	{
		start(fakeApplication(inMemoryDatabase()));
		User user = new User("test@test.com", "Test", "test");
		user.save();
		User user2 = new User("test2@test.com", "Test2", "test");
		user2.save();
		
		Project project = Project.create("Project1", "projects", "test@test.com");
		Project project2 = Project.create("Project2", "projects", "test2@test.com");
		
		Task t1 = new Task();
		t1.title = "Codify tests";
		t1.assignedTo = user;
		t1.done = true;
		t1.save();
		Task t2 = new Task();
		t2.title = "Keep codifying tests!";
		t2.project = project;
		t2.save();
	}
	
	@Test
	public void createAndRetrieveUser ()
	{
		User user = User.find.where().eq("email", "test@test.com").findUnique();
		assertNotNull(user);
		assertEquals("Test", user.name);
	}
	
	@Test
	public void authenticateUser ()
	{
		assertNotNull(User.authenticate("test@test.com", "test"));
		assertNull(User.authenticate("test@test.com", "badpass"));
		assertNull(User.authenticate("badtest@test.com", "pass"));
	}
	
	@Test
	public void findProjectsInvolving ()
	{
		List<Project> results = Project.findInvolving("test@test.com");
		assertEquals(1, results.size());
		assertEquals("Project1", results.get(0).name);
	}
	
	@Test
	public void findTodoTasksInvolving ()
	{
		List<Task> results = Task.findTodoInvolving("test@test.com");
		assertEquals(1, results.size());
		assertEquals("Keep codifying tests!", results.get(0).title);
	}
}
