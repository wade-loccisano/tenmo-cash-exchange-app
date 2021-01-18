package com.techelevator.tenmo.dao.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.tenmo.dao.TransfersDAO;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

class JDBCTransfersDAOTest {

	private static SingleConnectionDataSource dataSource;
	private TransfersDAO dao;
	private User user;
	
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
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	void getTransferById_returns_transfer_corresponding_to_id() {
	
		
		Transfers transfer = new Transfers(99, 1, 1, 1, 2, 99.0);
		dao.sendTransfer(transfer, user.getUsername());
		
		assertEquals(transfer, dao.getTransferById(99));
	}

}
