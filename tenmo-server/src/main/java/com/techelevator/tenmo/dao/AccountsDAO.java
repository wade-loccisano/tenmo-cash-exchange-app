package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;



public interface AccountsDAO {

	Accounts listBalance(String username);
	
	Accounts listBalanceById(int id);
	
	List<User> listUsers(String username);
	
	void updateBalance(int id, Accounts updatedAccount);
	
	Accounts createAccount(int userId, double balance);
	
}
