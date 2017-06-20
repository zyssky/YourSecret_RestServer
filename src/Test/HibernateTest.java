package Test;

import org.junit.Test;

import Api.UserApi;
import Model.User;

public class HibernateTest {
	@Test
	public void testUser(){
		User user = UserApi.createOrUpdateToken("dbedb4b52e0603fcb3eb6fdd0237366fd41f26c26149ba20b0f7b04ce5900e02");
		
	}
}
