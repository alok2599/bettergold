package in.bettergold.engine.execution;

import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import in.bettergold.model.OrderSide;
import in.bettergold.model.Pair;
import in.bettergold.model.Symbol;
import in.bettergold.websocket.wsmessage.GenericDepthMessageWS;

@Component
@Scope(value = "prototype")
public class LobImpl implements LOB {

	int trades = 0;
	// @Autowired
	TreeMap<BigDecimal, PriorityQueue<OrderBookEntry>> buyBook = new TreeMap<BigDecimal, PriorityQueue<OrderBookEntry>>(Collections.reverseOrder());
	// @Autowired
	TreeMap<BigDecimal, PriorityQueue<OrderBookEntry>> sellBook = new TreeMap<BigDecimal, PriorityQueue<OrderBookEntry>>();

	private Symbol symbol;

	@Autowired
	JmsTemplate jmsTemplate;
	
	@Autowired
	@Qualifier(value="fromLobExecutedQueue")
	Queue fromLobExecutedQueue; 
	
	@Autowired
	@Qualifier(value="fromLobCancelledQueue")
	Queue fromLobCancelledQueue;
    
	@Autowired
	@Qualifier(value="fromLobWSDepthQueue")
	Queue fromLobWSDepthQueue;
	
	ExecutedOrderMessage executedOrderMessage = null;
	CancelledOrderMessage cancelledOrderMessage = null;
	
	@Override
	public void submit(OrderBookEntry order) throws InvalidOrderSideException {
 
		//System.out.println("current depth:");
		//depth();
		if (order == null) {

			throw new InvalidOrderSideException("NULL order found");
			
		}
		
		if(order.isCancelRequested()) {
			cancel(order);
			 
		}

		else if (order.getSide() == OrderSide.BUY || order.getSide() == OrderSide.SELL) {

			add(order);
		}
		
		else
			throw new InvalidOrderSideException("OrderSide not supported");
		
		depth();

	}

	private void cancel(OrderBookEntry order) {
		
		if(order.getSide()==OrderSide.BUY) {
//			long start = System.nanoTime();
		synchronized (buyBook) {
			
			/*if(buyBook.get(order.getLimitPrice()).remove(order))
	                jmsTemplate.convertAndSend(fromLobCancelledQueue, order);
			
			if (buyBook.get(order.getLimitPrice()).isEmpty())// No more orders for that particular limit price
				buyBook.remove(order.getLimitPrice());// remove that limit price node from buyBook.

			long end = System.currentTimeMillis();
			System.out.println("direct removal: "+(end-start));*/
			
			
			if (buyBook.containsKey(order.getLimitPrice())) {

				// create a copy of the queue for this limit price
				PriorityQueue<OrderBookEntry> qCopy = new PriorityQueue<OrderBookEntry>(buyBook.get(order.getLimitPrice()));
				
				while (!qCopy.isEmpty()) {// look for required object by polling each element in the queue.
					OrderBookEntry obj = qCopy.poll();
					
					// if an element with orderId same as CANCEL orderId is found, pass that object to remove() of original queue for removal.
					if (obj.getOrderId().equals(order.getOrderId())) {
						buyBook.get(order.getLimitPrice()).remove(obj);
						
						cancelledOrderMessage = new CancelledOrderMessage();
						cancelledOrderMessage.setOrderId(obj.getOrderId());
						cancelledOrderMessage.setCancelledQuantity(obj.getQuantity());
						jmsTemplate.convertAndSend(fromLobCancelledQueue, cancelledOrderMessage);
						
						if (buyBook.get(order.getLimitPrice()).isEmpty())// No more orders for that particular limit price
							buyBook.remove(order.getLimitPrice());// remove that limit price node from buyBook.

					}

				}

			}
			
		/*	Iterator<OrderBookEntry> it = buyBook.get(order.getLimitPrice()).iterator();
			while(it.hasNext())
			{
			    if( ( (OrderBookEntry) it.next() ).getOrderId().equals(order.getOrderId()) ) {  
			        it.remove();
			        jmsTemplate.convertAndSend(fromLobCancelledQueue, order);
			         
			    }
			    	
			}*/
			
			/*long end = System.nanoTime();
			System.out.println("removal: "+start+" : "+end+" : "+(end-start));*/
			
			
			

		} 
		}
		
		else if(order.getSide()==OrderSide.SELL){
		synchronized (sellBook) {
			
			/*if(sellBook.get(order.getLimitPrice()).remove(order))
                jmsTemplate.convertAndSend(fromLobCancelledQueue, order);
			if (sellBook.get(order.getLimitPrice()).isEmpty() == true)
				sellBook.remove(order.getLimitPrice());*/

			if (sellBook.containsKey(order.getLimitPrice())) {

				// create a copy of the queue for this limit price
				PriorityQueue<OrderBookEntry> qCopy = new PriorityQueue<OrderBookEntry>(sellBook.get(order.getLimitPrice()));
				
				// look for required object by polling each element in the queue.
				while (!qCopy.isEmpty()) {
					OrderBookEntry obj = qCopy.poll();
					// if an element with orderId same as CANCEL  orderId is found, pass that object to remove() of original queue for removal.
					if (obj.getOrderId().equals( order.getOrderId())) {
						sellBook.get(order.getLimitPrice()).remove(obj);
						
						cancelledOrderMessage = new CancelledOrderMessage();
						cancelledOrderMessage.setOrderId(obj.getOrderId());
						cancelledOrderMessage.setCancelledQuantity(obj.getQuantity());
						jmsTemplate.convertAndSend(fromLobCancelledQueue, cancelledOrderMessage);
						
						if (sellBook.get(order.getLimitPrice()).isEmpty() == true)
							sellBook.remove(order.getLimitPrice());

					}

				}

			}
			
/*			Iterator<OrderBookEntry> it = sellBook.get(order.getLimitPrice()).iterator();
			while(it.hasNext())
			{
			    if( ( (OrderBookEntry) it.next() ).getOrderId().equals(order.getOrderId()) ){ // Implement sameJob 
			        it.remove();
			        }
			}*/

		}
	}

	}

	private void add(OrderBookEntry order) {

		
		if (order.getSide() == OrderSide.BUY) { 

			System.out.println("depth BEFORE adding BUY:");
			//depth();
			
			synchronized (buyBook) {

				if (buyBook.containsKey(order.getLimitPrice())) {// this price point is already present in the buyBook.
					buyBook.get(order.getLimitPrice()).add(order);// just add this order to the associated order queue

				} 
				
				
				else { // new price point. put this order in the buyBook
					PriorityQueue<OrderBookEntry> q = new PriorityQueue<OrderBookEntry>();
					q.add(order);
					buyBook.put(order.getLimitPrice(), q);
				}

			}
			System.out.println("depth after adding BUY:");
			//depth();
			execute();
			
			System.out.println("depth after executing BUY :");
			//depth();

		}

		else if (order.getSide() == OrderSide.SELL) { 

			
			
			synchronized (sellBook) {
				System.out.println("depth BEFORE adding SELL:");
				//depth();
				// this price point is already present in the buyBook. just add this order to
				// the associated order queue
				if (sellBook.containsKey(order.getLimitPrice())) {
					sellBook.get(order.getLimitPrice()).add(order);

				} else {// new price point. put this order in the buyBook
					PriorityQueue<OrderBookEntry> q = new PriorityQueue<OrderBookEntry>();
					q.add(order);
					sellBook.put(order.getLimitPrice(), q);
				}

			} // synchronized
		  
			System.out.println("depth after adding SELL:");
			//depth();
			execute();
			
			System.out.println("depth after executing SELL :");
			//depth();
		} // sell side order

	}

	public void execute() {

		synchronized (buyBook) {
			synchronized (sellBook) {

				while (!buyBook.isEmpty() && !sellBook.isEmpty()
						&& (buyBook.firstKey().compareTo( sellBook.firstKey() )) >= 0) {// market crossed.
					
					// queue of buy orders at crossed price
					PriorityQueue<OrderBookEntry> crossedBuyOrderQueue = buyBook.firstEntry().getValue();

					// queue of sell orders at crossed price
					PriorityQueue<OrderBookEntry> crossedSellOrderQueue = sellBook.firstEntry().getValue(); 
					
					BigDecimal executionPrice;

					// check whether crossing buy order arrived first or crossing sell order.
					//if (crossedBuyOrderQueue.peek().getOrderTimestamp().isAfter(crossedSellOrderQueue.peek().getOrderTimestamp()))
					if (crossedBuyOrderQueue.peek().getOrderTimestamp() < (crossedSellOrderQueue.peek().getOrderTimestamp()))
						executionPrice = crossedBuyOrderQueue.peek().getLimitPrice();
					else
						executionPrice = crossedSellOrderQueue.peek().getLimitPrice();

					// keep crossing until one of the queues exhausts at the crossing price
					while (!crossedBuyOrderQueue.isEmpty() && !crossedSellOrderQueue.isEmpty()) {
						BigDecimal tradeableQuantity = crossedBuyOrderQueue.peek().getQuantity().min(crossedSellOrderQueue.peek().getQuantity());
 
						crossedBuyOrderQueue.peek()
								.setQuantity(crossedBuyOrderQueue.peek().getQuantity().subtract(tradeableQuantity));

						crossedSellOrderQueue.peek()
								.setQuantity(crossedSellOrderQueue.peek().getQuantity().subtract(tradeableQuantity));

						System.out.println("Matched Price: " + executionPrice+" Matched Quatity: "+tradeableQuantity);
						
						long timeInMillis = System.currentTimeMillis();
						
						executedOrderMessage = new ExecutedOrderMessage();
						executedOrderMessage.setBuyBookOrderId(crossedBuyOrderQueue.peek().getOrderId());
						executedOrderMessage.setSellBookOrderId(crossedSellOrderQueue.peek().getOrderId());
						executedOrderMessage.setExecutionQuantity(tradeableQuantity);
						executedOrderMessage.setExecutionPrice(executionPrice);
						executedOrderMessage.setExecutionTimestamp(timeInMillis);
						executedOrderMessage.setTradeId(UUID.randomUUID().toString() + "-" + timeInMillis);
						
						jmsTemplate.convertAndSend(fromLobExecutedQueue, executedOrderMessage);
					 
								//
						
						if (crossedBuyOrderQueue.peek().getQuantity().compareTo(new BigDecimal(0)) == 0) { // remove matched buy order if fully executed
							crossedBuyOrderQueue.remove();

							if (crossedBuyOrderQueue.isEmpty() == true) {// no more elements in crossedBuyOrderQueue at this limit price
								buyBook.pollFirstEntry();// remove this buy limit price from buyBook

							}
						}

						if (crossedSellOrderQueue.peek().getQuantity().compareTo(new BigDecimal(0)) == 0) {//// remove matched sell order if fully executed
							crossedSellOrderQueue.remove();

							if (crossedSellOrderQueue.isEmpty() == true) {// no more elements in crossedSellOrderQueue at limit price
								sellBook.pollFirstEntry();
							}
						}

					}

				}

			}
		}
	}

	public void depth() {
		System.out.println("********************* DEPTH *************************");
		int noOfEntries = 0;
		GenericDepthMessageWS depth = new GenericDepthMessageWS();
		depth.setSymbol(symbol);
		
		TreeMap<BigDecimal, PriorityQueue<OrderBookEntry>> ReversedSellBook;
		synchronized (sellBook) {
			
			if (!sellBook.isEmpty()) {
				ReversedSellBook = new TreeMap<BigDecimal, PriorityQueue<OrderBookEntry>>(Collections.reverseOrder());
				ReversedSellBook.putAll(sellBook);
				
				for (Entry<BigDecimal, PriorityQueue<OrderBookEntry>> entry : ReversedSellBook.entrySet()) {
					
					BigDecimal key = entry.getKey();
					PriorityQueue<OrderBookEntry> value = entry.getValue();

					BigDecimal total = new BigDecimal(0);
					Iterator<OrderBookEntry> irt = value.iterator();
					while (irt.hasNext()) {
						total = total.add(irt.next().getQuantity());
					}
					System.out.println("Offers: " + key + " => " + total);
					depth.addOffer(key, total);
					noOfEntries++;
					if(noOfEntries==9)
						break;
					
					 
				}
			}

		}

		System.out.println("---------------------");
		
		synchronized (buyBook) {

			if (!buyBook.isEmpty()) {
				for (Entry<BigDecimal, PriorityQueue<OrderBookEntry>> entry : buyBook.entrySet()) {
					
					BigDecimal key = entry.getKey();
					PriorityQueue<OrderBookEntry> value = entry.getValue();

					BigDecimal total = new BigDecimal(0);
					Iterator<OrderBookEntry> irt = value.iterator();
					while (irt.hasNext()) {
						total = total.add(irt.next().getQuantity());
					}

					System.out.println("Bids: " + key + " => " + total);
					depth.addBid(key, total);
					noOfEntries++;
					if(noOfEntries==18)
						break;
				}
				

			}
		}

		
		jmsTemplate.convertAndSend(fromLobWSDepthQueue, depth);
		
	}

	public TreeMap<BigDecimal, PriorityQueue<OrderBookEntry>> getBuyBook() {
		return buyBook;
	}

	public void setBuyBook(TreeMap<BigDecimal, PriorityQueue<OrderBookEntry>> buyBook) {
		this.buyBook = buyBook;
	}

	public TreeMap<BigDecimal, PriorityQueue<OrderBookEntry>> getSellBook() {
		return sellBook;
	}

	public void setSellBook(TreeMap<BigDecimal, PriorityQueue<OrderBookEntry>> sellBook) {
		this.sellBook = sellBook;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public void setSymbol(Symbol sym) {
		this.symbol = sym;
	}

}

class BidOfferPrices{
	BigDecimal quantity;
	BigDecimal price;
	
	BidOfferPrices(BigDecimal q, BigDecimal p){
		quantity = q;
		price = p;
	}
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	
}
