package cryptoWallet;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class CryptoWallet {

	@Id
	private long id;
	
	@Column(unique=true)
	private String email;

	@Column
	private BigDecimal BTC_amount;
	
	@Column
	private BigDecimal ETH_amount;
	
	@Column
	private BigDecimal LTC_amount;
	
	@Column
	private BigDecimal XRP_amount;
	
	@Transient
	private String environment;

	public CryptoWallet() {
		
	}

	public CryptoWallet(long id, String email, BigDecimal bTC_amount, BigDecimal eTH_amount, BigDecimal lTC_amount,
			BigDecimal xRP_amount, String environment) {
		super();
		this.id = id;
		this.email = email;
		BTC_amount = bTC_amount;
		ETH_amount = eTH_amount;
		LTC_amount = lTC_amount;
		XRP_amount = xRP_amount;
		this.environment = environment;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BigDecimal getBTC_amount() {
		return BTC_amount;
	}

	public void setBTC_amount(BigDecimal bTC_amount) {
		BTC_amount = bTC_amount;
	}

	public BigDecimal getETH_amount() {
		return ETH_amount;
	}

	public void setETH_amount(BigDecimal eTH_amount) {
		ETH_amount = eTH_amount;
	}

	public BigDecimal getLTC_amount() {
		return LTC_amount;
	}

	public void setLTC_amount(BigDecimal lTC_amount) {
		LTC_amount = lTC_amount;
	}

	public BigDecimal getXRP_amount() {
		return XRP_amount;
	}

	public void setXRP_amount(BigDecimal xRP_amount) {
		XRP_amount = xRP_amount;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}
}
