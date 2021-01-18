package com.techelevator.tenmo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountsDAO;
import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountsController {

	private AccountsDAO accountsDao;
	
	public AccountsController(AccountsDAO accountsDao) {
		this.accountsDao = accountsDao;
	}
	
	@RequestMapping(path = "balance", method = RequestMethod.GET)
	public Accounts listBalance(Principal principal) {
		return accountsDao.listBalance(principal.getName());
	}	
	
	@RequestMapping(path = "users", method = RequestMethod.GET)
	public List<User> listUsers(Principal principal) {
		return accountsDao.listUsers(principal.getName());
	}
	
	@RequestMapping(path = "users/{id}", method = RequestMethod.PUT)
	public void sendTransfer(@PathVariable int id, @RequestBody Accounts updatedAccount) {
		accountsDao.updateBalance(id, updatedAccount);
		
	}
	
	@RequestMapping(path = "users/{id}", method = RequestMethod.GET)
	public Accounts listBalanceById(@PathVariable int id) {
		return accountsDao.listBalanceById(id);
	}
	
//	@RequestMapping(path = "users/create/{id}", method = RequestMethod.POST)
//	public void createAccount(@RequestBody Accounts newAccount, @PathVariable int id) {
//		accountsDao.createAccount(id, 1000.0);
//	}
}


