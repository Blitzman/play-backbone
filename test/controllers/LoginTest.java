package controllers;

import org.junit.*;
import java.util.*;
import play.mvc.*;
import play.libs.*;
import play.test.*;
import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

public class LoginTest extends WithApplication
{
	@Before
	public void setUp()
	{
		start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
		Ebean.save((List) Yaml.load("test-data.yml"));
	}

	@Test
	public void authenticateSuccess()
	{
		Result result = callAction(
			controllers.routes.ref.Application.authenticate(),
			fakeRequest().withFormUrlEncodedBody(ImmutableMap.of(
				"email", "bob@example.com",
				"pass", "secret"))
			);

		assertEquals(303, status(result));
		assertEquals("bob@example.com", session(result).get("email"));
	}

	@Test
	public void authenticateFailure()
	{
		Result result = callAction(
			controllers.routes.ref.Application.authenticate(),
			fakeRequest().withFormUrlEncodedBody(ImmutableMap.of(
				"email", "bob@example.com",
				"pass", "invalid"))
			);

		assertEquals(400, status(result));
		assertNull(session(result).get("email"));
	}
}