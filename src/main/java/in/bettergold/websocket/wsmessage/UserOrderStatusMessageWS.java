package in.bettergold.websocket.wsmessage;

import java.util.List;

public class UserOrderStatusMessageWS {

	PendingOrderWSMessage pendingOrderWSMessage;
	String channel;
	
	public UserOrderStatusMessageWS() {
		
	}
	public UserOrderStatusMessageWS(PendingOrderWSMessage pendingOrderWSMessage, String channel) {
		super();
		this.pendingOrderWSMessage = pendingOrderWSMessage;
		this.channel = channel;
	}
	public PendingOrderWSMessage getPendingOrderWSMessage() {
		return pendingOrderWSMessage;
	}
	public void setPendingOrderWSMessage(PendingOrderWSMessage pendingOrderWSMessage) {
		this.pendingOrderWSMessage = pendingOrderWSMessage;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
 
	

	 
	
	
	
}
