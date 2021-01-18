package com.techelevator.tenmo;


import java.util.Scanner;

import com.techelevator.tenmo.models.Accounts;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfers;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.view.ConsoleService;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountService accountService;
    private TransferService transferService;
    private UserService userService;

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new AccountService(API_BASE_URL),
    			new TransferService(API_BASE_URL), new UserService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, AccountService accountService,
    		TransferService transferService, UserService userService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;
		this.transferService = transferService;
		this.userService = userService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		Accounts account = accountService.getAccountBalance(currentUser.getToken());
		System.out.println("-----------------------------------");
		System.out.printf("%s %.2f\n", "Your current account balance is: ", account.getBalance());
		System.out.println("-----------------------------------");
	}

	private void viewTransferHistory() {
		Transfers[] transferList = transferService.getAllTransfers(currentUser.getToken());
		System.out.println("-----------------------------------------");
		System.out.printf("%5s %12s %14s\n", "Transfer ID", "From/To", "Amount");
		System.out.println("-----------------------------------------");
		for(Transfers transfer : transferList) {
			if (transfer.getAccountToName().equals(currentUser.getUser().getUsername())) {
				System.out.printf("%-15d %-15s %1s %1.2f\n", transfer.getTransferId(), "From: " + transfer.getAccountFromName(), "$", transfer.getAmount());
			} else {
				System.out.printf("%-15d %-15s %1s %1.2f\n", transfer.getTransferId(), "To: " + transfer.getAccountToName(), "$", transfer.getAmount());
			}
		}
		System.out.println("---------------------------------------");
		
		Scanner input = new Scanner(System.in);
		System.out.println();
		System.out.println("Please enter a transfer Id to view details: (0 to cancel)");
		String userInput = input.nextLine();
		int transferId = Integer.parseInt(userInput);
		System.out.println("------------------");
		System.out.println("Transfer Details");
		System.out.println("------------------");
		if (transferId != 0) {
			Transfers transfer = transferService.getTransferById(transferId, currentUser.getToken());
			
			System.out.println("ID: " + transfer.getTransferId());
			System.out.println("From: " + transfer.getAccountFromName());
			System.out.println("To: " + transfer.getAccountToName());
			System.out.println("Type: " + transfer.getTransferType());
			System.out.println("Status: " + transfer.getTransferStatus());
			System.out.printf("%s %.2f\n", "Amount: $", transfer.getAmount());
			System.out.println("------------------");
		}
	}

	private void viewPendingRequests() {
		Transfers[] transfersArr = transferService.getPendingTransfers(currentUser.getToken());
		System.out.println("---------------------------------------------");
		System.out.printf("%5s %16s %14s\n", "Transfer ID", "Transfer To", "Amount");
		System.out.println("---------------------------------------------");
		for(Transfers transfer : transfersArr) {
			if (transfer.getAccountFromName().equals(currentUser.getUser().getUsername())) {
				System.out.printf("%-18d %-15s %1s %1.2f\n", transfer.getTransferId(), transfer.getAccountToName(), "$", transfer.getAmount());
			}
		}
		System.out.println("---------------------------------------");
		
	}

	private void sendBucks() {
		Scanner input = new Scanner(System.in);
		User[] users = userService.listUsers(currentUser.getToken());
		System.out.println("---------------------------------------");
		System.out.println("Users ID                     Name");
		System.out.println("---------------------------------------");
		for (User u : users) {
			System.out.printf("%-28d %-1s\n", u.getId(), u.getUsername());
		}
		System.out.println("---------------------------------------");
		System.out.println("Select the ID of the user you are sending to: (0 to cancel)");
		String inputUserId = input.nextLine();
		if (!inputUserId.equals("0")) {
			System.out.println("Enter amount to send: ");
			double inputAmount = input.nextDouble();
			
			
			Accounts account = accountService.getAccountBalance(currentUser.getToken());
			Accounts accountTo = accountService.getAccountBalanceById(Integer.parseInt(inputUserId), currentUser.getToken());
			accountTo.setAccountId(Integer.parseInt(inputUserId));
			if (account.getBalance() >= inputAmount) {
				accountTo.setBalance(accountTo.getBalance() + inputAmount);
				account.setBalance(account.getBalance() - inputAmount);
				accountService.updateBalance(accountTo, currentUser.getToken());
				accountService.updateBalance(account, currentUser.getToken());
				
				Transfers transfer = new Transfers();
				transfer.setTransferStatusId(2);
				transfer.setTransferTypeId(2);
				transfer.setAccountFrom(currentUser.getUser().getId());
				transfer.setAccountTo(Integer.parseInt(inputUserId));
				transfer.setAmount(inputAmount);
				
				transferService.addSend(currentUser.getToken(), transfer);
			} else {
				System.out.println("Invalid amount.");
			}
		}
		
	}

	private void requestBucks() {
		int accountTo = currentUser.getUser().getId();
		Scanner input = new Scanner(System.in);
		User[] users = userService.listUsers(currentUser.getToken());
		System.out.println("---------------------------------------");
		System.out.println("Users ID                     Name");
		System.out.println("---------------------------------------");
		for (User u : users) {
			System.out.printf("%-28d %-1s\n", u.getId(), u.getUsername());
		}
		System.out.println("---------------------------------------");
		System.out.println("Select the ID of the user you are requesting from: (0 to cancel)");
		String accountFromString = input.nextLine();
		
		if(!accountFromString.equals("0")) {
			int accountFrom = Integer.parseInt(accountFromString);
			System.out.println("Enter amount to request: ");
			double inputAmount = input.nextDouble();
			
			Transfers transfer = new Transfers();
			transfer.setTransferStatusId(1);
			transfer.setTransferTypeId(1);
			transfer.setAccountFrom(accountFrom);
			transfer.setAccountTo(accountTo);
			transfer.setAmount(inputAmount);
			
			transferService.addRequest(currentUser.getToken(), transfer);
			
			System.out.println("Request has been added!");
		}
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
