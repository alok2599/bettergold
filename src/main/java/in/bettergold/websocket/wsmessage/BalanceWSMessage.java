package in.bettergold.websocket.wsmessage;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

public class BalanceWSMessage {
	
	
	String event = "balanceUpdate";
    private BigDecimal inr;
    private BigDecimal btc;
    private BigDecimal eth;
    private BigDecimal xrp;
    
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
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
	
	
	
}
