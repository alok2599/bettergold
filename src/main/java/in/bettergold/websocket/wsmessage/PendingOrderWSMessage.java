package in.bettergold.websocket.wsmessage;

import java.util.List;

public class PendingOrderWSMessage {

	List<PendingOrder> pendingOrder;
	String event = "pendingOrderUdpate";
	
	public PendingOrderWSMessage() {
		 
	}
	public PendingOrderWSMessage(List<PendingOrder> po) {
		this.pendingOrder = po;
	}
	
	public List<PendingOrder> getPendingOrder() {
		return pendingOrder;
	}
	public void setPendingOrder(List<PendingOrder> pendingOrder) {
		this.pendingOrder = pendingOrder;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	
	
}
