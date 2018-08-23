package in.bettergold.engine.execution;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.stereotype.Component;

@Component
public class ExecutedOrderMessage {

	private String buyBookOrderId;
	
	private String sellBookOrderId;
	
	private String tradeId;

	private Long executionTimestamp;

	private BigDecimal executionPrice;

	private BigDecimal executionQuantity;

	public String getBuyBookOrderId() {
		return buyBookOrderId;
	}

	public void setBuyBookOrderId(String buyBookOrderId) {
		this.buyBookOrderId = buyBookOrderId;
	}

	public String getSellBookOrderId() {
		return sellBookOrderId;
	}

	public void setSellBookOrderId(String sellBookOrderId) {
		this.sellBookOrderId = sellBookOrderId;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public Long getExecutionTimestamp() {
		return executionTimestamp;
	}

	public void setExecutionTimestamp(Long executionTimestamp) {
		this.executionTimestamp = executionTimestamp;
	}

	public BigDecimal getExecutionPrice() {
		return executionPrice;
	}

	public void setExecutionPrice(BigDecimal executionPrice) {
		this.executionPrice = executionPrice;
	}

	public BigDecimal getExecutionQuantity() {
		return executionQuantity;
	}

	public void setExecutionQuantity(BigDecimal executionQuantity) {
		this.executionQuantity = executionQuantity;
	}

	  
	
}
