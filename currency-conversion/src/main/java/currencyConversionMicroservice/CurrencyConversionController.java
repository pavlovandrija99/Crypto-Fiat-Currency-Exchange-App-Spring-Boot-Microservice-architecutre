	package currencyConversionMicroservice;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
public class CurrencyConversionController {
	
	@Autowired
	private CurrencyExchangeProxy currencyExchangeProxy;
	
	@Autowired
	private BankAccountProxy bankAccountProxy;
	
	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}/user/{email}")
	@RateLimiter(name="Currency conversion rate limiter")
	public BankAccount getConversionFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity, @PathVariable String email) {
		
		CurrencyConversion temp = currencyExchangeProxy.getExchange(from, to);
	
		bankAccountProxy.getBankAccount(email);
		
		return bankAccountProxy.updateBankAccount(from, to, quantity, email, temp.getConversionMultiple());
	}
}
