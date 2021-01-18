package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Transfers;



public interface TransfersDAO {

	List<Transfers> listAll(String username);
	
	List<Transfers> listPending(String username);

	void sendTransfer(Transfers transfer, String username);
	
	void requestTransfer(Transfers transfer, String username);
	
	Transfers getTransferById(int id);
	
}
