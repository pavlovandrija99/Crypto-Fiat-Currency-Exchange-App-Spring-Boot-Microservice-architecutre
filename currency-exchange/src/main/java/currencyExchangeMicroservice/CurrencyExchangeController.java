package currencyExchangeMicroservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
public class CurrencyExchangeController {

	@Autowired
	private Environment environment;
	
	@Autowired
	private CurrencyExchangeRepository repo;
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	//@RateLimiter(name="Currency exchange rate limiter", fallbackMethod = "fallbackMethod")
	@Bulkhead(name="Currency exchange bulkhead")
	public CurrencyExchange getExchange(@PathVariable("from") String from, @PathVariable("to") String to) {
		
		String port = environment.getProperty("local.server.port");

		CurrencyExchange temp = repo.findByFromAndTo(from, to);
		
		if(temp == null) {
			throw new RuntimeException("Data not found!");
		}
		
		return new CurrencyExchange(temp.getId(), from, to, temp.getConversionMultiple(), port);
	}
	
	/*public String fallbackMethod(Exception ex) {
		return "Circuit breaker fallback method triggered! You can't send more than 2 requests within 60 seconds to this microservice";
	}*/
}
