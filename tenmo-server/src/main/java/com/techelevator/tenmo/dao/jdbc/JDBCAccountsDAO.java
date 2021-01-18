package com.techelevator.tenmo.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.dao.AccountsDAO;
import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;

@Component
public class JDBCAccountsDAO implements AccountsDAO{
	
	JdbcTemplate jdbcTemplate;
	
	public JDBCAccountsDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}


	@Override
	public Accounts createAccount(int userId, double balance) {
		String sql = "INSERT INTO accounts (user_id, balance) VALUES (?, ?) RETURNING user_id";
		Accounts account = jdbcTemplate.queryForObject(sql, new Object[] { userId, balance }, Accounts.class);
		return account;
	}
	
	@Override
	public Accounts listBalance(String username) {
		Accounts account = new Accounts();
		String sql = " SELECT * FROM accounts" + 
				"        JOIN users ON accounts.user_id = users.user_id" + 
				"        WHERE users.username = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
		if(results.next()) {
			account = mapRowToAccount(results);
		}
		return account;
	}

	@Override
	public List<User> listUsers(String username) {
		int userId = getUserId(username);
		List<User> users = new ArrayList<>();
		String sql = "SELECT user_id, username FROM users " +
		        "WHERE users.user_id != ?";   
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
		while (results.next()) {
			User user = mapRowToUser(results);
			users.add(user);
		}
 		return users;
	}
	
	@Override
	public void updateBalance(int userId, Accounts updatedAccount) {
		String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?";
		
		jdbcTemplate.update(sql, updatedAccount.getBalance(), userId);
	}
	
	@Override
	public Accounts listBalanceById(int id) {
			Accounts account = new Accounts();
			String sqlGetById = "SELECT * FROM accounts WHERE user_id = ?";
			SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetById, id);
			
			if(results.next()) {
				account = mapRowToAccount(results);
			}
			return account;
		}
	
	
	// helper method
	 private Accounts mapRowToAccount(SqlRowSet results) {
		 Accounts account = new Accounts();
		 account.setAccountId(results.getInt("account_id"));
		 account.setUserId(results.getInt("user_id"));
		 account.setBalance(results.getDouble("balance"));
		 return account;
	 }
	 
	 private User mapRowToUser(SqlRowSet results) {
		 User user = new User();
		 user.setId(results.getLong("user_id"));
		 user.setUsername(results.getString("username"));
		 return user;
	 }

	
	private int getUserId(String username) {
		String sql = "SELECT user_id FROM users WHERE "
				+ "username = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
		int userId = 0;
		if (results.next()) {
			userId = results.getInt("user_id");
		}
		return userId;
	}
	
	private int getNextAccountId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_account_id')");
		
		if(nextIdResult.next()) {
			return nextIdResult.getInt(1);
		}
		throw new RuntimeException("Error in getNextAccounttId");
	}
	
}
