package com.techelevator.tenmo.model;

public class Accounts {
	
	private int accountId;
	private int userId;
	private double balance;
	
	public Accounts() {
			
		}

	public Accounts(int accountId, int userId, double balance) {
		super();
		this.accountId = accountId;
		this.userId = userId;
		this.balance = balance;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "Accounts [accountId=" + accountId + ", userId=" + userId + ", balance=" + balance + "]";
	}
	
	

}
