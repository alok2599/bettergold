package in.bettergold.engine.execution;

import java.math.BigDecimal;

public class CancelledOrderMessage {

    private String orderId;
	
	private BigDecimal cancelledQuantity;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getCancelledQuantity() {
		return cancelledQuantity;
	}

	public void setCancelledQuantity(BigDecimal cancelledQuantity) {
		this.cancelledQuantity = cancelledQuantity;
	}

	 
	
	
}
