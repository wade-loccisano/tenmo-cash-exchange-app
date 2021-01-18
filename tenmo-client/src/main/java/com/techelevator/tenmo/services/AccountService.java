package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Accounts;
import com.techelevator.tenmo.models.Transfers;

public class AccountService {

	private final String BASE_URL;
	private RestTemplate restTemplate;
	
	public AccountService(String url) {
		BASE_URL = url;
		this.restTemplate = new RestTemplate();
	}
	
	public Accounts getAccountBalance(String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(authToken);
		HttpEntity<Accounts> entity = new HttpEntity<>(headers);
		ResponseEntity<Accounts> response = null;
		try {
		response = restTemplate.exchange(BASE_URL + "balance", HttpMethod.GET,
				entity, Accounts.class);
		} catch (RestClientResponseException ex) {
			System.out.println("Whoops! " + ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return response.getBody();
	}
	
	public Accounts updateBalance(Accounts account, String authToken) {
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(authToken);
	    HttpEntity<Accounts> entity = new HttpEntity<>(account, headers);
	    try {
	    restTemplate.put(BASE_URL + "users/" + account.getAccountId(), entity);
	    } catch (RestClientResponseException ex) {
			System.out.println("Whoops! " + ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
	    return account;
	}
	
	public Accounts getAccountBalanceById(int id, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(authToken);
		HttpEntity<Accounts> entity = new HttpEntity<>(headers);
		ResponseEntity<Accounts> response = null;
		try {
		response = restTemplate.exchange(BASE_URL + "users/" + id, HttpMethod.GET,
				entity, Accounts.class);
		} catch (RestClientResponseException ex) {
			System.out.println("Whoops! " + ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return response.getBody();
	}
	

}
