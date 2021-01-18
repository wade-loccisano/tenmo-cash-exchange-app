package com.techelevator.tenmo.models;

import static org.junit.Assert.*;

import org.junit.Test;

public class TransfersTest {

	@Test
	public void transfersGetters_should_return_correct_thing() {
		Transfers transfer = new Transfers(1, 2, 3, 4, 5, 56.0);
		assertEquals(1, transfer.getTransferId());
		assertEquals(2, transfer.getTransferTypeId());
		assertEquals(3, transfer.getTransferStatusId());
		assertEquals(4, transfer.getAccountFrom());
		assertEquals(5, transfer.getAccountTo());
		assertEquals(56.0, transfer.getAmount(), .01);
	}
		
	@Test
	public void toString_should_return_correct_string() {
		Transfers transfer = new Transfers(1, 2, 3, 4, 5, 56.0);
		String testString = "Transfers [transferId=1, transferTypeId=2, transferStatusId=3, accountFrom=4,"
				+ " accountTo=5, amount=56.0]";
		assertEquals(testString, transfer.toString());
	}


}
