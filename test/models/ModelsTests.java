package models;

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
		new User("test@test.com", "Test", "test").save();
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
}
