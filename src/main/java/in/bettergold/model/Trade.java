package in.bettergold.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TRADE")
public class Trade {

	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "BUY_ORDER_ID", length = 50)
	@NotNull
	private String buyOrderId;
	
	@Column(name = "SELL_ORDER_ID", length = 50)
	@NotNull
	private String sellOrderId;

	@Column(name = "TRADE_ID", length = 100)
	@NotNull
	private String tradeId;

	@Column(name = "SYMBOL")
	@NotNull
	@Enumerated(EnumType.STRING)
	private Symbol symbol;
	
	@Column(name = "EXECUTION_PRICE", length = 50)
	@NotNull
	private BigDecimal executionPrice;

	@Column(name = "QUANTITY", length = 50)
	@NotNull
	private BigDecimal quantity;

	@Column(name = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date tradeTimestamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBuyOrderId() {
		return buyOrderId;
	}

	public void setBuyOrderId(String buyOrderId) {
		this.buyOrderId = buyOrderId;
	}

	public String getSellOrderId() {
		return sellOrderId;
	}

	public void setSellOrderId(String sellOrderId) {
		this.sellOrderId = sellOrderId;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public BigDecimal getExecutionPrice() {
		return executionPrice;
	}

	public void setExecutionPrice(BigDecimal executionPrice) {
		this.executionPrice = executionPrice;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public Date getTradeTimestamp() {
		return tradeTimestamp;
	}

	public void setTradeTimestamp(Date tradeTimestamp) {
		this.tradeTimestamp = tradeTimestamp;
	}

	
 
	 
	 

    
	
	
	

}