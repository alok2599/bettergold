package in.bettergold.websocket.wsmessage;

import java.math.BigDecimal;
import java.util.ArrayList;

import in.bettergold.model.Symbol;

public class GenericDepthMessageWS {

	String event = "depthUpdate";//event
	ArrayList<LimitOrder> bids = new ArrayList<LimitOrder>();
	ArrayList<LimitOrder> offers = new ArrayList<LimitOrder>();
	Symbol symbol;
	
	public void addBid(BigDecimal b, BigDecimal q) {
		bids.add(new LimitOrder(b,q));
	}
	
	public void addOffer(BigDecimal o, BigDecimal q) {
		offers.add(new LimitOrder(o,q));
	}
	
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public Symbol getSymbol() {
		return symbol;
	}
	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}
	public ArrayList<LimitOrder> getBids() {
		return bids;
	}
	public void setBids(ArrayList<LimitOrder> bids) {
		this.bids = bids;
	}
	public ArrayList<LimitOrder> getOffers() {
		return offers;
	}
	public void setOffers(ArrayList<LimitOrder> offers) {
		this.offers = offers;
	}
	
}


class LimitOrder {
	BigDecimal price;;
	BigDecimal quantity;
	
	public LimitOrder(BigDecimal p, BigDecimal q){
		price = p;
		quantity = q;
	}
	
	public LimitOrder(){
		
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	
}
