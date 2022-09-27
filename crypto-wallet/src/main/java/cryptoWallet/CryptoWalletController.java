package cryptoWallet;

import java.math.BigDecimal;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CryptoWalletController {

	@Autowired
	private Environment environment;
	
	@Autowired
	private CryptoWalletRepository cryptoWalletRepository;
	
	@GetMapping("/crypto-wallet/user/{email}")
	public CryptoWallet getCryptoWallet(@PathVariable String email) {
		
		String port = environment.getProperty("local.server.port");
		
		CryptoWallet cryptoWallet = cryptoWalletRepository.findByEmail(email);
		
		if(cryptoWallet == null) {
			throw new RuntimeException("Crypto wallet for " + email + " not found!");
		}
		
		return new CryptoWallet(cryptoWallet.getId(), email, cryptoWallet.getBTC_amount(), cryptoWallet.getETH_amount(),
								cryptoWallet.getLTC_amount(), cryptoWallet.getXRP_amount(), port);
	}
	
	
	@PutMapping("/crypto-wallet/from/{from}/to/{to}/quantity/{quantity}/user/{email}/conversion-multiple/{conversionMultiple}")
	public CryptoWallet updateCryptoWallet(@PathVariable String from, @PathVariable String to,
										 @PathVariable BigDecimal quantity, @PathVariable String email,
										 @PathVariable BigDecimal conversionMultiple)
	{
		String port = environment.getProperty("local.server.port");
		
		CryptoWallet cryptoWalletToUpdate = cryptoWalletRepository.findByEmail(email);
		
		
		if(cryptoWalletToUpdate == null) {
			throw new RuntimeException("Crypto wallet to update for " + email + " user not found!");
		}
		
		System.out.println("Crypto wallet to update: " + cryptoWalletToUpdate.getId() + " " + cryptoWalletToUpdate.getEmail());
		
		if (checkForUserAssets(from, cryptoWalletToUpdate, quantity)) {
			reduceTheAccountBalance(from, quantity, cryptoWalletToUpdate);
			increaseTheAccountBalance(to, quantity.multiply(conversionMultiple), cryptoWalletToUpdate);
		}
		
		cryptoWalletRepository.save(cryptoWalletToUpdate);
		return new CryptoWallet(cryptoWalletToUpdate.getId(), email, cryptoWalletToUpdate.getBTC_amount(),
								cryptoWalletToUpdate.getETH_amount(), cryptoWalletToUpdate.getLTC_amount(),
								cryptoWalletToUpdate.getXRP_amount(), port);
	}
	
	private void increaseTheAccountBalance(String to, BigDecimal totalAmount, CryptoWallet cryptoWalletToUpdate) {
		switch(to) {
		case "BTC":
			cryptoWalletToUpdate.setBTC_amount(cryptoWalletToUpdate.getBTC_amount().add(totalAmount));
			break;
		case "ETH":
			cryptoWalletToUpdate.setETH_amount(cryptoWalletToUpdate.getETH_amount().add(totalAmount));
			break;
		case "LTC":
			cryptoWalletToUpdate.setLTC_amount(cryptoWalletToUpdate.getLTC_amount().add(totalAmount));
			break;
		default:
			cryptoWalletToUpdate.setXRP_amount(cryptoWalletToUpdate.getXRP_amount().add(totalAmount));
		}
	}
	
	private void reduceTheAccountBalance(String from, BigDecimal quantity, CryptoWallet cryptoWalletToUpdate) {
		switch(from) {
		case "BTC":
			cryptoWalletToUpdate.setBTC_amount(cryptoWalletToUpdate.getBTC_amount().subtract(quantity));
			break;
		case "ETH":
			cryptoWalletToUpdate.setETH_amount(cryptoWalletToUpdate.getETH_amount().subtract(quantity));
			break;
		case "LTC":
			cryptoWalletToUpdate.setLTC_amount(cryptoWalletToUpdate.getLTC_amount().subtract(quantity));
			break;
		default:
			cryptoWalletToUpdate.setXRP_amount(cryptoWalletToUpdate.getXRP_amount().subtract(quantity));
		}
	}
	
	private boolean checkForUserAssets(String from, CryptoWallet cryptoWalletToUpdate, BigDecimal quantity) {
		switch(from) {
		case "BTC":
			if(cryptoWalletToUpdate.getBTC_amount().compareTo(quantity) > -1) {
				return true;
			}
			throw new RuntimeErrorException(null, "There is not enough BTH assets on this crypyo wallet!");
		case "ETH":
			if(cryptoWalletToUpdate.getETH_amount().compareTo(quantity) > -1) {
				return true;
			}
			throw new RuntimeErrorException(null, "There is not enough ETH assets on this crypyo wallet!");
		case "LTC":
			if(cryptoWalletToUpdate.getLTC_amount().compareTo(quantity) > -1) {
				return true;
			}
			throw new RuntimeErrorException(null, "There is not enough LTC assets on this crypyo wallet!");
		case "XRP":
			if(cryptoWalletToUpdate.getXRP_amount().compareTo(quantity) > -1) {
				return true;
			}
			throw new RuntimeErrorException(null, "There is not enough XRP assets on this crypyo wallet!");
		default:
			throw new RuntimeErrorException(null, "Invalid crypto currency parameter!");
		}
	}
}
