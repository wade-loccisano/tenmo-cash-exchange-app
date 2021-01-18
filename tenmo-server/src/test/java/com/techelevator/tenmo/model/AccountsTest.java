package com.techelevator.tenmo.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AccountsTest {

	@Test
	public void accountsGetters_return_correct_items() {
		Accounts account = new Accounts(1, 2, 1000.0);
		
		assertEquals(1, account.getAccountId());
		assertEquals(2, account.getUserId());
		assertEquals(1000.0, account.getBalance(), .00);
	}

	@Test
	public void accountsToString_returns_correct_string() {
		Accounts account = new Accounts(1, 2, 1000.0);
		String testString = "Accounts [accountId=1, userId=2, balance=1000.0]";
		
		assertEquals(testString, account.toString());
	}
}
