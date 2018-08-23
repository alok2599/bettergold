package in.bettergold.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "BALANCE")
public class Balance {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME", length = 100)
    @NotNull
    private String username;

    @Column(name = "INR", precision=25, scale=10)
    @NotNull
    private BigDecimal inr;
    
    @Column(name = "BTC")
    @NotNull
    private BigDecimal btc;
    
    @Column(name = "ETH")
    @NotNull
    private BigDecimal eth;
    
    @Column(name = "XRP")
    @NotNull
    private BigDecimal xrp;
 

    /*@Column(name = "BALANCE_ON_HOLD", length = 50)
    @NotNull
    private BigDecimal quantity;*/

    @Column(name = "LAST_MODIFIED")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date lastModifiedTimestamp;


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public BigDecimal getInr() {
		return inr;
	}


	public void setInr(BigDecimal inr) {
		this.inr = inr;
	}


	public BigDecimal getBtc() {
		return btc;
	}


	public void setBtc(BigDecimal btc) {
		this.btc = btc;
	}


	public BigDecimal getEth() {
		return eth;
	}


	public void setEth(BigDecimal eth) {
		this.eth = eth;
	}


	public BigDecimal getXrp() {
		return xrp;
	}


	public void setXrp(BigDecimal xrp) {
		this.xrp = xrp;
	}


	public Date getLastModifiedTimestamp() {
		return lastModifiedTimestamp;
	}


	public void setLastModifiedTimestamp(Date lastModifiedTimestamp) {
		this.lastModifiedTimestamp = lastModifiedTimestamp;
	}


    
	 
}