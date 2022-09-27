package currencyConversionMicroservice;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@FeignClient(name="bank-account")
public interface BankAccountProxy {
	
	@PutMapping("/bank-account/from/{from}/to/{to}/quantity/{quantity}/user/{email}/conversion-multiple/{conversionMultiple}")
	BankAccount updateBankAccount(@PathVariable String from, @PathVariable String to,
			 @PathVariable BigDecimal quantity, @PathVariable String email,
			 @PathVariable BigDecimal conversionMultiple);

	@GetMapping("/bank-account/user/{email}")
	BankAccount getBankAccount(@PathVariable String email);
}
