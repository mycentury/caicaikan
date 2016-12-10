/**
 * 
 */
package win.caicaikan.service;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.BaseTest;
import win.caicaikan.repository.mongodb.entity.UserEntity;

/**
 * @desc
 * @author yanwenge
 * @date 2016年12月10日
 * @class UserServiceTest
 */
public class UserServiceTest extends BaseTest {

	@Autowired
	private UserService userService;

	/**
	 * Test method for
	 * {@link win.caicaikan.service.UserService#findById(java.lang.String)}.
	 */
	@Test
	public void testFindById() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link win.caicaikan.service.UserService#insert(win.caicaikan.repository.mongodb.entity.UserEntity)}
	 * .
	 */
	@Test
	public void testInsert() {
		UserEntity user = new UserEntity();
		user.setPrimaryKey("A", "yanabel");
		user.setPassword("admin123");
		user.setLevel("10");
		user.setStatus("1");
		userService.insert(user);
	}

	/**
	 * Test method for
	 * {@link win.caicaikan.service.UserService#save(win.caicaikan.repository.mongodb.entity.UserEntity)}
	 * .
	 */
	@Test
	public void testSave() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link win.caicaikan.service.UserService#exists(win.caicaikan.repository.mongodb.entity.UserEntity)}
	 * .
	 */
	@Test
	public void testExists() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link win.caicaikan.service.UserService#findByEntity(win.caicaikan.repository.mongodb.entity.UserEntity)}
	 * .
	 */
	@Test
	public void testFindByEntity() {
		fail("Not yet implemented");
	}

}
