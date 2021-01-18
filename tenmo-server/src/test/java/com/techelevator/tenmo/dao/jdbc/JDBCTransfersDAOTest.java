package com.techelevator.tenmo.dao.jdbc;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.tenmo.dao.TransfersDAO;
import com.techelevator.tenmo.dao.UserSqlDAO;
import com.techelevator.tenmo.model.Transfers;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

@SpringBootTest
class JDBCTransfersDAOTest {

	@Autowired
	private static SingleConnectionDataSource dataSource;
	
	@Autowired
	private TransfersDAO dao;
	
	@Autowired
	private UserSqlDAO userDao;
	
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
		dao = new JDBCTransfersDAO(new JdbcTemplate(dataSource));
		userDao = new UserSqlDAO(new JdbcTemplate(dataSource));
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void getTransferById_returns_transfer_corresponding_to_id() {
		Transfers transfer = new Transfers(99, 1, 1, 1, 2, 99.0);
		dao.sendTransfer(transfer, "user");
		Transfers actual = dao.getTransferById(99);
		
		assertEquals(transfer.getTransferId(), actual.getTransferId());
	}
	
	@Test
	public void listAll_lists_all_transfers() {
		Transfers transfer = new Transfers(99, 1, 1, 1, 2, 99.0);
		List<Transfers> expected = dao.listAll("admin");
		dao.sendTransfer(transfer, "admin");
		List<Transfers> actual = dao.listAll("admin");
		
		assertEquals(expected.size(), actual.size());
	}

}
