package in.bettergold.websocket.wsmessage;

import in.bettergold.model.Balance;

public class UserBalanceStatusMessageWS {

	BalanceWSMessage balance;
	String channel;
	
	public UserBalanceStatusMessageWS() {
		
	}
	public UserBalanceStatusMessageWS(BalanceWSMessage balance, String channel) {
		 
		this.balance = balance;
		this.channel = channel;
	}
	public BalanceWSMessage getBalance() {
		return balance;
	}
	public void setBalance(BalanceWSMessage balance) {
		this.balance = balance;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	 
	
}
