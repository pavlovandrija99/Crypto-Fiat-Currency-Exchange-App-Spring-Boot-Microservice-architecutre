package currencyConversionMicroservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/* Feign prvo trazi projekat ciji je naziv (name parametar) specificiran u okviru application.properties fajla.
 * Zatim trazi implementaciju metode getExchange, i onda zna sta treba da odradi.
 * I mapira tu metodu u getExchange metodu koja vraca CurrencyConversion. */

@FeignClient(name="currency-exchange")
public interface CurrencyExchangeProxy {
	
	 @GetMapping("/currency-exchange/from/{from}/to/{to}")
	 CurrencyConversion getExchange(@PathVariable String from, @PathVariable String to);
}
