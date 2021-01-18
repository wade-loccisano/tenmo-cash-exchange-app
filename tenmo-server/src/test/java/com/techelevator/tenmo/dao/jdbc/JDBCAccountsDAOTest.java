package com.techelevator.tenmo.dao.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.tenmo.dao.AccountsDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.dao.UserSqlDAO;
import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.User;

@SpringBootTest
class JDBCAccountsDAOTest {

	@Autowired
	private static SingleConnectionDataSource dataSource;
	
	@Autowired
	private AccountsDAO dao;
	
	@Autowired
	private UserDAO userDao;
	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:8080/");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}
	
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}
	
	@Before
	public void setup() {
		dao = new JDBCAccountsDAO(dataSource);
		userDao = new UserSqlDAO(new JdbcTemplate(dataSource));
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void create_should_make_a_user() {
		boolean bool = userDao.create("Username", "Password");
		assertTrue(bool);
	}
	
	@Test
	public void listBalance_returns_a_balance() {
		boolean bool = userDao.create("Testo", "test");
		Accounts test = dao.listBalance("Testo");
		
		assertEquals(1000.0, test.getBalance(), .001);
	}
	
	@Test
	public void listUsers_returns_a_list_of_users() {
		boolean bool = userDao.create("Testo", "test");
		List<User> list = dao.listUsers("Testo");
		
		assertNotNull(list);
	}
	
	@Test
	public void listBalance_by_Id_Lists_Balance() {
		boolean bool = userDao.create("Testo", "test");
		Accounts test = dao.listBalance("Testo");
		
		Accounts actual = dao.listBalanceById(test.getUserId());
		
		assertEquals(1000.0, actual.getBalance(), .001);
	}

}
