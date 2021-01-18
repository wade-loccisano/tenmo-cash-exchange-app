package com.techelevator.tenmo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.TransfersDAO;
import com.techelevator.tenmo.model.Transfers;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransfersController {
	
	private TransfersDAO transfersDao;
	
	public TransfersController(TransfersDAO transfersDao) {
		this.transfersDao = transfersDao;
	}
	
	@RequestMapping(path = "transfers", method = RequestMethod.GET)
	public List<Transfers> listTransfers(Principal principal){
		return transfersDao.listAll(principal.getName());
	}
	
	@RequestMapping(path = "transfers/send", method = RequestMethod.POST)
	public void sendTransfer(@RequestBody Transfers transfer, Principal principal) {
		transfersDao.sendTransfer(transfer, principal.getName());
	}
	
	@RequestMapping(path = "transfers/request", method = RequestMethod.POST)
	public void requestTransfer(@RequestBody Transfers transfer, Principal principal) {
		transfersDao.requestTransfer(transfer, principal.getName());
	}
	
	@RequestMapping(path = "transfers/pending", method = RequestMethod.GET)
	public List<Transfers> getPendingTranfers(Principal principal){
		return transfersDao.listPending(principal.getName());
	}
	
	@RequestMapping(path = "transfers/{id}", method = RequestMethod.GET)
	public Transfers getTransferById(@PathVariable int id) {
		return transfersDao.getTransferById(id);
	}

}
