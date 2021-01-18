package com.techelevator.tenmo.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.dao.TransfersDAO;
import com.techelevator.tenmo.model.Transfers;

@Component
public class JDBCTransfersDAO implements TransfersDAO {
	
	JdbcTemplate jdbcTemplate;
	
	public JDBCTransfersDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Transfers> listAll(String username) {
		
		List<Transfers> transferList = new ArrayList<>();
		int userId = getUserId(username);
		
		//transfers FROM other users to logged in user
		String sqlFromUser = "SELECT username, transfer_id, account_from, account_to, amount FROM transfers " + 
				"        JOIN accounts ON accounts.account_id = transfers.account_from " + 
				"        JOIN users ON users.user_id = accounts.user_id WHERE transfers.account_to = ?";
		SqlRowSet resultsFrom = jdbcTemplate.queryForRowSet(sqlFromUser, userId);
		
		while (resultsFrom.next()) {
			Transfers transfer = mapRowToTransfersListFrom(resultsFrom);
			transfer.setAccountToName(username);;
			transferList.add(transfer);
		}
		
		//transfers TO other users from logged in user
		String sqlToUser = "SELECT username, transfer_id, account_from, account_to, amount FROM transfers " + 
				"        JOIN accounts ON accounts.account_id = transfers.account_to " + 
				"        JOIN users ON users.user_id = accounts.user_id WHERE transfers.account_from = ?";
		SqlRowSet resultsTo = jdbcTemplate.queryForRowSet(sqlToUser, userId);
		
		while (resultsTo.next()) {
			Transfers transfer = mapRowToTransfersListTo(resultsTo);
			transfer.setAccountFromName(username);
			transferList.add(transfer);
		}
		
		return transferList;
	}
	
	
	@Override
	public List<Transfers> listPending(String username) {
		List<Transfers> pendingTransfersList = new ArrayList<>();
		int userId = getUserId(username);
		String sql = " SELECT username, transfer_id, account_from, account_to, amount FROM transfers" + 
				"	 	 JOIN accounts ON transfers.account_to = accounts.account_id" + 
				"        JOIN users ON accounts.user_id = users.user_id" + 
				"		 WHERE transfer_status_id = 1 AND account_from = ? AND account_to != ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
		while(results.next()) {
			Transfers transfer = mapRowToPendingTransfers(results);
			transfer.setAccountFromName(username);
			pendingTransfersList.add(transfer);
		}
		return pendingTransfersList;
	}
	
	@Override
	public void sendTransfer(Transfers transfer, String username) {
		int userId = getUserId(username);
		String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, " +
				"		account_to, amount) " + 
				"       VALUES (2, 2, ?, ?, ?)";
		jdbcTemplate.update(sql, userId, transfer.getAccountTo(), transfer.getAmount());
	}
	
	@Override
	public void requestTransfer(Transfers transfer, String username) {
		int userId = getUserId(username);
		String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, " +
				"		account_to, amount) " + 
				"       VALUES (1, 1, ?, ?, ?)";
		jdbcTemplate.update(sql, transfer.getAccountFrom(), userId, transfer.getAmount());
		
	}

	@Override
	public Transfers getTransferById(int id) {
		Transfers transfer = new Transfers();
		String sql = "SELECT * FROM transfers " + 
				"JOIN accounts ON accounts.account_id = transfers.account_from " + 
				"OR accounts.account_id = transfers.account_to " + 
				"JOIN users ON users.user_id = accounts.user_id " + 
				"JOIN transfer_types ON transfers.transfer_type_id = transfer_types.transfer_type_id " + 
				"JOIN transfer_statuses ON transfers.transfer_status_id = transfer_statuses.transfer_status_id " + 
				"WHERE transfer_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
		if(results.next()){
			transfer = mapRowToTransferDetails(results);
		}
		
		sql = "SELECT username, account_from FROM users "  + 
				"        JOIN accounts ON accounts.user_id = users.user_id " + 
				"        JOIN transfers ON accounts.account_id = transfers.account_from" + 
				"        WHERE transfer_id = ?";
		results = jdbcTemplate.queryForRowSet(sql, id);
		if(results.next()) {
			transfer.setAccountFromName(results.getString("username"));
		}
		
		sql = "SELECT username, transfer_id, account_to FROM users " + 
				"        JOIN accounts ON accounts.user_id = users.user_id " + 
				"        JOIN transfers ON accounts.account_id = transfers.account_to " + 
				"        WHERE transfer_id = ?";
		results = jdbcTemplate.queryForRowSet(sql, id);
		if(results.next()) {
			transfer.setAccountToName(results.getString("username"));
		}
		return transfer;
	}
	
	private Transfers mapRowToTransfersListFrom(SqlRowSet results) {
		Transfers transfer = new Transfers();
		transfer.setAccountFromName(results.getString("username"));
		transfer.setTransferId(results.getInt("transfer_id"));
		transfer.setAccountFrom(results.getInt("account_from"));
		transfer.setAccountTo(results.getInt("account_to"));
		transfer.setAmount(results.getDouble("amount"));
		return transfer;
	}
	
	private Transfers mapRowToTransfersListTo(SqlRowSet results) {
		Transfers transfer = new Transfers();
		transfer.setAccountToName(results.getString("username"));
		transfer.setTransferId(results.getInt("transfer_id"));
		transfer.setAccountFrom(results.getInt("account_from"));
		transfer.setAccountTo(results.getInt("account_to"));
		transfer.setAmount(results.getDouble("amount"));
		return transfer;
	}
	
	
	private Transfers mapRowToPendingTransfers(SqlRowSet results) {
		Transfers transfer = new Transfers();
		transfer.setAccountToName(results.getString("username"));
		transfer.setTransferId(results.getInt("transfer_id"));
		transfer.setAccountTo(results.getInt("account_to"));
		transfer.setAmount(results.getDouble("amount"));
		return transfer;
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
	
	private Transfers mapRowToTransfers(SqlRowSet results) {
		
		Transfers transfer = new Transfers();
		transfer.setTransferId(results.getInt("transfer_id"));
		transfer.setTransferTypeId(results.getInt("transfer_type_id"));
		transfer.setTransferStatusId(results.getInt("transfer_status_id"));
		transfer.setAccountFrom(results.getInt("account_from"));
		transfer.setAccountTo(results.getInt("account_to"));
		transfer.setAmount(results.getDouble("amount"));
		return transfer;
	}
	
	private Transfers mapRowToTransferDetails(SqlRowSet results) {
		
		Transfers transfer = new Transfers();
		transfer.setTransferId(results.getInt("transfer_id"));
		transfer.setTransferTypeId(results.getInt("transfer_type_id"));
		transfer.setTransferType(results.getString("transfer_type_desc"));
		transfer.setTransferStatusId(results.getInt("transfer_status_id"));
		transfer.setTransferStatus(results.getString("transfer_status_desc"));
		transfer.setAccountFrom(results.getInt("account_from"));
		transfer.setAccountTo(results.getInt("account_to"));
		transfer.setAmount(results.getDouble("amount"));
		return transfer;
	}
	
}