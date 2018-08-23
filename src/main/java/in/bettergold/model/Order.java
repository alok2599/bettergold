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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import in.bettergold.model.Symbol;

@Entity
@Table(name = "ORDER1")
@NamedQueries({ @NamedQuery(name = "findAllPendingOrdersByUsername", query = "select o from Order o where o.status = 'OPEN' OR o.status = 'PARTIALLY_FILLED' AND o.username= :username") })
public class Order {

	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "ORDER_ID", length = 50)
	@NotNull
	private String orderId;

	@Column(name = "USERNAME", length = 100)
	@NotNull
	private String username;

	@Column(name = "SYMBOL", length = 6)
	@Enumerated(EnumType.STRING)
	@NotNull
	private Symbol symbol;

	@Column(name = "PRICE", length = 50)
	@NotNull
	private BigDecimal  limitPrice;

	@Column(name = "QUANTITY", length = 50)
	@NotNull
	private BigDecimal quantity;
	
	@Column(name = "EXECUTED_QUANTITY", length = 50)
	@NotNull
	private BigDecimal executedQuantity;

	@Column(name = "SIDE", length = 4)
	@Enumerated(EnumType.STRING)
	@NotNull
	private OrderSide side;

	@Column(name = "STATUS", length = 50)
	@Enumerated(EnumType.STRING)
	@NotNull
	private OrderStatus status;
	
	@Column(name = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date orderTimestamp;

	@Column(name = "LAST_MODIFIED")
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date lastModifiedTimestamp;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public BigDecimal getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(BigDecimal limitPrice) {
		this.limitPrice = limitPrice;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	public BigDecimal getExecutedQuantity() {
		return executedQuantity;
	}

	public void setExecutedQuantity(BigDecimal executedQuantity) {
		this.executedQuantity = executedQuantity;
	}


	public OrderSide getSide() {
		return side;
	}

	public void setSide(OrderSide side) {
		this.side = side;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Date getOrderTimestamp() {
		return orderTimestamp;
	}

	public void setOrderTimestamp(Date orderTimestamp) {
		this.orderTimestamp = orderTimestamp;
	}

	public Date getLastModifiedTimestamp() {
		return lastModifiedTimestamp;
	}

	public void setLastModifiedTimestamp(Date lastModifiedTimestamp) {
		this.lastModifiedTimestamp = lastModifiedTimestamp;
	}

	
}