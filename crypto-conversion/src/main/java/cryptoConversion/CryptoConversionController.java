package cryptoConversion;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
public class CryptoConversionController {

	@Autowired
	private CryptoExchangeProxy cryptoExchangeProxy;
	
	@Autowired
	private CryptoWalletProxy cryptoWalletProxy;
	
	@GetMapping("/crypto-conversion/from/{from}/to/{to}/quantity/{quantity}/user/{email}")
	@RateLimiter(name="Crypto conversion rate limiter")
	public CryptoWallet getCryptoConversionFeign(@PathVariable String from, @PathVariable String to,
												 @PathVariable BigDecimal quantity, @PathVariable String email) 
	{
		
		CryptoExchange temp = cryptoExchangeProxy.getCryptoExchange(from, to);
			
		cryptoWalletProxy.getCryptoWallet(email);
			
		return cryptoWalletProxy.updateCryptoWallet(from, to, quantity, email, temp.getConversionMultiple());
	}
}
