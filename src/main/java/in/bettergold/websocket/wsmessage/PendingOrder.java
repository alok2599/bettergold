package in.bettergold.websocket.wsmessage;

import java.math.BigDecimal;
import java.util.Date;

import in.bettergold.model.OrderSide;
import in.bettergold.model.OrderStatus;
import in.bettergold.model.Symbol;

public class PendingOrder {

	private Symbol symbol;
	private OrderSide side;
	private BigDecimal orderQuantity;
	private BigDecimal execQuantity;
	private OrderStatus status;
	private Date timestamp;
	private String orderId;
	
	public Symbol getSymbol() {
		return symbol;
	}
	
	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}
	public OrderSide getSide() {
		return side;
	}
	public void setSide(OrderSide side) {
		this.side = side;
	}
	public BigDecimal getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(BigDecimal orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public BigDecimal getExecQuantity() {
		return execQuantity;
	}
	public void setExecQuantity(BigDecimal execQuantity) {
		this.execQuantity = execQuantity;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	 
	 
	
}
