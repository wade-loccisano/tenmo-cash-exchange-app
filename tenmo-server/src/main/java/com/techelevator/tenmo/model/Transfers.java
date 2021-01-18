package com.techelevator.tenmo.model;

public class Transfers {
	
	private int transferId;
	private int transferTypeId;
	private String transferType;
	private int transferStatusId;
	private String transferStatus;
	private int accountFrom;
	private String accountFromName;
	private int accountTo;
	private String accountToName;
	private double amount;
	private String activeUsername;
	private String foreignUsername;
	
	public Transfers() {
		
	
	}
	
	
	public Transfers(int transferId, int transferTypeId, int transferStatusId, int accountFrom, int accountTo,
			double amount) {
		super();
		this.transferId = transferId;
		this.transferTypeId = transferTypeId;
		this.transferStatusId = transferStatusId;
		this.accountFrom = accountFrom;
		this.accountTo = accountTo;
		this.amount = amount;
	}
	
	

	public String getAccountFromName() {
		return accountFromName;
	}


	public void setAccountFromName(String accountFromName) {
		this.accountFromName = accountFromName;
	}


	public String getAccountToName() {
		return accountToName;
	}


	public void setAccountToName(String accountToName) {
		this.accountToName = accountToName;
	}


	public String getTransferType() {
		return transferType;
	}


	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}


	public String getTransferStatus() {
		return transferStatus;
	}


	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}


	public String getActiveUsername() {
		return activeUsername;
	}


	public void setActiveUsername(String username) {
		this.activeUsername = username;
	}


	public int getTransferId() {
		return transferId;
	}


	public void setTransferId(int transferId) {
		this.transferId = transferId;
	}


	public int getTransferTypeId() {
		return transferTypeId;
	}


	public void setTransferTypeId(int transferTypeId) {
		this.transferTypeId = transferTypeId;
	}


	public int getTransferStatusId() {
		return transferStatusId;
	}


	public void setTransferStatusId(int transferStatusId) {
		this.transferStatusId = transferStatusId;
	}


	public int getAccountFrom() {
		return accountFrom;
	}


	public void setAccountFrom(int accountFrom) {
		this.accountFrom = accountFrom;
	}


	public int getAccountTo() {
		return accountTo;
	}


	public void setAccountTo(int accountTo) {
		this.accountTo = accountTo;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	
	
	public String getForeignUsername() {
		return foreignUsername;
	}


	public void setForeignUsername(String userNameFrom) {
		this.foreignUsername = userNameFrom;
	}


	@Override
	public String toString() {
		return "Transfers [transferId=" + transferId + ", transferTypeId=" + transferTypeId + ", transferStatusId="
				+ transferStatusId + ", accountFrom=" + accountFrom + ", accountTo=" + accountTo + ", amount=" + amount
				+ "]";
	}	

}
