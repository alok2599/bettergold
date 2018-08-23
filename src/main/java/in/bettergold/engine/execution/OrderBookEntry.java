package in.bettergold.engine.execution;

import java.io.Serializable;
import java.math.BigDecimal;

import in.bettergold.model.OrderSide;
import in.bettergold.model.Symbol;
 
public class OrderBookEntry implements Comparable<OrderBookEntry>, Serializable  {

	private static final long serialVersionUID = -295422703255886286L;
	private String orderId;

	private Long orderTimestamp;

	private BigDecimal price;

	private BigDecimal quantity;

	private OrderSide side;

	private Symbol symbol;
	
	private boolean cancelRequested;
	
	public boolean isCancelRequested() {
		return cancelRequested;
	}

	public void setCancelRequested(boolean cancelRequested) {
		this.cancelRequested = cancelRequested;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getOrderTimestamp() {
		return orderTimestamp;
	}

	public void setOrderTimestamp(Long orderTimestamp) {
		this.orderTimestamp = orderTimestamp;
	}

	public BigDecimal getLimitPrice() {
		return price;
	}

	public void setLimitPrice(BigDecimal limitPrice) {
		this.price = limitPrice;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public void setSide(OrderSide side) {
		this.side = side;
	}
	
	public OrderSide getSide() {
		return side;
	}


	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	@Override
	public int compareTo(OrderBookEntry o) {
		 
		//if( this.getOrderTimestamp().isBefore(o.getOrderTimestamp()))
		if( this.getOrderTimestamp() < (o.getOrderTimestamp()))
			return -1;
		else //if (order1.getOrderTimestamp().isBefore(order2.getOrderTimestamp()))
			return 1;
	}
	
	@Override
    public boolean equals(Object o) {
 
        if (o == this) {
            return true;
        }
 
        if (o == null) {
            return false;
        }
        
        if (!(o instanceof OrderBookEntry)) {
            return false;
        }
        
        return  this.getOrderId().equals( ((OrderBookEntry) o).getOrderId() );
    }
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((orderId == null) ? 0 : orderId.hashCode());
        return result;
    }
}
