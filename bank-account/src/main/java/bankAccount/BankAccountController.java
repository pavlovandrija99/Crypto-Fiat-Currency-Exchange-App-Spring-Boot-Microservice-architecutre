package bankAccount;

import java.math.BigDecimal;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankAccountController {

	@Autowired
	private Environment environment;
	
	@Autowired
	private BankAccountRepository bankAccountRepo;
	
	@GetMapping("/bank-account/user/{email}")
	public BankAccount getBankAccount(@PathVariable String email) { 

		String port = environment.getProperty("local.server.port");
		
		BankAccount bankAccount = bankAccountRepo.findByEmail(email);
		
		if(bankAccount == null) {
			throw new RuntimeException("Bank account for " + email + " not found!");
		}
		
		return new BankAccount(bankAccount.getId(), email, bankAccount.getRSD_amount(), bankAccount.getEUR_amount(),
							   bankAccount.getCHF_amount(), bankAccount.getGBP_amount(), bankAccount.getUSD_amount(),
							   port);
	}
	
	@PutMapping("/bank-account/from/{from}/to/{to}/quantity/{quantity}/user/{email}/conversion-multiple/{conversionMultiple}")
	public BankAccount updateBankAccount(@PathVariable String from, @PathVariable String to,
										 @PathVariable BigDecimal quantity, @PathVariable String email,
										 @PathVariable BigDecimal conversionMultiple) 
	{
		String port = environment.getProperty("local.server.port");
		
		BankAccount bankAccountToUpdate = bankAccountRepo.findByEmail(email);
		
		if(bankAccountToUpdate == null) {
			throw new RuntimeException("Bank account to update for " + email + " user not found!");
		} 
		
		if (checkForUserAssets(from, bankAccountToUpdate, quantity)) {
			reduceTheAccountBalance(from, quantity, bankAccountToUpdate);
			increaseTheAccountBalance(to, quantity.multiply(conversionMultiple), bankAccountToUpdate);
		}
		
		bankAccountRepo.save(bankAccountToUpdate);
		return new BankAccount(bankAccountToUpdate.getId(), email, bankAccountToUpdate.getRSD_amount(),
							   bankAccountToUpdate.getEUR_amount(), bankAccountToUpdate.getCHF_amount(),
							   bankAccountToUpdate.getGBP_amount(), bankAccountToUpdate.getUSD_amount(), port);
	}
	
	private void reduceTheAccountBalance(String from, BigDecimal quantity, BankAccount bankAccountToUpdate) {
		switch(from) {
		case "RSD":
			bankAccountToUpdate.setRSD_amount(bankAccountToUpdate.getRSD_amount().subtract(quantity));
			break;
		case "EUR":
			bankAccountToUpdate.setEUR_amount(bankAccountToUpdate.getEUR_amount().subtract(quantity));
			break;
		case "USD":
			bankAccountToUpdate.setUSD_amount(bankAccountToUpdate.getUSD_amount().subtract(quantity));
			break;
		case "GBP":
			bankAccountToUpdate.setGBP_amount(bankAccountToUpdate.getGBP_amount().subtract(quantity));
			break;
		default: 
			bankAccountToUpdate.setCHF_amount(bankAccountToUpdate.getCHF_amount().subtract(quantity));
		}
	}
	
	private void increaseTheAccountBalance(String to, BigDecimal totalAmount, BankAccount bankAccountToUpdate) {
		switch(to) {
		case "RSD":
			bankAccountToUpdate.setRSD_amount(bankAccountToUpdate.getRSD_amount().add(totalAmount));
			break;
		case "EUR":
			bankAccountToUpdate.setEUR_amount(bankAccountToUpdate.getEUR_amount().add(totalAmount));
			break;
		case "USD":
			bankAccountToUpdate.setUSD_amount(bankAccountToUpdate.getUSD_amount().add(totalAmount));
			break;
		case "GBP":
			bankAccountToUpdate.setGBP_amount(bankAccountToUpdate.getGBP_amount().add(totalAmount));
			break;
		default:
			bankAccountToUpdate.setCHF_amount(bankAccountToUpdate.getCHF_amount().add(totalAmount));
		}
	}
	
	private boolean checkForUserAssets(String from, BankAccount bankAccountToUpdate, BigDecimal quantity) {
		switch(from) {
		case "RSD":
			if (bankAccountToUpdate.getRSD_amount().compareTo(quantity) > -1) {
				return true;
			}
			throw new RuntimeErrorException(null, "There is not enough RSD assets on bank account!");
		case "USD":
			if (bankAccountToUpdate.getUSD_amount().compareTo(quantity) > -1) {
				return true;
			}
			throw new RuntimeErrorException(null, "There is not enough USD assets on bank account!");
		case "EUR":
			if (bankAccountToUpdate.getEUR_amount().compareTo(quantity) > -1) {
				return true;
			}
			throw new RuntimeErrorException(null, "There is not enough EUR assets on bank account!");
		case "CHF":
			if (bankAccountToUpdate.getCHF_amount().compareTo(quantity) > -1) {
				return true;
			}
			throw new RuntimeErrorException(null, "There is not enough CHF assets on bank account!");
		case "GBP":
			if (bankAccountToUpdate.getGBP_amount().compareTo(quantity) > -1) {
				return true;
			}
			throw new RuntimeErrorException(null, "There is not enough GBP assets on bank account!");
		default: 
			throw new RuntimeErrorException(null, "Invalid currency parameter!");
		}	
	}
}
