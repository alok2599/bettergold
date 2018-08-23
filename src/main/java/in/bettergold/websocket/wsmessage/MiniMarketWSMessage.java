package in.bettergold.websocket.wsmessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MiniMarketWSMessage {

	String event = "marketUpdate";
	Set<MiniMarketMessage> trades;

	public MiniMarketWSMessage() {
		trades = new HashSet<MiniMarketMessage>();
	}

	public void add(MiniMarketMessage msg) {
		trades.add(msg);
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Set<MiniMarketMessage> getTrades() {
		return trades;
	}

	public void setTrades(Set<MiniMarketMessage> trades) {
		this.trades = trades;
	}

 

	
 

}