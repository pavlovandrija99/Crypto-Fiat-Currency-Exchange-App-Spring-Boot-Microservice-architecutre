package currencyExchangeMicroservice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;


@RestController
public class CurcuitBreakerController {
	
	@GetMapping("/test")
	//@RateLimiter(name="Currency exchange rate limiter")
	//@Bulkhead(name="Currency exchange bulkhead")
	public String sampleApi() {
		return "You can call this only 2 times in 40 seconds";
	}
}
