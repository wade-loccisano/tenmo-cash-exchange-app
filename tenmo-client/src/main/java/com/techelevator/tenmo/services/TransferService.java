package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Transfers;

public class TransferService {

	private String BASE_URL;
	private RestTemplate restTemplate;
	
	public TransferService(String url) {
		this.BASE_URL = url;
		this.restTemplate = new RestTemplate();
	}
	
	public Transfers[] getAllTransfers(String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(authToken);
		HttpEntity<Transfers> entity = new HttpEntity<>(headers);
		ResponseEntity <Transfers[]> response = null;
		try {
		response = restTemplate.exchange(BASE_URL + "transfers", HttpMethod.GET, 
				entity, Transfers[].class);
		} catch (RestClientResponseException ex) {
			System.out.println("Whoops! " + ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return response.getBody();
	}
	
	public Transfers[] getPendingTransfers(String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(authToken);
		HttpEntity<Transfers> entity = new HttpEntity<>(headers);
		ResponseEntity <Transfers[]> response = null;
		try {
		response = restTemplate.exchange(BASE_URL + "transfers/pending", HttpMethod.GET, 
				entity, Transfers[].class);
		} catch (RestClientResponseException ex) {
			System.out.println("Whoops! " + ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return response.getBody();
	}
	
	public Transfers addRequest(String authToken, Transfers transfer) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(authToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Transfers> entity = new HttpEntity<>(transfer, headers);
		try {
		transfer = restTemplate.postForObject(BASE_URL + "transfers/request", entity, Transfers.class);
		} catch (RestClientResponseException ex) {
			System.out.println("Whoops! " + ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return transfer;
	}
	
	public Transfers addSend(String authToken, Transfers transfer) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(authToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Transfers> entity = new HttpEntity<>(transfer, headers);
		try {
		transfer = restTemplate.postForObject(BASE_URL + "transfers/send", entity, Transfers.class);
		} catch (RestClientResponseException ex) {
			System.out.println("Whoops! " + ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return transfer;
	}
	
	public Transfers getTransferById(int id, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(authToken);
		HttpEntity<Transfers> entity = new HttpEntity<>(headers);
		ResponseEntity<Transfers> response = null;
		try {
		response = restTemplate.exchange(BASE_URL + "transfers/" + id, HttpMethod.GET,
				entity, Transfers.class);
		} catch (RestClientResponseException ex) {
			System.out.println("Whoops! " + ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return response.getBody();
	}
}
